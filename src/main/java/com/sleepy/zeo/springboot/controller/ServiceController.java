package com.sleepy.zeo.springboot.controller;

import com.sleepy.zeo.springboot.service.HumanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/sv")
public class ServiceController {

    private HumanService humanService;

    @Autowired
    @Qualifier("doctorService")
    public void setHumanService(HumanService humanService) {
        this.humanService = humanService;
    }

    @RequestMapping("")
    @ResponseBody
    public String human() {
        return humanService.name();
    }
}
