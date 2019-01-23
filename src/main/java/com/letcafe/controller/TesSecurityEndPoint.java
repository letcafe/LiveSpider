package com.letcafe.controller;

import org.springframework.web.bind.annotation.*;

@RestController
public class TesSecurityEndPoint {

    @RequestMapping(value = "/product/{id}", method= RequestMethod.GET)
    public String getProduct(@PathVariable  String id) {
        return "product id : " + id;
    }

    @GetMapping("/order/{id}")
    public String getOrder(@PathVariable String id) {
        return "order id : " + id;
    }
}
