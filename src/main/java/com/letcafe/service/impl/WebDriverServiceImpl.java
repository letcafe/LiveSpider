package com.letcafe.service.impl;

import com.letcafe.service.CookieService;
import com.letcafe.service.WebDriverService;
import com.letcafe.util.HuYaUtils;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.letcafe.util.HuYaUtils.CHROME_DRIVER_PATH;

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
        System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_PATH);
        ChromeOptions options = new ChromeOptions();

        options.addArguments("--no-sandbox");
        options.addArguments("--headless");

        // show no picture
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("profile.managed_default_content_settings.images", 2);
        options.addArguments("--disable-images");
        options.setExperimentalOption("prefs", prefs);
        WebDriver webDriver = new ChromeDriver(options);
        try {
            webDriver.get("https://www.huya.com/");
            String cookieInRedis = cookieService.getUserCookieInRedis(username);
            Set<Cookie> cookies = HuYaUtils.stringToCookies(cookieInRedis);
            for (Cookie cookie : cookies) {
                webDriver.manage().addCookie(cookie);
            }
        } catch (Exception ex) {
            logger.error("[WebDriver Error] = " + ex.getMessage());
            ex.printStackTrace(System.err);
            webDriver.quit();
            return null;
        }
        return webDriver;
    }
}
