package com.letcafe.controller.huya;


import com.letcafe.bean.HuYaLiveInfo;
import com.letcafe.service.HuYaLiveInfoService;
import com.letcafe.service.HuYaUserLevelService;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.List;



@Controller
public class TaskAutoWorker {

    private static final Logger logger = LoggerFactory.getLogger(TaskAutoWorker.class);

    private HuYaUserLevelService huYaUserLevelService;
    private HuYaLiveInfoService huYaLiveInfoService;

    @Autowired
    public TaskAutoWorker(HuYaUserLevelService huYaUserLevelService, HuYaLiveInfoService huYaLiveInfoService) {
        this.huYaUserLevelService = huYaUserLevelService;
        this.huYaLiveInfoService = huYaLiveInfoService;
    }

    // do watch live work:watch 10 live one day
    @Scheduled(fixedRate = 5 * 60 * 1000)
//    @Scheduled(cron = "0 0 * * * *")
    public void watchNumberedLive() throws IOException {
        WebDriver webDriver = huYaUserLevelService.getActiveHuYaLoginWebDriver(false, false);
        if (webDriver == null) {
            logger.error("web driver is null");
            return;
        }
        LiveInfoGetter liveInfoGetter = new LiveInfoGetter();
        // take LOL for example, get LOL live list, if come across exception,log then recursive
        List<HuYaLiveInfo> liveInfoList = liveInfoGetter.listHuYaLiveList(1);
        try {
            // use 2 more (10 < 10 + 2) for some accident url
            for (int i = 0; i < 12; i ++) {
                String watchUrl = "https://www.huya.com/" + liveInfoList.get(i).getProfileRoom();
                webDriver.get(watchUrl);
                Thread.sleep(5000);
                logger.info("[Task:watch 10 live one day] No." + (i + 1) + " watch url = " + watchUrl);
            }
            logger.info("[Task:watch 10 live one day] ended");
        } catch (Exception ex) {
            logger.error("watch 10 failed");
            ex.printStackTrace(System.out);
        } finally {
            webDriver.quit();
            logger.info("webdirver quit");
        }
    }
}
