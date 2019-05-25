package com.letcafe.bean;

public enum BrowserType {
    // chrome浏览器
    CHROME("chrome", "webdriver.chrome.driver"),
    // 火狐浏览器
    FIRE_FOX("firefox", "webdriver.gecko.driver"),
    // Event Firing浏览器
    EVENT_FIRING("eventFiring", "webdriver.eventFiring.driver"),
    // Edge浏览器
    EDGE("edge", "webdriver.edge.driver"),
    // IE浏览器
    INTERNET_EXPLORER("internetExplorer", "webdriver.ie.driver"),
    // Opera（欧朋）浏览器
    OPERA("opera", "webdriver.opera.driver"),
    // IOS浏览器
    SAFARI("safari", "webdriver.safari.driver"),
    // 远控浏览器
    REMOTE_WEB("remoteWeb", "webdriver.remoteWeb.driver");

    String id;
    String systemPropertyName;

    BrowserType(String id, String systemPropertyName) {
        this.id = id;
        this.systemPropertyName = systemPropertyName;
    }

}
