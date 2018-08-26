package com.letcafe.controller.huya;


import com.letcafe.bean.HuYaLiveInfo;
import com.letcafe.service.HuYaLiveInfoService;
import com.letcafe.service.HuYaUserLevelService;
import com.letcafe.util.HuYaUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.List;

import static com.letcafe.util.HuYaUtils.getActiveHuYaLoginWebDriver;

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
    @Scheduled(cron = "0 0 6 * * *")
    public void watchNumberedLive() throws IOException, InterruptedException {
        WebDriver webDriver = getActiveHuYaLoginWebDriver(false, false);
        if (webDriver == null) {
            return;
        }

        LiveInfoGetter liveInfoGetter = new LiveInfoGetter();
        // take LOL for example, get LOL live list
        List<HuYaLiveInfo> liveInfoList = liveInfoGetter.listHuYaLiveList(74);

        // use 2 more (10 < 10 + 2) for some accident url
        for (int i = 0; i < 12; i ++) {
            String watchUrl = "https://www.huya.com/" + liveInfoList.get(i).getProfileRoom();
            webDriver.get(watchUrl);

            WebDriverWait switchToPersonalPageWait = new WebDriverWait(webDriver, 3, 500);
            switchToPersonalPageWait.until(ExpectedConditions.urlToBe(watchUrl));

            logger.info("[Task:watch 10 live one day] watch url = " + watchUrl);
        }
        logger.info("[Task:watch 10 live one day] ended");
    }
}
