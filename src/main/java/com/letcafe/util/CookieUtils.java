package com.letcafe.util;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class CookieUtils {
    private static final Logger logger = LoggerFactory.getLogger(HuYaUtils.class);

    /**
     * transfer Cookie Set to String
     *
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
     *
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

    public static void printWebDriverCookies(WebDriver webDriver) {
        Set<Cookie> cookies = webDriver.manage().getCookies();
        for (Cookie cookie : cookies) {
            logger.info(cookie.getName() + "=>" + cookie.getValue() + ",expire time = " + cookie.getExpiry());
        }
    }
}
