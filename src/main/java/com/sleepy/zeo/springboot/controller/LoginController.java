package com.sleepy.zeo.springboot.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/login")
public class LoginController {

    private static final Log logger = LogFactory.getLog(LoginController.class);

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String login() {
        return "login";
    }

    @RequestMapping(value = "/error", method = RequestMethod.GET)
    public void error(HttpServletRequest request, HttpServletResponse response) {
        AuthenticationException exception = (AuthenticationException) request.getSession().getAttribute("SPRING_SECURITY_LAST_EXCEPTION");
        try {
            response.getWriter().write(exception.toString());
        } catch (Throwable e) {
            logger.error(e);
            try {
                response.sendRedirect("/welcome");
            } catch (IOException e1) {
                logger.error(e1);
            }
        }
    }
}
