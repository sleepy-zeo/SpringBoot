package com.sleepy.zeo.springboot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/cookie")
public class CookieController {


    @RequestMapping("/test")
    @ResponseBody
    public String test(@RequestParam("browser") String browser,
                         HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        Object sessionBrowser = session.getAttribute("browser");
        if (sessionBrowser == null) {
            System.out.println("session not exists, set browser=" + browser);
            session.setAttribute("browser", browser);
        } else {
            System.out.println("session exists, browser=" + sessionBrowser.toString());
        }
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                System.out.println(cookie.getName() + ": " + cookie.getValue());
            }
        }
        return "success";
    }
}
