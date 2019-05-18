package com.letcafe.service.impl;

import com.letcafe.bean.WebDriverFactory;
import com.letcafe.service.CookieService;
import com.letcafe.service.WebDriverService;
import com.letcafe.util.HuYaUtils;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;


@Service
public class WebDriverServiceImpl implements WebDriverService {

    private CookieService cookieService;

    private static final Logger logger = LoggerFactory.getLogger(WebDriverServiceImpl.class);

    @Autowired
    public WebDriverServiceImpl(CookieService cookieService) {
        this.cookieService = cookieService;
    }

    @Override
    public WebDriver getWebDriverWithCookie(String username) {
        WebDriver webDriver = WebDriverFactory.getInstance();
        try {
            webDriver.get("https://www.huya.com/");
            String cookieInRedis = cookieService.getUserCookieInRedis(username);
            if (cookieInRedis == null) {
                return null;
            }
            Set<Cookie> cookies = HuYaUtils.stringToCookies(cookieInRedis);
            for (Cookie cookie : cookies) {
                webDriver.manage().addCookie(cookie);
            }
        } catch (Exception ex) {
            logger.error("[WebDriver Error] = " + ex.getMessage());
            ex.printStackTrace(System.err);
            return null;
        } finally {
            webDriver.quit();
        }
        return webDriver;
    }
}
