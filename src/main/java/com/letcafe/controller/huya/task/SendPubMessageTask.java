package com.letcafe.controller.huya.task;

import com.letcafe.bean.HuYaLiveInfo;
import com.letcafe.bean.HuYaProperties;
import com.letcafe.controller.huya.LiveInfoGetter;
import com.letcafe.service.WebDriverService;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Controller
public class SendPubMessageTask {

    private static final Logger logger = LoggerFactory.getLogger(SendPubMessageTask.class);

    private WebDriverService webDriverService;
    private HuYaProperties huYaProperties;

    @Autowired
    public SendPubMessageTask(WebDriverService webDriverService, HuYaProperties huYaProperties) {
        this.webDriverService = webDriverService;
        this.huYaProperties = huYaProperties;
    }

    /**
     * 完成弹幕发送任务
     *
     * @throws InterruptedException sleep中断异常
     */
    @Scheduled(cron = "${huya.task.worker.time.sendPubMessage}")
    public void sendPubMessage() throws InterruptedException {
        WebDriver webDriver = webDriverService.getWebDriverWithCookie(huYaProperties.getYyId());
        try {
            WebDriverWait loginFrameWait = new WebDriverWait(webDriver, 20, 500);
            LiveInfoGetter liveInfoGetter = new LiveInfoGetter();
            // take LOL for example, get LOL live list, if come across exception,log then recursive
            List<HuYaLiveInfo> liveInfoList = liveInfoGetter.listHuYaLiveByGid(1);
            String watchUrl = "https://www.huya.com/" + liveInfoList.get(0).getProfileRoom();
            String message = "6666";
            webDriver.get(watchUrl);
            TimeUnit.SECONDS.sleep(3);
            WebElement chatInput = webDriver.findElement(By.id("pub_msg_input"));
            JavascriptExecutor js = (JavascriptExecutor) webDriver;
            // 添加Enable的class使得发送按钮可以直接点击
            js.executeScript("document.getElementById('msg_send_bt').className += 'enable';");
            chatInput.sendKeys(message);
            WebElement chatSendButton = webDriver.findElement(By.id("msg_send_bt"));
            loginFrameWait.until(ExpectedConditions.elementToBeClickable(chatSendButton));
            chatSendButton.click();
            TimeUnit.SECONDS.sleep(1);
            logger.info("[send message to live(" + watchUrl + ")] = " + message);
        } finally {
            webDriver.quit();
            logger.info("[System] WebDriver quit");
        }
    }
}
