package com.example.dccworkflow.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

    @GetMapping("/login")
    public String login() {
        return "security/login";
    }

    @RequestMapping("/")
    public String index() {
        return "index/index";
    }
}
