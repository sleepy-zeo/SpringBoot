package com.sleepy.zeo.springboot.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {

    private Log logger = LogFactory.getLog(IndexController.class);

    @RequestMapping("")
    public String index() {
        return "index";
    }

    @RequestMapping("/static/{dir}/**")
    public String src(@PathVariable("dir") String dir, HttpServletRequest request) {
        // 获取请求的全路径: /static/drawable/background.png
        String path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        logger.info("path: " + path);

        // 获取匹配到controller的路径: /static/{dir}/**
        String bestMatchPattern = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        logger.info("bestMatchPattern: " + bestMatchPattern);

        // 获取dir的值: drawable
        logger.info("suffix: " + dir);

        return "forward:/" + path.substring(8);
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
