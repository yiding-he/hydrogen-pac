package com.hyd.hydrogenpac.beans;

import com.hyd.hydrogenpac.oauth.OAuthServiceType;
import org.dizitart.no2.objects.Id;

/**
 * (description)
 * created at 2017/8/10
 *
 * @author yidin
 */
public class User {

    private OAuthServiceType type;

    private String username;

    private String userId;

    private String avatar;

    @Id
    private long userDocId;

    public long getUserDocId() {
        return userDocId;
    }

    public void setUserDocId(long userDocId) {
        this.userDocId = userDocId;
    }

    public OAuthServiceType getType() {
        return type;
    }

    public void setType(OAuthServiceType type) {
        this.type = type;
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

    @Override
    public String toString() {
        return "User{" +
                "type=" + type +
                ", username='" + username + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
