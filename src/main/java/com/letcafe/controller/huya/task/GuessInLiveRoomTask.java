package com.letcafe.controller.huya.task;

import com.letcafe.bean.HuYaLiveInfo;
import com.letcafe.bean.HuYaProperties;
import com.letcafe.generator.GameIdGen;
import com.letcafe.generator.HuYaLiveInfoGen;
import com.letcafe.service.WebDriverService;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Controller
public class GuessInLiveRoomTask {

    private static final Logger logger = LoggerFactory.getLogger(GuessInLiveRoomTask.class);

    private WebDriverService webDriverService;
    private HuYaProperties huYaProperties;

    @Autowired
    public GuessInLiveRoomTask(WebDriverService webDriverService, HuYaProperties huYaProperties) {
        this.webDriverService = webDriverService;
        this.huYaProperties = huYaProperties;
    }

    /**
     * 完成每日三次竞猜任务
     */
    @Scheduled(cron = "${huya.task.worker.time.guessInLiveRoom}")
    public void guessInLiveRoom() {
        WebDriver webDriver = webDriverService.getWebDriverWithCookie(huYaProperties.getYyId());
        if (webDriver == null) {
            logger.error("[System] WebDriver is null");
            return;
        }
        try {
            // 设置窗体分辨率，使得多个竞猜中的按钮可以被点击出来
            webDriver.manage().window().setSize(new Dimension(1960, 1080));
            // 每个直播间竞猜大于等于1，但是有的直播间可能存在刚开盘或者一边倒的情况导致无法两边投票，因此先预留额外几个用于下注
            List<HuYaLiveInfo> guessRoomList = getGuessRoomList(webDriver, 8);
            for (HuYaLiveInfo huYaLiveInfo : guessRoomList) {
                String watchUrl = "https://www.huya.com/" + huYaLiveInfo.getProfileRoom();
                logger.info("[Guess Room Url] = " + watchUrl);
            }
        } catch (TimeoutException ex1) {
            logger.error("[System: ERROR] finish three guess one day failed, reason = " + ex1.getMessage());
        } catch (Exception ex2) {
            logger.error("[System: ERROR] " + ex2.getMessage());
            ex2.printStackTrace();
        } finally {
            webDriver.quit();
            logger.info("[System] WebDriver quit");
        }
    }


    private List<HuYaLiveInfo> getGuessRoomList(WebDriver webDriver, int number) throws InterruptedException {
        List<HuYaLiveInfo> numberedRoomList = new ArrayList<>();
        int count = 0;
        GameIdGen gameIdGen = new GameIdGen();
        HuYaLiveInfoGen liveInfoGen = new HuYaLiveInfoGen(gameIdGen);
        while (count < number) {
            HuYaLiveInfo liveInfo = liveInfoGen.next();
            String watchUrl = "https://www.huya.com/" + liveInfo.getProfileRoom();
            boolean hasGuessInRoom = hasGuessInLiveRoom(webDriver, watchUrl);
            if (hasGuessInRoom) {
                // 如果存在竞猜，进行竞猜，策略：两边都投入等量银豆
                numberedRoomList.add(liveInfo);
                count++;
                logger.info(watchUrl + " has guess event");
                guess2SideInOneLiveRoom(webDriver, watchUrl);
            }
        }
        return numberedRoomList;
    }

