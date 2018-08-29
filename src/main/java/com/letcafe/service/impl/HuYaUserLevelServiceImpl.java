package com.letcafe.service.impl;

import com.letcafe.bean.HuYaUserLevel;
import com.letcafe.dao.HuYaUserLevelDao;
import com.letcafe.dao.RedisDao;
import com.letcafe.service.HuYaUserLevelService;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.letcafe.util.HuYaUtils.*;

@ConfigurationProperties(prefix = "huya")
@Service
@Transactional
public class HuYaUserLevelServiceImpl implements HuYaUserLevelService {

    private static final Logger logger = LoggerFactory.getLogger(HuYaUserLevelServiceImpl.class);

    private HuYaUserLevelDao userLevelDao;
    private RedisDao redisDao;

    @Autowired
    public HuYaUserLevelServiceImpl(HuYaUserLevelDao userLevelDao, RedisDao redisDao) {
        this.userLevelDao = userLevelDao;
        this.redisDao = redisDao;
    }

    @Override
    public void save(HuYaUserLevel huYaUserLevel) {
        userLevelDao.save(huYaUserLevel);
    }

    @Override
    public void saveLoginCookie(String cookieKey, String cookieValue) {
        redisDao.setKeyValueWithExpireTime(cookieKey, cookieValue, 7 * 24 * 3600 * 1000);
    }

    @Override
    public String getLoginCookie(String cookieRedisKey) {
        return redisDao.getStringValue(cookieRedisKey);
    }

    @Override
    public WebDriver getActiveHuYaLoginWebDriver(boolean isOpenGUI, boolean isShowPic) {
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

        // set huya login iframe and switch to it,then wait time to get its login form
        WebDriverWait loginFrameWait = new WebDriverWait(webDriver, 30, 500);
        loginFrameWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".UDBSdkLgn-box")));
        logger.info("set huya login iframe and switch to it,then wait time to get its login form");

        loginFrameWait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".UDBSdkLgn-switch a")));
        WebElement registerBtn = webDriver.findElement(By.cssSelector(".UDBSdkLgn-switch a"));
        registerBtn.click();
        logger.info("register button now click");
        loginFrameWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".UDBSdkLgn-head .login")));
        WebElement loginBtn = webDriver.findElement(By.cssSelector(".UDBSdkLgn-head .login"));
        logger.info("loginSwitch button now click");
        loginBtn.click();

        WebElement usernameInput, passwordInput, loginSubmit;
        try {
            loginFrameWait.until(ExpectedConditions.visibilityOfElementLocated(By.className("E_acct")));

            usernameInput = webDriver.findElement(By.className("E_acct"));
            passwordInput = webDriver.findElement(By.className("E_passwd"));
            loginSubmit = webDriver.findElement(By.className("E_login"));
            // please set your own yyid and password
            usernameInput.sendKeys(YY_ID);
            passwordInput.sendKeys(PASSWORD);
            logger.info("username and password has been set");

            loginSubmit.click();
            logger.info("login btn has been clicked");

            Thread.sleep(5 * 1000);
//            loginFrameWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".icon-filter li")));
        } catch (Exception ex) {
            logger.warn("try to get webdriver cookie failed,over time");
            webDriver.quit();
            return null;
        }
        return webDriver;
    }

    public Set<Cookie> getAllLoginCookie() {
        WebDriver webDriver = getActiveHuYaLoginWebDriver(false, false);
        if (webDriver == null) {
            return null;
        }
        // if success get cookies
        Set<Cookie> cookies = webDriver.manage().getCookies();
        webDriver.close();
        return cookies;
    }
}
