package com.letcafe.controller.huya;

import com.letcafe.bean.HuYaGameType;
import com.letcafe.parse.HuYaParser;
import com.letcafe.service.HuYaGameTypeService;
import com.letcafe.util.UrlFetcher;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/getter/huya/gameType")
public class GameTypeGetter {

    private final Logger logger = LoggerFactory.getLogger(GameTypeGetter.class);
    private static int logShowInt = 0;

    private HuYaGameTypeService huYaGameTypeService;

    @Autowired
    public GameTypeGetter(HuYaGameTypeService huYaGameTypeService) {
        this.huYaGameTypeService = huYaGameTypeService;
    }

    @Scheduled(cron = "25/30 * * * * *")
    public void gameTypeScheduled() throws Exception {
        HttpClient client = HttpClients.createDefault();
        String url="https://www.huya.com/g";
        List<HuYaGameType> types = UrlFetcher.URLParser(client, url, new HuYaParser());
        huYaGameTypeService.saveOrUpdateList(types);
        // every 20 count = 1 hour,put down one log
        if (++ logShowInt == 20) {
            logger.info("game type updated, fetch total = " + types.size());
            logShowInt = 0;
        }
    }

}
