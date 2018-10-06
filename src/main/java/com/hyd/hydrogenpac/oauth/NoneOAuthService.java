package com.hyd.hydrogenpac.oauth;

import com.hyd.hydrogenpac.beans.User;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @author yiding_he
 */
@Component
@OAuth(type = OAuthChannel.None)
public class NoneOAuthService extends OAuthService {

    @Override
    public String readCode(HttpServletRequest request) {
        return "";
    }

    @Override
    public OAuthEntry getOAuthEntry(String requestUrl) {
        return new OAuthEntry(OAuthChannel.None, "auth/callback/None", "");
    }

    @Override
    public User getUser(String authCode, String requestUrl) {
        User user = new User();
        user.setUserId("0");
        user.setUsername("none");
        user.setOauthChannel(OAuthChannel.None);
        user.setOauthUserId("0");
        return user;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
