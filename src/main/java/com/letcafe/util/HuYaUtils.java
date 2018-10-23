package com.letcafe.util;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class HuYaUtils {
    private static final Logger logger = LoggerFactory.getLogger(HuYaUtils.class);

    // 通过Setter方法注入值，注意setter方法不需要加static修饰
    public static String YY_ID;
    public static String PASSWORD;
    public static String COOKIE_IN_REDIS;
    public static String CHROME_DRIVER_LOCATION;
    public static Boolean SYSTEM_IS_OPEN_GUI;
    public static Integer CPU_CORE;

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

    @Value("${huya.YY_ID}")
    public void setYyId(String yyId) {
        YY_ID = yyId;
    }
    @Value("${huya.PASSWORD}")
    public void setPASSWORD(String PASSWORD) {
        HuYaUtils.PASSWORD = PASSWORD;
    }

    @Value("loginCookie_${huya.YY_ID}")
    public void setCookieInRedis(String cookieInRedis) {
        COOKIE_IN_REDIS = cookieInRedis;
    }
    @Value("${huya.CHROME_DRIVER_LOCATION}")
    public void setChromeDriverLocation(String chromeDriverLocation) {
        CHROME_DRIVER_LOCATION = chromeDriverLocation;
    }

    @Value("${huya.SYSTEM_IS_OPEN_GUI}")
    public void setSystemIsOpenGui(Boolean systemIsOpenGui) {
        SYSTEM_IS_OPEN_GUI = systemIsOpenGui;
    }

    @Value("#{T(Runtime).getRuntime().availableProcessors()}")
    public void setCpuCore(Integer cpuCore) {
        CPU_CORE = cpuCore;
    }
}
