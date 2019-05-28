package com.letcafe.bean;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "huya")
@Component
public class HuYaProperties {
    private String yyId;
    private String password;
    private String cookieInRedis;
    private BrowserType browserType;
    private String browserBinaryLocation;
    private String browserDriverLocation;
    private Boolean openGui;
    private Integer cpuCore;
    private Integer attemptTimes;
    private String checkLoginStr;

    public String getYyId() {
        return yyId;
    }

    public void setYyId(String yyId) {
        this.yyId = yyId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCookieInRedis() {
        return cookieInRedis;
    }

    public void setCookieInRedis(String cookieInRedis) {
        this.cookieInRedis = cookieInRedis;
    }

    public BrowserType getBrowserType() {
        return browserType;
    }

    public void setBrowserType(BrowserType browserType) {
        this.browserType = browserType;
    }

    public String getBrowserBinaryLocation() {
        return browserBinaryLocation;
    }

    public void setBrowserBinaryLocation(String browserBinaryLocation) {
        this.browserBinaryLocation = browserBinaryLocation;
    }

    public String getBrowserDriverLocation() {
        return browserDriverLocation;
    }

    public void setBrowserDriverLocation(String browserDriverLocation) {
        this.browserDriverLocation = browserDriverLocation;
    }

    public Boolean getOpenGui() {
        return openGui;
    }

    public void setOpenGui(Boolean openGui) {
        this.openGui = openGui;
    }

    public Integer getCpuCore() {
        return cpuCore;
    }

    public Integer getAttemptTimes() {
        return attemptTimes;
    }

    public void setAttemptTimes(Integer attemptTimes) {
        this.attemptTimes = attemptTimes;
    }

    public String getCheckLoginStr() {
        return checkLoginStr;
    }

    public void setCheckLoginStr(String checkLoginStr) {
        this.checkLoginStr = checkLoginStr;
    }

    @Value("#{T(Runtime).getRuntime().availableProcessors()}")
    public void setCpuCore(Integer cpuCore) {
        this.cpuCore = cpuCore;
    }

    @Override
    public String toString() {
        return "HuYaProperties{" +
                "yyId='" + yyId + '\'' +
                ", password='" + password + '\'' +
                ", cookieInRedis='" + cookieInRedis + '\'' +
                ", browserType=" + browserType +
                ", browserBinaryLocation='" + browserBinaryLocation + '\'' +
                ", browserDriverLocation='" + browserDriverLocation + '\'' +
                ", openGui=" + openGui +
                ", cpuCore=" + cpuCore +
                ", attemptTimes=" + attemptTimes +
                ", checkLoginStr='" + checkLoginStr + '\'' +
                '}';
    }
}
