package com.letcafe.util;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class HuYaUtils {

    private static final Logger logger = LoggerFactory.getLogger(HuYaUtils.class);

    /**
     * 通过WebDriver登录，当登录完成后，返回用户Cookie
     *
     * @param webDriver 浏览器驱动
     * @param username 虎牙用户名
     * @param password 虎牙密码
     * @return 登录后获得的Cookie字符串，返回null说明模拟登陆失败
     */
    public static String login(WebDriver webDriver, String username, String password) {
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
