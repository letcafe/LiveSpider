package com.letcafe.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth/")
public class AuthorizationController {

    @RequestMapping("login")
    public String indexPages(Model model){
        return "pages/examples/login";
    }

    @RequestMapping("logout")
    public String randomPage(Model model,
                             @PathVariable("functionPages") String functionPages,
                             @PathVariable("subPages") String subPages){
        return "pages/" + functionPages + "/" + subPages;
    }
}
