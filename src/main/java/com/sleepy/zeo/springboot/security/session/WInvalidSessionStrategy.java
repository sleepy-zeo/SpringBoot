package com.sleepy.zeo.springboot.security.session;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class WInvalidSessionStrategy implements InvalidSessionStrategy {
    private static final Log logger = LogFactory.getLog(WInvalidSessionStrategy.class);

    @Override
    public void onInvalidSessionDetected(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException, ServletException {

        // clear old session
        httpServletRequest.getSession(true);
        logger.info("session timeout, please login again");

        httpServletResponse.sendRedirect("/login");
    }
}
