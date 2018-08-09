package com.letcafe.controller;

import com.letcafe.bean.JDProduct;
import com.letcafe.util.UrlFetcher;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/")
public class DispatchController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping("index")
    public String index(Model model) throws Exception {

        //初始化一个httpclient
        HttpClient client = HttpClients.createDefault();
        //我们要爬取的一个地址，这里可以从数据库中抽取数据，然后利用循环，可以爬取一个URL队列
        String url="http://search.jd.com/Search?keyword=%E7%AC%94%E8%AE%B0%E6%9C%AC%E7%94%B5%E8%84%91&enc=utf-8&suggest=1.def.0.V12&wq=%E7%AC%94%E8%AE%B0%E6%9C%AC&pvid=546f97fc1c954bb596661c10f5cc2122";
        //抓取的数据
        List<JDProduct> bookList= UrlFetcher.URLParser(client, url);
        //循环输出抓取的数据
        for (JDProduct jd : bookList) {
            logger.info("bookID:"+jd.getProductId()+"\t"+"bookPrice:"+ jd.getProductPrice() +"\t"+"bookName:"+jd.getProductName());
        }
        //将抓取的数据插入数据库
        model.addAttribute("bookList", bookList);
        return "index";
    }

    @RequestMapping("tables/{var}")
    public String tables(Model model, @PathVariable("var") String var){
        return "tables/" + var;
    }
}
