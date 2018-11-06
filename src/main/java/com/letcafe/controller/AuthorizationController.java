package com.letcafe.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/pages/auth/")
public class AuthorizationController {

    @RequestMapping(value = "login",method = RequestMethod.GET)
    public String indexPages(Model model){
        return "pages/examples/login";
    }

    @RequestMapping(value = "login",method = RequestMethod.POST)
    public String loginAction(Model model){
        return "pages/examples/login";
    }

    @RequestMapping("logout")
    public String randomPage(Model model,
                             @PathVariable("functionPages") String functionPages,
                             @PathVariable("subPages") String subPages){
        return "pages/" + functionPages + "/" + subPages;
    }
}
