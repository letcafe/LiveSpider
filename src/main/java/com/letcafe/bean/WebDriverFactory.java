package com.letcafe.bean;

import com.letcafe.exception.BrowserTypeUnsupportedException;
import com.letcafe.util.HuYaUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.letcafe.util.HuYaUtils.BROWSER_BINARY_LOCATION;
import static com.letcafe.util.HuYaUtils.BROWSER_DRIVER_LOCATION;
import static com.letcafe.util.HuYaUtils.SYSTEM_IS_OPEN_GUI;

public class WebDriverFactory {

    private static final Logger logger = LoggerFactory.getLogger(WebDriverFactory.class);

    /**
     * 如果遍历了所有的浏览器类型，都没有和huya.yaml里的BROWSER_DRIVER_LOCATION匹配，
     * 则表明不支持改浏览器类型或者yaml里填写错误
     */
    static {
        // 找到第一个匹配驱动类型的，设置驱动的环境变量
        System.setProperty(HuYaUtils.BROWSER_TYPE.systemPropertyName, BROWSER_DRIVER_LOCATION);
    }

    /**
     * 获取对应的WebDriver
     * @return 获取对应的WebDriver(目前获得的是非单例的，即每次new)
     */
    public static WebDriver getInstance() {
        WebDriver webDriver = getDriverByType(HuYaUtils.BROWSER_TYPE);
        webDriver.manage().window().maximize();
        return webDriver;
    }

    @NotNull
    private static WebDriver getDriverByType(BrowserType type) {
        // 根据YAML初始化WebDriver的环境变量
        // 根据类型进行实例化
        switch (type) {
            case CHROME:
                return getChromeDriver();
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
                throw new BrowserTypeUnsupportedException(HuYaUtils.BROWSER_TYPE + " is not in supported browser list("
                        + Arrays.toString(BrowserType.values()) + ")");
        }
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
