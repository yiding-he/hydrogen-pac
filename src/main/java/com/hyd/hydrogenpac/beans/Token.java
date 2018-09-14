package com.hyd.hydrogenpac.beans;

import com.hyd.hydrogenpac.oauth.OAuthChannel;
import lombok.Data;

@Data
public class Token {

    private OAuthChannel oauthChannel;

    private String userId;

    private String token;

    private long expiration;
}
