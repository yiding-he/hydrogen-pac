package com.hyd.hydrogenpac.beans;

import com.hyd.hydrogenpac.oauth.OAuthServiceType;

/**
 * (description)
 * created at 2017/8/10
 *
 * @author yidin
 */
public class User {

    private OAuthServiceType oAuthServiceType;

    private String username;

    private String userId;

    private String avatar;

    public OAuthServiceType getoAuthServiceType() {
        return oAuthServiceType;
    }

    public void setoAuthServiceType(OAuthServiceType oAuthServiceType) {
        this.oAuthServiceType = oAuthServiceType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
