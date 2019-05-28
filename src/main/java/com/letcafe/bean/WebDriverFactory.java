package com.letcafe.bean;

import com.letcafe.exception.BrowserTypeUnsupportedException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class WebDriverFactory {

    private static final Logger logger = LoggerFactory.getLogger(WebDriverFactory.class);

    /**
     * 获取对应的WebDriver
     * @return 获取对应的WebDriver(目前获得的是非单例的，即每次new)
     */
    public static WebDriver getInstance(HuYaProperties huYaProperties) {
        // 该出可优化为只执行一次
        System.setProperty(huYaProperties.getBrowserType().getSystemPropertyName(), huYaProperties.getBrowserDriverLocation());
        WebDriver webDriver = getDriverByType(huYaProperties);
        webDriver.manage().window().maximize();
        return webDriver;
    }

    @NotNull
    private static WebDriver getDriverByType(HuYaProperties huYaProperties) {
        // 根据YAML初始化WebDriver的环境变量
        // 根据类型进行实例化
        switch (huYaProperties.getBrowserType()) {
            case CHROME:
                return getChromeDriver(huYaProperties);
            case FIRE_FOX:
                return getFireFoxDriver();
            case EVENT_FIRING:
                return getEventFiringDriver();
            case EDGE:
                return getEdgeDriver();
            case INTERNET_EXPLORER:
                return getInternetExplorerDriver();
            case OPERA:
                return getOperaDriver();
            case SAFARI:
                return getSafariDriver();
            case REMOTE_WEB:
                return getRemoteDriver();
            default:
                throw new BrowserTypeUnsupportedException(huYaProperties.getBrowserType() + " is not in supported browser list("
                        + Arrays.toString(BrowserType.values()) + ")");
        }
    }

    private static WebDriver getChromeDriver(HuYaProperties huYaProperties) {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox");
        // 是否开启GUI
        if (!huYaProperties.getOpenGui()) {
            // 设置使用headless模式(不需要开启GUI)
            options.addArguments("--headless");
        }
        // 设置chrome.exe路径
        options.setBinary(huYaProperties.getBrowserBinaryLocation());
        // 设置不显示图片
        Map<String, Object> prefs = new HashMap<>(2);
        prefs.put("profile.managed_default_content_settings.images", 2);
        options.addArguments("--disable-images");
        options.setExperimentalOption("prefs", prefs);
        return new ChromeDriver(options);
    }

    private static WebDriver getRemoteDriver() {
        logger.info("[getRemoteDriver]");
        return null;
    }

    private static WebDriver getSafariDriver() {
        logger.info("[getSafariDriver]");
        return null;
    }

    private static WebDriver getOperaDriver() {
        logger.info("[getOperaDriver]");
        return null;
    }

    private static WebDriver getInternetExplorerDriver() {
        logger.info("[getInternetExplorerDriver]");
        return null;
    }

    private static WebDriver getEdgeDriver() {
        logger.info("[getEdgeDriver]");
        return null;
    }

    private static WebDriver getEventFiringDriver() {
        logger.info("[getEventFiringDriver]");
        return null;
    }

    private static WebDriver getFireFoxDriver() {
        logger.info("[getFireFoxDriver]");
        return null;
    }

}