package com.sleepy.zeo.springboot.security.session;

import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import java.io.IOException;

@Component
public class WSessionInformationExpiredStrategy implements SessionInformationExpiredStrategy {

    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent sessionInformationExpiredEvent) throws IOException, ServletException {
        // clear old session
        sessionInformationExpiredEvent.getRequest().getSession(true);

        sessionInformationExpiredEvent.getResponse().setContentType("text/html");
        sessionInformationExpiredEvent.getResponse().getWriter().write("Another account login, you are forced to logout");
    }
}
