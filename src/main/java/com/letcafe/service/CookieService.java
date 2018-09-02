package com.letcafe.service;

public interface CookieService {

    String simulateLogin(String username, String password, boolean isOpenGUI, boolean isShowPic);

    void setUserCookieInRedis(String username, String password);

    String getUserCookieInRedis(String username);
}
