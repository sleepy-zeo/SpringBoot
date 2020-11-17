package com.sleepy.zeo.springboot.security.session;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import java.io.IOException;

@Component
public class WSessionInformationExpiredStrategy implements SessionInformationExpiredStrategy {
    private static final Log logger = LogFactory.getLog(WSessionInformationExpiredStrategy.class);

    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent sessionInformationExpiredEvent) throws IOException, ServletException {
        logger.warn("session expired, " + sessionInformationExpiredEvent.getSessionInformation().getPrincipal());

        // clear old session
        sessionInformationExpiredEvent.getRequest().getSession(true);

        sessionInformationExpiredEvent.getResponse().setContentType("text/html");
        sessionInformationExpiredEvent.getResponse().getWriter().write("Another account login, you are forced to logout");
    }
}
