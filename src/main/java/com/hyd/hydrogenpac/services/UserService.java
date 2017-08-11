package com.hyd.hydrogenpac.services;

import com.hyd.hydrogenpac.beans.User;
import org.dizitart.no2.Document;
import org.dizitart.no2.NitriteCollection;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;

import static org.dizitart.no2.filters.Filters.and;
import static org.dizitart.no2.filters.Filters.eq;

/**
 * @author yiding.he
 */
@Component
public class UserService extends AbstractService {

    @Value("${server.session.timeout}")
    private int sessionExpireSeconds;

    private static final String SESSION_KEY = "user";

    public boolean isUserLoggedIn(HttpSession session) {
        return session.getAttribute(SESSION_KEY) != null;
    }

    public User getUser(HttpSession session) {
        return (User) session.getAttribute(SESSION_KEY);
    }

    public void onUserLoggedIn(User user, HttpSession session) {
        session.setAttribute(SESSION_KEY, user);

        if (!userExists(user)) {
            long tokenExpire = System.currentTimeMillis() / 1000 + sessionExpireSeconds;

            getUserCollection().insert(new Document()
                    .put("type", user.getoAuthServiceType().name())
                    .put("userId", user.getUserId())
                    .put("avatar", user.getAvatar())
                    .put("token", session.getId())
                    .put("tokenExpire", tokenExpire));
        }
    }

    private boolean userExists(User user) {
        return getUserCollection().find(and(
                eq("type", user.getoAuthServiceType().name()),
                eq("userId", user.getUserId())
        )).hasMore();
    }

    private NitriteCollection getUserCollection() {
        return nitrite.getCollection("users");
    }
}
