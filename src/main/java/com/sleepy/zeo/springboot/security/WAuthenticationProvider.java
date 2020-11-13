package com.sleepy.zeo.springboot.security;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

// 取出WebAuthenticationDetails和AuthenticationDetailsSource
@Component
public class WAuthenticationProvider implements AuthenticationProvider {
    private static final Log logger = LogFactory.getLog(WAuthenticationProvider.class);

    private DaoAuthenticationProvider defaultProvider;

    public WAuthenticationProvider() {
        defaultProvider = new DaoAuthenticationProvider();
    }

    @Autowired
    @Qualifier("userDetailsServiceImpl")
    public void setUserDetailsService(UserDetailsService userDetailsService) {
        defaultProvider.setUserDetailsService(userDetailsService);
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        defaultProvider.setPasswordEncoder(passwordEncoder);
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(UsernamePasswordAuthenticationToken.class);
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        WAuthenticationDetails authenticationDetails = (WAuthenticationDetails) authentication.getDetails();

        String verifyCode = authenticationDetails.getVerifyCode();
        if (!verifyCodeValid(verifyCode)) {
            throw new DisabledException("verifycode error");
        }

        return defaultProvider.authenticate(authentication);
    }

    @SuppressWarnings("Duplicates")
    private boolean verifyCodeValid(String inputVerifyCode) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String verifyCode = (String) request.getSession().getAttribute("verifycode");
        logger.info("inputCode: " + inputVerifyCode + ", requestCode: " + verifyCode);
        return verifyCode.toLowerCase().equals(inputVerifyCode.toLowerCase());
    }
}
