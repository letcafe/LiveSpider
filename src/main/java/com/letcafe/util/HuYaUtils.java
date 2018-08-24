package com.letcafe.util;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

public class HuYaUtils {
    private static final Logger logger = LoggerFactory.getLogger(HuYaUtils.class);
    private static final String chromeDriverDestination = "C:\\Program Files (x86)\\Google\\Chrome\\Application\\chromedriver.exe";


    private static Set<Cookie> getAllLoginCookie(String username, String password){
        System.setProperty("webdriver.chrome.driver", chromeDriverDestination);
        ChromeOptions options = new ChromeOptions();

        // start chrome without GUI
        options.addArguments("--headless");

        ChromeDriverService chromeDriverService = ChromeDriverService.createDefaultService();
        WebDriver webDriver = new ChromeDriver(chromeDriverService, options);
        webDriver.get("https://i.huya.com/");

        // set huya login iframe and switch to it,then wait time to get its login form
        WebDriverWait loginFrameWait = new WebDriverWait(webDriver, 20, 500);
        loginFrameWait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("udbsdk_frm_normal"));
        logger.info("set huya login iframe and switch to it,then wait time to get its login form");

        WebElement usernameInput, passwordInput, loginBtn;
        try {
            usernameInput = webDriver.findElement(By.className("E_acct"));
            passwordInput = webDriver.findElement(By.className("E_passwd"));
            loginBtn = webDriver.findElement(By.cssSelector("#m_commonLogin .form_item .E_login"));
            // please set your own yyid and password
            usernameInput.sendKeys(username);
            passwordInput.sendKeys(password);
            logger.info("username and password has been set");

            loginBtn.click();
            logger.info("login btn has been clicked");

            WebDriverWait switchToPersonalPageWait = new WebDriverWait(webDriver, 20, 500);
            switchToPersonalPageWait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.cssSelector(".icon-filter li"), 0));
        } catch (Exception ex) {
            logger.warn("try to get E_acct,E_passwd,loginBtn failed,over time");
            webDriver.close();
            return null;
        }

        // if success get cookies
        Set<Cookie> cookies = webDriver.manage().getCookies();
        webDriver.close();
        return cookies;
    }

    /**
     * ensure one can get api call cookie String"yyuid=xxx;udb_oar=xxx;"
     * @param username user login name
     * @param password user login password
     * @return header cookie string
     */
    public static String getLoginCookie(String username, String password) {
        Set<Cookie> cookies = null;
        while (cookies == null) {
            cookies = getAllLoginCookie(username, password);
        }

        Cookie yyuid = null;
        Cookie udb_oar = null;

        // use httpUtil to get api response,get yyuid and udb_oar then call api
        for (Cookie cookie : cookies) {
            if ("yyuid".equals(cookie.getName())) {
                yyuid = cookie;
            }
            if ("udb_oar".equals(cookie.getName())) {
                udb_oar = cookie;
            }
        }
        if (yyuid == null || udb_oar == null) {
            logger.info("yyuid or udb_oar is empty,get cookie failed");
            return "";
        }

        return yyuid.getName() + "=" + yyuid.getValue() + ";" + udb_oar.getName() + "=" + udb_oar.getValue();
    }

    /**
     * ensure can get huya task information json string
     * @param headerMap header cookie include "yyuid" and "udb_oar"
     * @return filter and do with huya task information
     */
    public static String getUserTaskInfo(Map<String, String> headerMap) {
        HttpResponse response = HttpUtils.doGet("https://www.huya.com/member/task.php?m=User&do=listTotal&callback=huyaNavUserCard", headerMap);

        int StatusCode = response.getStatusLine().getStatusCode();
        String dataObjStr = "";

        if(StatusCode == 200) {
            try {
                dataObjStr = EntityUtils.toString(response.getEntity(), "utf-8");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return dataObjStr.substring(dataObjStr.indexOf('{'), dataObjStr.lastIndexOf('}') + 1);
    }

    public static void printWebDriverCookies(WebDriver webDriver) {
        Set<Cookie> cookies = webDriver.manage().getCookies();
        for (Cookie cookie : cookies) {
            logger.info(cookie.getName() + "=>" + cookie.getValue() + ",expire time = " + cookie.getExpiry());
        }
    }
}
