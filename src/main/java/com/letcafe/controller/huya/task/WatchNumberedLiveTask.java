package com.letcafe.controller.huya.task;

import com.letcafe.bean.HuYaLiveInfo;
import com.letcafe.bean.HuYaProperties;
import com.letcafe.controller.huya.LiveInfoGetter;
import com.letcafe.service.WebDriverService;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class WatchNumberedLiveTask {

    private static final Logger logger = LoggerFactory.getLogger(WatchNumberedLiveTask.class);

    private WebDriverService webDriverService;
    private HuYaProperties huYaProperties;

    @Autowired
    public WatchNumberedLiveTask(WebDriverService webDriverService, HuYaProperties huYaProperties) {
        this.webDriverService = webDriverService;
        this.huYaProperties = huYaProperties;
    }

    /**
     * 完成每天观看10名主播任务
     */
    @Scheduled(cron = "${huya.task.worker.time.watchNumberedLive}")
    public void watchNumberedLive() {
        WebDriver webDriver = webDriverService.getWebDriverWithCookie(huYaProperties.getYyId());
        LiveInfoGetter liveInfoGetter = new LiveInfoGetter();
        // take LOL for example, get LOL live list, if come across exception,log then recursive
        List<HuYaLiveInfo> liveInfoList = liveInfoGetter.listHuYaLiveByGid(1);
        try {
            // use 2 more (10 < 10 + 2) for some accident url
            for (int i = 0; i < 12; i++) {
                String watchUrl = "https://www.huya.com/" + liveInfoList.get(i).getProfileRoom();
                webDriver.get(watchUrl);
                Thread.sleep(10);
                logger.info("[Task:watch 10 live one day] No.{} watch url = {}", i + 1, watchUrl);
            }
            logger.info("[Watch 10 Live] end");
        } catch (Exception ex) {
            logger.error("[Watch 10 Live] fail");
            ex.printStackTrace(System.out);
        } finally {
             webDriver.quit();
            logger.info("[System] WebDriver quit");
        }
    }
}
