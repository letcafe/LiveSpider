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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

@Component
@ConfigurationProperties(prefix = "huya")
public class HuYaUtils {
    private static final Logger logger = LoggerFactory.getLogger(HuYaUtils.class);
    public static final String chromeDriverDestination = "C:\\Program Files (x86)\\Google\\Chrome\\Application\\chromedriver.exe";
    // init my login param
    public static String YY_ID;
    public static String PASSWORD;
    public static String COOKIE_IN_REDIS;


    public static WebDriver getActiveHuYaLoginWebDriver(boolean isOpenGUI, boolean isShowPic){
        System.setProperty("webdriver.chrome.driver", chromeDriverDestination);
        ChromeOptions options = new ChromeOptions();

        // start chrome without GUI
        if (! isOpenGUI) {
            options.addArguments("--headless");
        }

        if (! isShowPic) {
            Map<String, Object> prefs = new HashMap<>();
            prefs.put("profile.managed_default_content_settings.images", 2);
            options.addArguments("--disable-images");
            options.setExperimentalOption("prefs", prefs);
        }
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
            usernameInput.sendKeys(YY_ID);
            passwordInput.sendKeys(PASSWORD);
            logger.info("username and password has been set");

            loginBtn.click();
            logger.info("login btn has been clicked");

            WebDriverWait switchToPersonalPageWait = new WebDriverWait(webDriver, 20, 500);
            switchToPersonalPageWait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.cssSelector(".icon-filter li"), 0));
        } catch (Exception ex) {
            logger.warn("try to get webdriver cookie failed,over time");
            webDriver.close();
            return null;
        }
        return webDriver;
    }

    public static Set<Cookie> getAllLoginCookie(){
        WebDriver webDriver = getActiveHuYaLoginWebDriver(false, false);
        if(webDriver == null) {
            return null;
        }
        // if success get cookies
        Set<Cookie> cookies = webDriver.manage().getCookies();
        webDriver.close();
        return cookies;
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

    /**
     * transfer Cookie Set to String
     * @return header cookie string
     */
    public static String cookieToString(Set<Cookie> cookies) {
        while (cookies == null) {
            cookies = getAllLoginCookie();
        }

        StringBuilder loginCookieString = new StringBuilder();

        // use httpUtil to get api response,get yyuid and udb_oar then call api
        for (Cookie cookie : cookies) {
            loginCookieString.append(cookie.getName()).append("=").append(cookie.getValue()).append(";");
        }
        return loginCookieString.toString();
    }

    /**
     * transfer String to Cookie Set
     * @return header cookie set
     */
    public static Set<Cookie> stringToCookies(String cookieString) {
        Set<Cookie> cookies = new HashSet<>();
        List<String> cookieList = new ArrayList<>();
        String[] cookieArray = cookieString.split(";");
        Collections.addAll(cookieList, cookieArray);
        for (String cookie : cookieList) {
            String[] cookieKV = cookie.split("=");
            String cookieKey = cookieKV[0];
            String cookieValue;
            // sometimes cookie value is null, it may cause indexOutOfBoundsException, So do as follow
            if (cookieKV.length == 2) {
                cookieValue = cookieKV[1];
            } else {
                cookieValue = "";
            }
            cookies.add(new Cookie(cookieKey, cookieValue));
        }
        return cookies;
    }


    public static void setYyId(String yyId) {
        YY_ID = yyId;
    }

    public static void setPASSWORD(String PASSWORD) {
        HuYaUtils.PASSWORD = PASSWORD;
    }

    public static void setCookieInRedis(String cookieInRedis) {
        COOKIE_IN_REDIS = cookieInRedis;
    }
}
