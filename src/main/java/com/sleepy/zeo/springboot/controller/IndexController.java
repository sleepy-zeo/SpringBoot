package com.sleepy.zeo.springboot.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {

    private Log logger = LogFactory.getLog(IndexController.class);

    @RequestMapping("")
    public String index() {
        return "index";
    }

    @RequestMapping("/admin")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String printAdmin() {
        return "You have role ROLE_ADMIN";
    }

    @RequestMapping("/user")
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_USER')")
    public String printUser() {
        return "You have role ROLE_USER";
    }

    @RequestMapping("/welcome")
    @ResponseBody
    public String welcome() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("Current user: " + username);
        return "Welcome " + username;
    }
}
