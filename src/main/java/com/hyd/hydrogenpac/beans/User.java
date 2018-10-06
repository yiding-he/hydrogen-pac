package com.hyd.hydrogenpac.beans;

import com.hyd.hydrogenpac.oauth.OAuthChannel;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class User {

    private String username;

    private String userId;

    private String avatar;

    private String oauthUserId;

    private OAuthChannel oauthChannel = OAuthChannel.None;

    public User() {
    }

    public User(OAuthChannel oAuthChannel, String userId, String username) {
        this.oauthChannel = oAuthChannel;
        this.userId = userId;
        this.username = username;
    }
}
