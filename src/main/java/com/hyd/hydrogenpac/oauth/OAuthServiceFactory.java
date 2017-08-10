package com.hyd.hydrogenpac.oauth;

import org.springframework.stereotype.Component;

/**
 * (description)
 * created at 2017/8/10
 *
 * @author yidin
 */
@Component
public class OAuthServiceFactory {

    public OAuthService getOAuthService(OAuthServiceType type) {
        return OAuthService.getOAuthService(type);
    }
}
