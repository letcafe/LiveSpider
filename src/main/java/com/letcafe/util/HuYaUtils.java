package com.letcafe.util;

import com.letcafe.bean.BrowserType;
import com.letcafe.bean.WebDriverFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class HuYaUtils {

    private static final Logger logger = LoggerFactory.getLogger(HuYaUtils.class);

    /**
     * 通过Setter方法注入值，注意setter方法不需要加static修饰
     */
    public static String YY_ID;
    public static String PASSWORD;
    public static String COOKIE_IN_REDIS;
    public static BrowserType BROWSER_TYPE;
    public static String BROWSER_BINARY_LOCATION;
    public static String BROWSER_DRIVER_LOCATION;
    public static Boolean SYSTEM_IS_OPEN_GUI;
    public static Integer CPU_CORE;
    public static Integer ATTEMPT_TIMES;
    public static final String CHECK_LOGIN_STRING = ";username=";

    @Value("${huya.YY_ID}")
    public void setYyId(String yyId) {
        YY_ID = yyId;
    }

    @Value("${huya.PASSWORD}")
    public void setPASSWORD(String password) {
        PASSWORD = password;
    }

    @Value("${huya.COOKIE_IN_REDIS}")
    public void setCookieInRedis(String cookieInRedis) {
        COOKIE_IN_REDIS = cookieInRedis;
    }

    @Value("${huya.BROWSER_TYPE}")
    public void setBrowserType(String browserType) {
        BROWSER_TYPE = BrowserType.valueOf(browserType);
    }

    @Value("${huya.BROWSER_BINARY_LOCATION}")
    public void setBrowserBinaryLocation(String browserBinaryLocation) {
        BROWSER_BINARY_LOCATION = browserBinaryLocation;
    }

    @Value("${huya.BROWSER_DRIVER_LOCATION}")
    public void setBrowserDriverLocation(String browserDriverLocation) {
        BROWSER_DRIVER_LOCATION = browserDriverLocation;
    }

    @Value("${huya.SYSTEM_IS_OPEN_GUI}")
    public void setSystemIsOpenGui(Boolean systemIsOpenGui) {
        SYSTEM_IS_OPEN_GUI = systemIsOpenGui;
    }

    @Value("#{T(Runtime).getRuntime().availableProcessors()}")
    public void setCpuCore(Integer cpuCore) {
        CPU_CORE = cpuCore;
    }

    @Value("${huya.ATTEMPT_TIMES}")
    public void setAttemptTime(Integer attemptTime) {
        ATTEMPT_TIMES = attemptTime;
    }

    /**
     * 通过WebDriver登录，当登录完成后，返回用户Cookie
     *
     * @param username 虎牙用户名
     * @param password 虎牙密码
     * @return 登录后获得的Cookie字符串，用于存储于Redis中
     */
    public static String login(String username, String password) {
        WebDriver webDriver = WebDriverFactory.getInstance();
        webDriver.get("https://i.huya.com/");
        // set huya login iframe and switch to it,then wait time to get its login form
        WebDriverWait loginFrameWait = new WebDriverWait(webDriver, 20, 500);
        JavascriptExecutor js = (JavascriptExecutor) webDriver;

        try {
            loginFrameWait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".UDBSdkLgn-box")));
            logger.info("[Login] searching for className='.UDBSdkLgn-box'...");

            WebElement loginMultiBox = webDriver.findElement(By.cssSelector(".UDBSdkLgn-box"));

            List<WebElement> loginInnerBoxList = loginMultiBox.findElements(By.cssSelector(".UDBSdkLgn-inner"));
            for (WebElement loginBox : loginInnerBoxList) {
                String loginBoxCssClass = loginBox.getAttribute("className");
                if (loginBoxCssClass.contains("account")) {
                    logger.info("[Login] the account login with password panel has shown");
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
            logger.info("[Login] username and password has been set");
            loginSubmit.click();
            logger.info("[Login] login btn has been clicked");
            TimeUnit.SECONDS.sleep(8);
            return CookieUtils.cookieToString(webDriver.manage().getCookies());
        } catch (Exception ex) {
            logger.error(webDriver.toString() + "_" + ex.getMessage(), ex);
            ex.printStackTrace(System.err);
            webDriver.quit();
            // sleep被打断，返回null表示失败
            return null;
        }
    }
}
