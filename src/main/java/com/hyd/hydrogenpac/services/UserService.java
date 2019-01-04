package com.hyd.hydrogenpac.services;

import com.hyd.hydrogenpac.beans.User;
import com.hyd.hydrogenpac.config.CookieConfig;
import org.dizitart.no2.objects.filters.ObjectFilters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.dizitart.no2.objects.filters.ObjectFilters.and;

/**
 * @author yiding.he
 */
@Component
public class UserService extends AbstractService<User> {

    @Autowired
    CookieConfig cookieConfig;

    @Autowired
    TokenService tokenService;

    @Override
    protected Class<User> getRepositoryType() {
        return User.class;
    }

    public User getLoggedInUser(String tokenString) {
        return tokenService.getAvailableToken(tokenString)
                .map(token -> findById(token.getUserId()))
                .orElse(null);
    }

    public void saveUser(User user) {
        User userById = findById(user.getUserId());
        if (userById == null) {
            repository.insert(user);
        }
    }

    public User findById(String userId) {
        return repository.find(and(
                ObjectFilters.eq("userId", userId)
        )).firstOrDefault();
    }

    public List<User> findAll() {
        return repository.find().toList();
    }
}
