package com.letcafe.parse;

import com.letcafe.bean.HuYaGameType;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class HuYaParser implements Parser {

    @Override
    public List<?> listData(String html) {
        List<HuYaGameType> data = new ArrayList<>();
        Document document = Jsoup.parse(html);
        Elements elements = document.select("div[class=box-bd]").select(".game-list").select("li[class=game-list-item]");
        for(Element element : elements) {
            String gid = element.attr("gid");
            String title = element.select(".title").text();
            HuYaGameType huYaGameType = new HuYaGameType(gid, title);
            data.add(huYaGameType);
        }
        return data;
    }
}
