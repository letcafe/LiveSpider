package com.letcafe.service.impl;

import com.letcafe.service.CookieService;
import com.letcafe.dao.RedisDao;
import com.letcafe.util.HuYaUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.letcafe.util.HuYaUtils.*;

@Service
public class HuYaCookieServiceImpl implements CookieService {

    private static final Logger logger = LogManager.getLogger(HuYaCookieServiceImpl.class);

    private RedisDao redisDao;

    @Autowired
    public HuYaCookieServiceImpl(RedisDao redisDao) {
        this.redisDao = redisDao;
    }

    @Override
    public String simulateLogin(String username, String password, boolean isOpenGUI, boolean isShowPic) {
        System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_LOCATION);
        ChromeOptions options = new ChromeOptions();

        options.addArguments("--no-sandbox");
        // start chrome without GUI
        if (!SYSTEM_IS_OPEN_GUI) {
            options.addArguments("--headless");
        } else if (!isOpenGUI) {
            options.addArguments("--headless");
        }

        if (!isShowPic) {
            Map<String, Object> prefs = new HashMap<>();
            prefs.put("profile.managed_default_content_settings.images", 2);
            options.addArguments("--disable-images");
            options.setExperimentalOption("prefs", prefs);
        }

        WebDriver webDriver = new ChromeDriver(options);
        webDriver.get("https://i.huya.com/");

        System.out.println(webDriver);
        // set huya login iframe and switch to it,then wait time to get its login form
        WebDriverWait loginFrameWait = new WebDriverWait(webDriver, 20, 500);

        try {
            loginFrameWait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".UDBSdkLgn-box")));
            logger.info("set huya login iframe and switch to it,then wait time to get its login form");

            loginFrameWait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".UDBSdkLgn-switch > a")));
            WebElement registerBtn = webDriver.findElement(By.cssSelector(".UDBSdkLgn-switch > a"));

            //wait extra 3 second for register button be clickable
            Thread.sleep(3 * 1000);
            registerBtn.click();

            logger.info("register button now click");
            loginFrameWait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".UDBSdkLgn-head > div > .login")));
            WebElement loginBtn = webDriver.findElement(By.cssSelector(".UDBSdkLgn-head > div > .login"));

            loginBtn.click();

            WebElement usernameInput, passwordInput, loginSubmit;

            loginFrameWait.until(ExpectedConditions.presenceOfElementLocated(By.className("E_acct")));

            usernameInput = webDriver.findElement(By.className("E_acct"));
            passwordInput = webDriver.findElement(By.className("E_passwd"));
            loginSubmit = webDriver.findElement(By.className("E_login"));
            // please set your own yyid and password
            usernameInput.sendKeys(username);
            passwordInput.sendKeys(password);
            logger.info("username and password has been set");

            loginSubmit.click();
            logger.info("login btn has been clicked");

            // wait 5 second for next page cookie to set
            Thread.sleep(5 * 1000);
            String loginCookie = HuYaUtils.cookieToString(webDriver.manage().getCookies());
            logger.info("webdriver get [cookie login] = " + loginCookie);
            return loginCookie;
        } catch (Exception ex) {
            logger.warn("try to get webdriver cookie failed,over time");
            ex.printStackTrace(System.out);
            webDriver.quit();
            return null;
        }
    }

    @Override
    public void setUserCookieInRedis(String username, String password) {
        int tryToLoginTime = 3;
        String cookieIntoRedis = simulateLogin(username, password, false, false);
        // try 3 time login to get cookie
        while (tryToLoginTime != 0 && cookieIntoRedis == null) {
            logger.warn("login and then get cookie failed, try to get once more,[try time] = " + (4 - tryToLoginTime));
            cookieIntoRedis = simulateLogin(username, password, false, false);
            tryToLoginTime --;
        }
        // if 3 try failed to get cookie, log this error operation
        if(cookieIntoRedis == null) {
            logger.error("login and then get cookie failed, may come into some big network error");
            return;
        }
        //redis value key for 6 day
        redisDao.setKeyValueWithExpireTime("loginCookie_" + username, cookieIntoRedis, 6 * 24 * 60 * 60 * 1000);
        logger.info("redis cookie is set,key = loginCookie_" + username + ",value = " + cookieIntoRedis);
    }

    @Override
    public String getUserCookieInRedis(String username) {
        return redisDao.getStringValue("loginCookie_" + username);
    }
}
