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

    /**
     * 每个周日、周三零点更新redis中的Cookie值
     */
    @Scheduled(cron = "${huya.user.cookie.time.setUserLoginCookie}")
    public void setUserLoginCookie() {
        cookieService.setUserCookieInRedis(YY_ID, PASSWORD);
    }
}
