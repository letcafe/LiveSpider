package com.letcafe.service;

import com.letcafe.bean.HuYaUserLevel;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;

import java.util.Set;

public interface HuYaUserLevelService {
    void save(HuYaUserLevel huYaUserLevel);

    void saveLoginCookie(String cookieKey, String cookieValue);

    String getLoginCookie(String cookieRedisKey);

    WebDriver getActiveHuYaLoginWebDriver(boolean isOpenGUI, boolean isShowPic);

    Set<Cookie> getAllLoginCookie();
}
