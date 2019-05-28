package com.letcafe.controller.huya;

import com.letcafe.bean.HuYaProperties;
import com.letcafe.service.CookieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;


@Controller
public class CookieSetter {

    private CookieService cookieService;
    private HuYaProperties huYaProperties;

    @Autowired
    public CookieSetter(CookieService cookieService, HuYaProperties huYaProperties) {
        this.cookieService = cookieService;
        this.huYaProperties = huYaProperties;
    }

    /**
     * 每个周日、周三零点更新redis中的Cookie值
     */
    @Scheduled(cron = "${huya.user.cookie.time.setUserLoginCookie}")
    public void setUserLoginCookie() {
        cookieService.setUserCookieInRedis(huYaProperties.getYyId(), huYaProperties.getPassword());
    }
}
