package com.letcafe.service;

public interface CookieService {

    String simulateLogin(String username, String password);

    void setUserCookieInRedis(String username, String password);

    String getUserCookieInRedis(String username);
}
