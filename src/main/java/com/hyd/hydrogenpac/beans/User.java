package com.hyd.hydrogenpac.beans;

import com.hyd.hydrogenpac.oauth.OAuthChannel;
import lombok.Data;

@Data
public class User {

    private String username;

    private String userId;

    private String avatar;

    private String oauthUserId;

    private OAuthChannel oauthChannel;
}
