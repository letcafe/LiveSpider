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
    public String login(){
        return "pages/examples/login";
    }

    @RequestMapping(value = "logout",method = RequestMethod.GET)
    public String logout(){
        return "pages/examples/login";
    }

}
