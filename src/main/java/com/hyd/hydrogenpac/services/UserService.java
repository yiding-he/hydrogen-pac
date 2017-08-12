package com.hyd.hydrogenpac.services;

import com.hyd.hydrogenpac.beans.Token;
import com.hyd.hydrogenpac.beans.User;
import com.hyd.hydrogenpac.config.CookieConfig;
import com.hyd.hydrogenpac.oauth.OAuthServiceType;
import org.dizitart.no2.objects.ObjectRepository;
import org.dizitart.no2.objects.filters.ObjectFilters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import static org.dizitart.no2.objects.filters.ObjectFilters.and;
import static org.dizitart.no2.objects.filters.ObjectFilters.eq;

/**
 * @author yiding.he
 */
@Component
public class UserService extends AbstractService {

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    @Autowired
    CookieConfig cookieConfig;

    @Autowired
    TokenService tokenService;

    @PostConstruct
    private void init() {
        LOG.info("current users: ");
        nitrite.getCollection("users").find().forEach(doc -> LOG.info(doc.toString()));
    }

    public User getLoggedInUser(String token) {
        return tokenService.getAvailableToken(token)
                .map(Token::getUserDocId)
                .map(this::getUserByDocId)
                .orElse(null);
    }

    private User getUserByDocId(long userDocId) {
        return getUserRepository()
                .find(eq("userDocId", userDocId))
                .firstOrDefault();
    }

    public void onUserLoggedIn(OAuthServiceType type, String userId, String username, String avatar, String token) {

        User user = getUserById(type, userId);
        if (user == null) {
            user = createUser(type, userId, username, avatar);
        }

        tokenService.saveToken(token, user.getUserDocId());
    }

    private User createUser(OAuthServiceType type, String userId, String username, String avatar) {
        User user = new User();
        user.setUserId(userId);
        user.setType(type);
        user.setAvatar(avatar);
        user.setUsername(username);

        getUserRepository().insert(user);
        return user;
    }

    private User getUserById(OAuthServiceType type, String userId) {
        return getUserRepository().find(and(
                ObjectFilters.eq("type", type.name()),
                ObjectFilters.eq("userId", userId)
        )).firstOrDefault();
    }

    private ObjectRepository<User> userObjectRepository;

    private ObjectRepository<User> getUserRepository() {
        if (userObjectRepository == null) {
            userObjectRepository = nitrite.getRepository(User.class);
        }
        return userObjectRepository;
    }
}
