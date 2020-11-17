package com.sleepy.zeo.springboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/session")
public class SessionController {

    private SessionRegistry sessionRegistry;

    @Autowired
    public void setSessionRegistry(SessionRegistry sessionRegistry) {
        this.sessionRegistry = sessionRegistry;
    }

    @RequestMapping("/kick")
    @ResponseBody
    public String kickUserSessionByUsername(@RequestParam String username) {

        List<Object> users = sessionRegistry.getAllPrincipals();
        int kickCount = 0;
        for (Object principal : users) {
            if (principal instanceof User) {
                User u = (User) principal;
                String principalName = u.getUsername();
                if (principalName != null && principalName.equals(username)) {
                    List<SessionInformation> sessionInformations = sessionRegistry.getAllSessions(principal, false);
                    if (sessionInformations != null && !sessionInformations.isEmpty()) {
                        for (SessionInformation info : sessionInformations) {
                            info.expireNow();
                            kickCount++;
                        }
                    }
                }
            }
        }
        return "success, kick " + username + "\' session num: " + kickCount;
    }
}
