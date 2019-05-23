package com.letcafe.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class HuYaUtils {

    /**
     * 通过Setter方法注入值，注意setter方法不需要加static修饰
     */
    public static String YY_ID;
    public static String PASSWORD;
    public static String COOKIE_IN_REDIS;
    public static String BROWSER_TYPE;
    public static String BROWSER_BINARY_LOCATION;
    public static String BROWSER_DRIVER_LOCATION;
    public static Boolean SYSTEM_IS_OPEN_GUI;
    public static Integer CPU_CORE;
    public static Integer ATTEMPT_TIMES;
    public static final String CHECK_LOGIN_STRING = ";username=";

    @Value("${huya.YY_ID}")
    public void setYyId(String yyId) {
        YY_ID = yyId;
    }
    @Value("${huya.PASSWORD}")
    public void setPASSWORD(String password) {
        PASSWORD = password;
    }

    @Value("${huya.COOKIE_IN_REDIS}")
    public void setCookieInRedis(String cookieInRedis) {
        COOKIE_IN_REDIS = cookieInRedis;
    }

    @Value("${huya.BROWSER_TYPE}")
    public void setBrowserType(String browserType) {
        BROWSER_TYPE = browserType;
    }

    @Value("${huya.BROWSER_BINARY_LOCATION}")
    public void setBrowserBinaryLocation(String browserBinaryLocation) {
        BROWSER_BINARY_LOCATION = browserBinaryLocation;
    }

    @Value("${huya.BROWSER_DRIVER_LOCATION}")
    public void setBrowserDriverLocation(String browserDriverLocation) {
        BROWSER_DRIVER_LOCATION = browserDriverLocation;
    }

    @Value("${huya.SYSTEM_IS_OPEN_GUI}")
    public void setSystemIsOpenGui(Boolean systemIsOpenGui) {
        SYSTEM_IS_OPEN_GUI = systemIsOpenGui;
    }

    @Value("#{T(Runtime).getRuntime().availableProcessors()}")
    public void setCpuCore(Integer cpuCore) {
        CPU_CORE = cpuCore;
    }

    @Value("${huya.ATTEMPT_TIMES}")
    public void setAttemptTime(Integer attemptTime) {
        ATTEMPT_TIMES = attemptTime;
    }
}
