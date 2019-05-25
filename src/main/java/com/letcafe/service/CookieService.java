package com.letcafe.service;

public interface CookieService {

    void setUserCookieInRedis(String username, String password);

    String getUserCookieInRedis(String username);
}
