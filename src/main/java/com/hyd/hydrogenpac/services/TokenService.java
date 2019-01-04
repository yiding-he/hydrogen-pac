package com.hyd.hydrogenpac.services;

import com.hyd.hydrogenpac.beans.Token;
import com.hyd.hydrogenpac.config.CookieConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.dizitart.no2.objects.filters.ObjectFilters.*;

@Service
public class TokenService extends AbstractService<Token> {

    @Autowired
    CookieConfig cookieConfig;

    @Override
    protected Class<Token> getRepositoryType() {
        return Token.class;
    }

    public Optional<Token> getAvailableToken(String tokenString) {
        long currentTime = System.currentTimeMillis() / 1000;

        return Optional.ofNullable(repository
                .find(and(
                        eq("token", tokenString),
                        gt("expiration", currentTime)
                ))
                .firstOrDefault()
        );
    }

    public void saveToken(String tokenString, String userId) {
        long tokenExpire = System.currentTimeMillis() / 1000 + cookieConfig.getExpiry();

        Token token = new Token();
        token.setToken(tokenString);
        token.setUserId(userId);
        token.setExpiration(tokenExpire);

        repository.insert(token);
    }
}
