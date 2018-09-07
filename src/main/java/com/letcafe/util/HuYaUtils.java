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
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

@Component
@ConfigurationProperties(prefix = "huya")
public class HuYaUtils {
    private static final Logger logger = LoggerFactory.getLogger(HuYaUtils.class);
    // init my login param
    public static String YY_ID;
    public static String PASSWORD;
    public static String COOKIE_IN_REDIS;
    public static String CHROME_DRIVER_LOCATION;
    public static Boolean SYSTEM_IS_OPEN_GUI;

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

    public static void setChromeDriverLocation(String chromeDriverLocation) {
        CHROME_DRIVER_LOCATION = chromeDriverLocation;
    }

    public static void setSystemIsOpenGui(Boolean systemIsOpenGui) {
        SYSTEM_IS_OPEN_GUI = systemIsOpenGui;
    }
}
