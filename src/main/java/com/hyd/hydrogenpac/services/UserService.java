package com.hyd.hydrogenpac.services;

import com.hyd.hydrogenpac.beans.User;
import com.hyd.hydrogenpac.config.CookieConfig;
import com.hyd.hydrogenpac.oauth.OAuthChannel;
import org.dizitart.no2.objects.Cursor;
import org.dizitart.no2.objects.ObjectRepository;
import org.dizitart.no2.objects.filters.ObjectFilters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

import static org.dizitart.no2.objects.filters.ObjectFilters.and;

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
                .map(token -> findById(token.getOauthChannel(), token.getUserId()))
                .orElse(null);
    }

    public void onUserLoggedIn(User user, String token) {
        saveUser(user);
        tokenService.saveToken(token, user.getUserId(), user.getOauthChannel());
    }

    public void saveUser(User user) {
        User userById = findById(user.getOauthChannel(), user.getUserId());
        if (userById == null) {
            getUserRepository().insert(user);
        }
    }

    public User findById(OAuthChannel channel, String userId) {
        return getUserRepository().find(and(
                ObjectFilters.eq("oauthChannel", channel.name()),
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

    public List<User> findAll() {
        Cursor<User> users = getUserRepository().find();
        return users.toList();
    }
}
