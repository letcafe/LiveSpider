package com.letcafe.service.impl;

import com.letcafe.service.CookieService;
import com.letcafe.dao.RedisDao;
import com.letcafe.util.HuYaUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.letcafe.util.HuYaUtils.*;

@Service
@Transactional
public class HuYaCookieServiceImpl implements CookieService {

    private static final Logger logger = LogManager.getLogger(HuYaCookieServiceImpl.class);

    private RedisDao redisDao;

    @Autowired
    public HuYaCookieServiceImpl(RedisDao redisDao) {
        this.redisDao = redisDao;
    }

    @Override
    public String simulateLogin(String username, String password, boolean isOpenGUI, boolean isShowPic) {
        System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_PATH);
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
        WebDriverWait loginFrameWait = new WebDriverWait(webDriver, 20, 500);
        JavascriptExecutor js = (JavascriptExecutor) webDriver;

        try {
            loginFrameWait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".UDBSdkLgn-box")));
            logger.info("[System : New Cookie] searching for className='.UDBSdkLgn-box'...");

            WebElement loginMultiBox = webDriver.findElement(By.cssSelector(".UDBSdkLgn-box"));

            List<WebElement> loginInnerBoxList = loginMultiBox.findElements(By.cssSelector(".UDBSdkLgn-inner")) ;
            for (WebElement loginBox : loginInnerBoxList) {
                String loginBoxCssClass = loginBox.getAttribute("className");
                if (loginBoxCssClass.contains("account")) {
                    logger.info("[System : New Cookie] the account login with password panel has shown");
                    js.executeScript("arguments[0].className=arguments[1]", loginBox, "NaN");
                }
            }
            WebElement usernameInput, passwordInput, loginSubmit;

            usernameInput = webDriver.findElement(By.className("E_acct"));
            passwordInput = webDriver.findElement(By.className("E_passwd"));
            loginSubmit = webDriver.findElement(By.className("E_login"));
            // 设置yyid和password
            usernameInput.sendKeys(username);
            passwordInput.sendKeys(password);
            logger.info("[System : New Cookie] username and password has been set");

            loginSubmit.click();
            logger.info("[System : New Cookie] login btn has been clicked");

            TimeUnit.SECONDS.sleep(5);
            return HuYaUtils.cookieToString(webDriver.manage().getCookies());
        } catch (Exception ex) {
            logger.warn("[System : New Cookie] try to get webdriver cookie failed, over time");
            ex.printStackTrace(System.out);
            return null;
        } finally {
            webDriver.quit();
        }
    }

    @Override
    public void setUserCookieInRedis(String username, String password) {
        int tryToLoginTime = 3;
        String cookieIntoRedis = simulateLogin(username, password, false, false);
        // 三次尝试登陆以获取Cookie
        while (tryToLoginTime != 0 && cookieIntoRedis == null) {
            logger.warn("[Cookie : Redis] login and then get cookie failed, try to get once more,[try time] = " + (4 - tryToLoginTime));
            cookieIntoRedis = simulateLogin(username, password, false, false);
            tryToLoginTime --;
        }
        // 如果尝试3次失败，都未获得到Cookie，那么日志记录故障
        if(cookieIntoRedis == null) {
            logger.error("[Cookie : Redis] login and then get cookie failed, may come into some network error");
            return;
        }
        // Redis中的值将保留六天，配合周三周日刷新，不会失效
        redisDao.setKeyValueWithExpireTime("loginCookie:" + username, cookieIntoRedis, 6 * 24 * 60 * 60 * 1000);
    }

    @Override
    public String getUserCookieInRedis(String username) {
        return redisDao.getStringValue("loginCookie:" + username);
    }
}
