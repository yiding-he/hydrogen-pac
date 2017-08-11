package com.hyd.hydrogenpac.controllers;

import com.hyd.hydrogenpac.beans.User;

/**
 * (description)
 * created at 2017/8/11
 *
 * @author yidin
 */
public class AbstractController {

    private String token;

    private User user;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
