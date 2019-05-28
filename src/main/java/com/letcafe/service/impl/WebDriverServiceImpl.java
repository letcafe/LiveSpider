package com.letcafe.service.impl;

import com.letcafe.bean.HuYaProperties;
import com.letcafe.bean.WebDriverFactory;
import com.letcafe.service.CookieService;
import com.letcafe.service.WebDriverService;
import com.letcafe.util.CookieUtils;
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
    private HuYaProperties huYaProperties;

    private static final Logger logger = LoggerFactory.getLogger(WebDriverServiceImpl.class);

    @Autowired
    public WebDriverServiceImpl(CookieService cookieService, HuYaProperties huYaProperties) {
        this.cookieService = cookieService;
        this.huYaProperties = huYaProperties;
    }

    @Override
    public WebDriver getWebDriverWithCookie(String username) {
        WebDriver webDriver = WebDriverFactory.getInstance(huYaProperties);
        String cookieInitStr = "https://www.huya.com/";
        try {
            webDriver.get(cookieInitStr);
            String cookieInRedis = cookieService.getUserCookieInRedis(username);
            if (cookieInRedis == null) {
                return null;
            }
            Set<Cookie> cookies = CookieUtils.stringToCookies(cookieInRedis);
            for (Cookie cookie : cookies) {
                webDriver.manage().addCookie(cookie);
            }
        } catch (Exception ex) {
            logger.error(webDriver + "_" + ex.getMessage(), ex);
            throw new RuntimeException("Get \"" + cookieInitStr + "\" timeout or Redis Server connect error");
        }
        return webDriver;
    }
}
