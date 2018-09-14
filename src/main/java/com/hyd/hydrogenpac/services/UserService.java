package com.hyd.hydrogenpac.services;

import com.hyd.hydrogenpac.beans.User;
import com.hyd.hydrogenpac.config.CookieConfig;
import com.hyd.hydrogenpac.oauth.OAuthChannel;
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

    public User getLoggedInUser(String tokenString) {
        return tokenService.getAvailableToken(tokenString)
                .map(token -> getUserById(token.getOauthChannel(), token.getUserId()))
                .orElse(null);
    }

    private User getUserByDocId(long userDocId) {
        return getUserRepository()
                .find(eq("userDocId", userDocId))
                .firstOrDefault();
    }

    public void onUserLoggedIn(User user, String token) {
        tokenService.saveToken(token, user.getUserId(), user.getOauthChannel());
    }

    private User createUser(OAuthChannel oAuthChannel, String userId, String username, String avatar) {
        User user = new User();
        user.setUserId(userId);
        user.setOauthChannel(oAuthChannel);
        user.setAvatar(avatar);
        user.setUsername(username);

        getUserRepository().insert(user);
        return user;
    }

    private User getUserById(OAuthChannel type, String userId) {
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
