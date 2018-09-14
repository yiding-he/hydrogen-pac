package com.hyd.hydrogenpac.services;

import com.hyd.hydrogenpac.beans.Token;
import com.hyd.hydrogenpac.config.CookieConfig;
import org.dizitart.no2.objects.ObjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.dizitart.no2.objects.filters.ObjectFilters.*;

@Service
public class TokenService extends AbstractService {

    @Autowired
    CookieConfig cookieConfig;

    private ObjectRepository<Token> tokenObjectRepository;

    private ObjectRepository<Token> getTokenRepository() {
        if (tokenObjectRepository == null) {
            tokenObjectRepository = nitrite.getRepository(Token.class);
        }
        return tokenObjectRepository;
    }

    public Optional<Token> getAvailableToken(String tokenString) {
        long currentTime = System.currentTimeMillis() / 1000;

        return Optional.ofNullable(getTokenRepository()
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

        getTokenRepository().insert(token);
    }
}
