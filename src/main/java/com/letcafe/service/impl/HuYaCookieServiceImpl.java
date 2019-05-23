package com.letcafe.service.impl;

import com.letcafe.bean.WebDriverFactory;
import com.letcafe.service.CookieService;
import com.letcafe.dao.RedisDao;
import com.letcafe.util.CookieUtils;
import com.letcafe.util.HuYaUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.concurrent.TimeUnit;


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
    public String simulateLogin(String username, String password) {
        WebDriver webDriver = WebDriverFactory.getInstance();
        webDriver.get("https://i.huya.com/");
        // set huya login iframe and switch to it,then wait time to get its login form
        WebDriverWait loginFrameWait = new WebDriverWait(webDriver, 20, 500);
        JavascriptExecutor js = (JavascriptExecutor) webDriver;

        try {
            loginFrameWait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".UDBSdkLgn-box")));
            logger.info("[System : New Cookie] searching for className='.UDBSdkLgn-box'...");

            WebElement loginMultiBox = webDriver.findElement(By.cssSelector(".UDBSdkLgn-box"));

            List<WebElement> loginInnerBoxList = loginMultiBox.findElements(By.cssSelector(".UDBSdkLgn-inner"));
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

            TimeUnit.SECONDS.sleep(8);
            return CookieUtils.cookieToString(webDriver.manage().getCookies());
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
        for (int i = 1; i <= HuYaUtils.ATTEMPT_TIMES; i++) {
            String cookieIntoRedis = simulateLogin(username, password);
            // 如果找到了正确的Cookie
            if (cookieIntoRedis != null && cookieIntoRedis.contains(HuYaUtils.CHECK_LOGIN_STRING)) {
                // Redis中的值将保留六天，配合周三周日刷新，不会失效(后面的计算6天的值，会由编译器进行优化编译)
                redisDao.setKeyValueWithExpireTime("loginCookie:" + username, cookieIntoRedis, 6 * 24 * 60 * 60 * 1000);
                return;
            }
            // 如果没找到，开始重试
            logger.warn("[Cookie Setter] login failed, try for " + i + " times");
        }
        // 重试超过失败次数，打印错误日志
        logger.error("[Cookie Setter] finally failed, may come into some network error");
    }

    @Override
    public String getUserCookieInRedis(String username) {
        return redisDao.getStringValue(HuYaUtils.COOKIE_IN_REDIS);
    }
}
