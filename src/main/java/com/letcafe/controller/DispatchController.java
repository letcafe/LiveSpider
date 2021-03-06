package com.letcafe.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class DispatchController {

    private final Logger logger = LoggerFactory.getLogger(DispatchController.class);

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
