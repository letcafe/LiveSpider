package com.letcafe.controller.huya;


import com.letcafe.bean.HuYaLiveInfo;
import com.letcafe.service.WebDriverService;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import java.io.IOException;
import java.util.List;
import static com.letcafe.util.HuYaUtils.YY_ID;
@Controller
public class TaskAutoWorker {

    private static final Logger logger = LoggerFactory.getLogger(TaskAutoWorker.class);

    private WebDriverService webDriverService;

    @Autowired
    public TaskAutoWorker(WebDriverService webDriverService) {
        this.webDriverService = webDriverService;
    }

    // do watch live work:watch 10 live one day
    @Scheduled(cron = "0 0 7 * * *")
    public void watchNumberedLive() throws IOException {
        WebDriver webDriver = webDriverService.getWebDriverWithCookie(YY_ID);
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

    // do watch live work:watch 10 live one day
    @Scheduled(cron = "0 0 3 * * *")
    public void watchLiveGetSixTreasure() throws IOException {
        WebDriver webDriver = webDriverService.getWebDriverWithCookie(YY_ID);
        if (webDriver == null) {
            logger.error("web driver is null");
            return;
        }
        LiveInfoGetter liveInfoGetter = new LiveInfoGetter();
        // take LOL for example, get LOL live list, if come across exception,log then recursive
        List<HuYaLiveInfo> liveInfoList = liveInfoGetter.listHuYaLiveList(1);
        String watchUrl = "https://www.huya.com/" + liveInfoList.get(0).getProfileRoom();
        try {
            webDriver.get(watchUrl);
            Thread.sleep(60 * 60 * 1000);
            logger.info("[Six Treasure:watch 60 minutes to get 6 treasure] watch url = " + watchUrl);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            webDriver.quit();
        }
        logger.info("webdirver quit");
    }
}
