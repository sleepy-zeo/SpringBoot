package com.sleepy.zeo.springboot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/hello")
public class HelloController {

    // "/hello"被HelloServlet处理了，不会传递到这里
    @RequestMapping("")
    @ResponseBody
    public String index() {
        return "hello";
    }

    @RequestMapping("/info")
    @ResponseBody
    public String info() {
        return "info";
    }
}
