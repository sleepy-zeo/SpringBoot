package com.sleepy.zeo.springboot.security;

import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;

public class WAuthenticationDetails extends WebAuthenticationDetails {

    private String verifyCode;

    public WAuthenticationDetails(HttpServletRequest request) {
        super(request);
        verifyCode = request.getParameter("verifycode");
    }

    public String getVerifyCode() {
        return verifyCode;
    }
}