    private int guess2SideInOneLiveRoom(WebDriver webDriver, String url) {
        // 先设置player-box属性display:none -> display:block，使得其先课件（为后面点击宝箱领取做铺垫）
        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        WebDriverWait waitForThisPage = new WebDriverWait(webDriver, 20, 500);

        int successGuessCount = 0;
        waitForThisPage.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("guess-main-box")));
        List<WebElement> guessMainBoxes = webDriver.findElements(By.className("guess-main-box"));

        // 点击用户的“玩竞猜”按钮，使得可以获取到每个投注按钮的文本
        waitForThisPage.until(ExpectedConditions.elementToBeClickable(By.className("guess-icon")));
        WebElement guessNavBarButton = webDriver.findElement(By.className("guess-icon"));
        guessNavBarButton.click();

        int noSuccessGuessCount = 0;
        for (WebElement guessMainBox : guessMainBoxes) {
            waitForThisPage.until(ExpectedConditions.elementToBeClickable(By.className("guess-btn")));
            List<WebElement> guess2SideButtons = guessMainBox.findElements(By.className("guess-btn"));

            if (guess2SideButtons.size() != 2) {
                logger.warn("Guess in " + url + ", there has one side for guess button");
            } else {
                // 使用JS获取投注按钮内的文本（也可以在这里使用循环减半胆码，衡量只有两个按钮，为了可读性未使用循环）
                String leftGuessBtnInnerHtml = js.executeScript("return arguments[0].innerHTML", guess2SideButtons.get(0)).toString();
                String rightGuessBtnInnerHtml = js.executeScript("return arguments[0].innerHTML", guess2SideButtons.get(1)).toString();
                // 如果两边的按钮都是允许竞猜直接投注的，那么两边都投注可以保证任务完成
                if (leftGuessBtnInnerHtml.matches("种\\d返.*") && rightGuessBtnInnerHtml.matches("种\\d返.*")) {
                    logger.info("[" + leftGuessBtnInnerHtml + " VS " + rightGuessBtnInnerHtml + "] is a win success");
                    // 防止检测的时候有竞猜，在下一时刻就没有的情况
                    try {
                        // i = 0是点击左边的投下注票按钮， i = 1是点击右边的投票下注按钮
                        for (int i = 0; i < 2; i++) {
                            WebElement sendGuessReqBtn = webDriver.findElement(By.cssSelector(".guess-plan button"));
                            js.executeScript("arguments[0].click()", guess2SideButtons.get(i));
                            WebElement guessPlan = webDriver.findElement(By.cssSelector(".guess-plan input"));
                            // 下注豆子保护，防止原来已有豆子导致String不断append
                            if (!"".equals(guessPlan.getAttribute("value"))) {
                                logger.error("[Doing Guess] fail, because the guess number is not empty before send");
                                break;
                            }
                            // 每次只投资一个豆子（为了完成任务）
                            guessPlan.sendKeys("" + 1);
                            TimeUnit.SECONDS.sleep(2);
                            logger.info("[Doing Guess: Before sendBtn Click] : " + guessPlan.getAttribute("value"));
                            js.executeScript("arguments[0].click()", sendGuessReqBtn);
                            logger.info("[Doing Guess: After sendBtn click] : " + guessPlan.getAttribute("value"));
                            TimeUnit.SECONDS.sleep(2);
                        }
                    } catch (Exception ex) {
                        logger.error("[Error] the reason is {}", ex.toString());
                    }
                    successGuessCount++;
                } else {
                    noSuccessGuessCount++;
                }
            }
            if (noSuccessGuessCount == guessMainBoxes.size()) {
                logger.warn("But this guess in [" + url + "] has no success guess");
            }
        }
        return successGuessCount;
    }

    private boolean hasGuessInLiveRoom(WebDriver webDriver, String url) throws InterruptedException {
        webDriver.get(url);
        TimeUnit.SECONDS.sleep(2);
        WebElement guessBox = webDriver.findElement(By.cssSelector("#player-gift-wrap .guess-box"));
        return guessBox.isDisplayed();
    }

    /**
     * 单参数重载
     *
     * @param url 竞猜的地址
     * @return 是否成功
     * @throws InterruptedException sleep中断异常
     */
    private boolean hasGuessInLiveRoom(String url) throws InterruptedException {
        WebDriver webDriver = webDriverService.getWebDriverWithCookie(huYaProperties.getYyId());
        if (webDriver == null) {
            logger.error("[System] WebDriver is null");
            return false;
        }
        return hasGuessInLiveRoom(webDriver, url);
    }
}
