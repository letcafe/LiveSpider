package com.letcafe.service;

import lombok.Data;
import org.openqa.selenium.WebDriver;

public interface WebDriverService {
    WebDriver getWebDriverWithCookie(String username);
}
