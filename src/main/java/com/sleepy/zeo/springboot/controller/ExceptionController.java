package com.sleepy.zeo.springboot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/ex")
public class ExceptionController {

    @RequestMapping()
    public void exception(HttpServletResponse response) throws Exception {
        int divisor = 0;
        int dividend = 100;
        int value = dividend / divisor;
        response.getWriter().write("success");
    }

    @RequestMapping("/500")
    public void exception500(HttpServletResponse response) throws IOException {
        response.setStatus(500);
        response.getWriter().write("success");
    }
}
