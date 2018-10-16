package com.letcafe.controller.huya;

import com.letcafe.bean.HuYaGameType;
import com.letcafe.service.HuYaGameTypeService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/getter/huya/gameType")
public class GameTypeGetter {

    private final Logger logger = LoggerFactory.getLogger(GameTypeGetter.class);

    private HuYaGameTypeService huYaGameTypeService;

    @Autowired
    public GameTypeGetter(HuYaGameTypeService huYaGameTypeService) {
        this.huYaGameTypeService = huYaGameTypeService;
    }

    @Scheduled(cron = "0 0/10 * * * *")
    public void gameTypeScheduled() throws Exception {
        List<HuYaGameType> types = new ArrayList<>();
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://www.huya.com/g";
        ResponseEntity<String> response =  restTemplate.getForEntity(url, String.class);
        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            // 使用JSoup抓取元素并存入数据库
            Document document = Jsoup.parse(response.getBody());
            Elements elements = document.select("div[class=box-bd]").select(".game-list").select("li[class=game-list-item]");
            for (Element element : elements) {
                String gid = element.attr("gid");
                String title = element.select(".title").text();
                HuYaGameType huYaGameType = new HuYaGameType(gid, title);
                types.add(huYaGameType);
            }
            huYaGameTypeService.saveOrUpdateList(types);
            logger.info("[Game Type : Update] fetch number = {}", types.size());
        } else {
            logger.warn(response.getStatusCode().getReasonPhrase());
        }
    }

}
