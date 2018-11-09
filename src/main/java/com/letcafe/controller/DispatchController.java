package com.letcafe.controller;

import com.letcafe.bean.JDProduct;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/")
public class DispatchController {

    private final Logger logger = LoggerFactory.getLogger(DispatchController.class);

    @RequestMapping("jdProduct")
    public String jdProduct(Model model) throws Exception {

        //初始化一个httpclient
        HttpClient client = HttpClients.createDefault();
        //我们要爬取的一个地址，这里可以从数据库中抽取数据，然后利用循环，可以爬取一个URL队列
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://www.huya.com/g";
        ResponseEntity<String> response =  restTemplate.getForEntity(url, String.class);
        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            //抓取的数据
            //获取的数据，存放在集合中
            List<JDProduct> bookList = new ArrayList<>();
            //采用Jsoup解析
            Document doc = Jsoup.parse(response.getBody());
            //获取html标签中的内容
            Elements elements = doc.select("ul[class=gl-warp clearfix]").select("li[class=gl-item]");
            for (Element ele : elements) {
                String bookID = ele.attr("data-sku");
                String bookPrice = ele.select("div[class=p-price]").select("strong").select("i").text();
                String bookName = ele.select("div[class=p-name p-name-type-2]").select("em").text();
                //创建一个对象，这里可以看出，使用Model的优势，直接进行封装
                JDProduct JDProduct = new JDProduct();
                //对象的值
                JDProduct.setProductId(bookID);
                JDProduct.setProductName(bookName);
                JDProduct.setProductPrice(Double.parseDouble(bookPrice));
                //将每一个对象的值，保存到List集合中
                bookList.add(JDProduct);
            }
            //循环输出抓取的数据
            logger.info("[Book List] size() = " + bookList.size());
            //将抓取的数据插入数据库
            model.addAttribute("bookList", bookList);
        }
        return "jdProduct";
    }

    @RequestMapping("api")
    public String api(Model model){
        return "api/index";
    }

    @RequestMapping("tables/{var}")
    public String tables(Model model, @PathVariable("var") String var){
        return "tables/" + var;
    }

    @RequestMapping("{indexPages}")
    public String randomPage1(@PathVariable("indexPages") String indexPages){
        return indexPages;
    }

    @RequestMapping("pages/{subPages}")
    public String randomPage2(Model model,
                             @PathVariable("subPages") String subPages){
        return "pages/" + subPages;
    }

    @RequestMapping("pages/{functionPages}/{subPages}")
    public String randomPage3(Model model,
                             @PathVariable("functionPages") String functionPages,
                             @PathVariable("subPages") String subPages){
        return "pages/" + functionPages + "/" + subPages;
    }

    @RequestMapping("pages/{functionPages}/{subPages}/{subSubPages}")
    public String randomPage4(Model model,
                              @PathVariable("functionPages") String functionPages,
                              @PathVariable("subPages") String subPages,
                              @PathVariable("subSubPages") String subSubPages){
        return "pages/" + functionPages + "/" + subPages + "/" + subSubPages;
    }
}
