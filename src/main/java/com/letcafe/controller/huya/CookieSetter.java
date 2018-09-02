package com.letcafe.controller.huya;

import com.letcafe.service.CookieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import static com.letcafe.util.HuYaUtils.PASSWORD;
import static com.letcafe.util.HuYaUtils.YY_ID;

@Controller
public class CookieSetter {

    private CookieService cookieService;

    @Autowired
    public CookieSetter(CookieService cookieService) {
        this.cookieService = cookieService;
    }

    // every Sunday and Wednesday update cookie in redis
    @Scheduled(cron = "0 0 0 ? * SUN,WED")
    public void setUserLoginCookie() {
        cookieService.setUserCookieInRedis(YY_ID, PASSWORD);
    }
}
