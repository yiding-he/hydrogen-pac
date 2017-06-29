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

    private static final String USERS_HASH_KEY = "hydrogen-pac-users";

    @PostConstruct
    private void init() {
        redis.getHash(USERS_HASH_KEY).put("admin", "admin123");
    }

    public boolean isUserLoggedIn(HttpSession session) {
        return session.getAttribute(SESSION_KEY) != null;
    }

    public boolean login(HttpSession session, String user, String pass) {
        String _pass = String.valueOf(redis.getHash(USERS_HASH_KEY).get(user));

        if (pass.equals(_pass)) {
            session.setAttribute(SESSION_KEY, user);
            return true;
        } else {
            return false;
        }
    }

    public String getUserName(HttpSession session) {
        return String.valueOf(session.getAttribute(SESSION_KEY));
    }
}
