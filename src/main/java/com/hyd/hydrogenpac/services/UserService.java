package com.hyd.hydrogenpac.services;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;

/**
 * @author yiding.he
 */
@Component
public class UserService extends AbstractService {

    private static final String SESSION_KEY = "user";

    @PostConstruct
    private void init() {
    }

    public boolean isUserLoggedIn(HttpSession session) {
        return session.getAttribute(SESSION_KEY) != null;
    }

    public boolean login(HttpSession session, String user, String pass) {
        session.setAttribute(SESSION_KEY, user);
        return true;
    }

    public String getUserName(HttpSession session) {
        return String.valueOf(session.getAttribute(SESSION_KEY));
    }
}
