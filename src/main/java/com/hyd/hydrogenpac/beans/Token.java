package com.hyd.hydrogenpac.beans;

/**
 * (description)
 * created at 2017/8/12
 *
 * @author yidin
 */
public class Token {

    private long userDocId;

    private String token;

    private long expiration;

    public long getUserDocId() {
        return userDocId;
    }

    public void setUserDocId(long userDocId) {
        this.userDocId = userDocId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getExpiration() {
        return expiration;
    }

    public void setExpiration(long expiration) {
        this.expiration = expiration;
    }
}
