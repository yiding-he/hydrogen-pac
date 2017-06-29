package com.hyd.hydrogenpac.services;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;

/**
 * @author yiding.he
 */
@Component
public class UserService extends AbstractService {

    public static final String SESSION_KEY = "user";

    public static final String REDIS_HASH_KEY = "hydrogen-pac-users";

    @PostConstruct
    private void init() {
        getRedis().opsForHash().put(REDIS_HASH_KEY, "admin", "admin123");
    }

    public boolean isUserLoggedIn(HttpSession session) {
        return session.getAttribute(SESSION_KEY) != null;
    }

    public boolean login(HttpSession session, String user, String pass) {
        String _pass = String.valueOf(getRedis().opsForHash().get(REDIS_HASH_KEY, user));
        boolean passMatch = pass.equals(_pass);

        if (passMatch) {
            session.setAttribute(SESSION_KEY, user);
        }

        return passMatch;
    }

    public String getUserName(HttpSession session) {
        return String.valueOf(session.getAttribute(SESSION_KEY));
    }
}
