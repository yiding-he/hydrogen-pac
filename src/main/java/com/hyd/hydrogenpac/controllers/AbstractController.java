package com.hyd.hydrogenpac.controllers;

import com.hyd.hydrogenpac.beans.User;

/**
 * (description)
 * created at 2017/8/11
 *
 * @author yidin
 */
public class AbstractController {

    private ThreadLocal<String> token = new ThreadLocal<>();

    private ThreadLocal<User> user = new ThreadLocal<>();

    private ThreadLocal<String> requestUrl = new ThreadLocal<>();

    public String getToken() {
        return token.get();
    }

    public void setToken(String token) {
        this.token.set(token);
    }

    public void setUser(User user) {
        this.user.set(user);
    }

    public User getUser() {
        return user.get();
    }

    public String getRequestUrl() {
        return this.requestUrl.get();
    }

    public void setRequestUrl(String uri) {
        this.requestUrl.set(uri);
    }
}
