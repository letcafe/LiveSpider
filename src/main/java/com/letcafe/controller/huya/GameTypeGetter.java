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

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private HuYaGameTypeService huYaGameTypeService;

    @Autowired
    public GameTypeGetter(HuYaGameTypeService huYaGameTypeService) {
        this.huYaGameTypeService = huYaGameTypeService;
    }

    @RequestMapping("")
    public String gameType(Model model) throws Exception {
        //初始化一个httpclient
        HttpClient client = HttpClients.createDefault();
        //我们要爬取的一个地址，这里可以从数据库中抽取数据，然后利用循环，可以爬取一个URL队列
        String url="https://www.huya.com/g";
        //抓取的数据
        List<HuYaGameType> types = UrlFetcher.URLParser(client, url, new HuYaParser());
        //循环输出抓取的数据
        logger.info("types.size() = " + types.size());
        huYaGameTypeService.saveOrUpdateList(types);
        //将抓取的数据插入数据库
        if (model != null) {
            model.addAttribute("types", types);
        }
        return "huyaLiveType";
    }

    @Scheduled(fixedRate = 10000)
    public void gameTypeScheduled() throws Exception {
        gameType(null);
    }

}
