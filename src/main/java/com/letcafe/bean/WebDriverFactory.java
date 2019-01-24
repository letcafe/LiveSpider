package com.letcafe.bean;

import com.letcafe.util.HuYaUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.letcafe.util.HuYaUtils.BROWSER_BINARY_LOCATION;
import static com.letcafe.util.HuYaUtils.BROWSER_DRIVER_LOCATION;
import static com.letcafe.util.HuYaUtils.SYSTEM_IS_OPEN_GUI;

public class WebDriverFactory {

    private static final Logger logger = LoggerFactory.getLogger(WebDriverFactory.class);

    public static WebDriver getInstance() {
        BrowserType browserType = null;
        WebDriver webDriver = null;
        for (BrowserType tmp : BrowserType.values()) {
            if (HuYaUtils.BROWSER_TYPE.equals(tmp.id)) {
                browserType = tmp;
                // 设置各个驱动的环境变量
                System.setProperty(tmp.systemPropertyName, BROWSER_DRIVER_LOCATION);
                break;
            }
        }
        if (Objects.isNull(browserType)) {
            logger.error("Log Type = \"" + HuYaUtils.BROWSER_TYPE + "\" is not supported now");
            return null;
        }
        switch (browserType) {
            case CHROME:
                webDriver = getChromeDriver();break;
            case FIRE_FOX:
                webDriver = getFireFoxDriver();break;
            case EVENT_FIRING:
                webDriver = getEventFiringDriver();break;
            case EDGE:
                webDriver = getEdgeDriver();break;
            case INTERNET_EXPLORER:
                webDriver = getInternetExplorerDriver();break;
            case OPERA:
                webDriver = getOperaDriver();break;
            case SAFARI:
                webDriver = getSafariDriver();break;
            case REMOTE_WEB:
                webDriver = getRemoteDriver();break;
            default:
                logger.error("Log Type = \"" + browserType + "\" is not supported now");
        }
        if (webDriver != null) {
            webDriver.manage().window().maximize();
        }
        return webDriver;
    }

    private static WebDriver getChromeDriver() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox");
        // 是否开启GUI
        if (!SYSTEM_IS_OPEN_GUI) {
            // 设置使用headless模式(不需要开启GUI)
            options.addArguments("--headless");
        }
        // 设置chrome.exe路径
        options.setBinary(BROWSER_BINARY_LOCATION);
        // 设置不显示图片
        Map<String, Object> prefs = new HashMap<>(2);
        prefs.put("profile.managed_default_content_settings.images", 2);
        options.addArguments("--disable-images");
        options.setExperimentalOption("prefs", prefs);
        return new ChromeDriver(options);
    }

    private static WebDriver getRemoteDriver() {
        System.out.println("[getRemoteDriver]");
        return null;
    }

    private static WebDriver getSafariDriver() {
        System.out.println("[getSafariDriver]");
        return null;
    }

    private static WebDriver getOperaDriver() {
        System.out.println("[getOperaDriver]");
        return null;
    }

    private static WebDriver getInternetExplorerDriver() {
        System.out.println("[getInternetExplorerDriver]");
        return null;
    }

    private static WebDriver getEdgeDriver() {
        System.out.println("[getEdgeDriver]");
        return null;
    }

    private static WebDriver getEventFiringDriver() {
        System.out.println("[getEventFiringDriver]");
        return null;
    }

    private static WebDriver getFireFoxDriver() {
        FirefoxOptions options = new FirefoxOptions();
        options.addArguments("--no-sandbox");
        // 是否开启GUI
        if (!SYSTEM_IS_OPEN_GUI) {
            // 设置使用headless模式(不需要开启GUI)
            options.addArguments("--headless");
        }
        // 设置firefox.exe路径
        options.setBinary(BROWSER_BINARY_LOCATION);
        // 设置不显示图片
        options.addArguments("--disable-images");
        options.addPreference("permissions.default.image", 2);
        options.addPreference("dom.ipc.plugins.enabled.libflashplayer.so", "true");
        options.addPreference("plugin.state.flash", 2);
        return new FirefoxDriver(options);
    }


}
