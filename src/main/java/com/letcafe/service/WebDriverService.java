package com.letcafe.service;

import org.openqa.selenium.WebDriver;

public interface WebDriverService {
    WebDriver getWebDriverWithCookie(String username);
}
