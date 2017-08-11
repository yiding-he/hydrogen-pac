package com.hyd.hydrogenpac.services;

import com.hyd.hydrogenpac.beans.User;
import com.hyd.hydrogenpac.oauth.OAuthServiceType;
import org.dizitart.no2.Document;
import org.dizitart.no2.NitriteCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import static org.dizitart.no2.filters.Filters.*;

/**
 * @author yiding.he
 */
@Component
public class UserService extends AbstractService {

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    @Value("${server.session.timeout}")
    private int sessionExpireSeconds;

    @PostConstruct
    private void init() {
        LOG.info("current users: ");
        getUserCollection().find().forEach(doc -> LOG.info(doc.toString()));
    }

    public User getLoggedInUser(String token) {
        long currentTime = System.currentTimeMillis() / 1000;

        Document document = getUserCollection().find(and(
                eq("token", token),
                gt("tokenExpire", currentTime)
        )).firstOrDefault();

        if (document == null) {
            return null;
        } else {
            return parseUser(document);
        }
    }

    private User parseUser(Document document) {
        User user = new User();
        user.setoAuthServiceType(OAuthServiceType.valueOf(document.get("type", String.class)));
        user.setUserId(document.get("userId", String.class));
        user.setUsername(document.get("userName", String.class));
        user.setAvatar(document.get("avatar", String.class));
        return user;
    }

    public void onUserLoggedIn(User user, String token) {

        long tokenExpire = System.currentTimeMillis() / 1000 + sessionExpireSeconds;

        if (!userExists(user)) {
            LOG.info("Create user record: " + user);

            getUserCollection().insert(new Document()
                    .put("type", user.getoAuthServiceType().name())
                    .put("userId", user.getUserId())
                    .put("userName", user.getUsername())
                    .put("avatar", user.getAvatar())
                    .put("token", token)
                    .put("tokenExpire", tokenExpire));
        } else {
            LOG.info("Update user record: " + user);

            getUserCollection().update(
                    and(
                            eq("userId", user.getUserId()),
                            eq("type", user.getoAuthServiceType().name())
                    ),
                    new Document()
                            .put("token", token)
                            .put("tokenExpire", tokenExpire)
            );
        }
    }

    private boolean userExists(User user) {
        return getUserCollection().find(and(
                eq("type", user.getoAuthServiceType().name()),
                eq("userId", user.getUserId())
        )).totalCount() > 0;
    }

    private NitriteCollection getUserCollection() {
        return nitrite.getCollection("users");
    }
}
