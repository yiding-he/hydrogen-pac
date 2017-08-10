package com.hyd.hydrogenpac.oauth;

public enum OAuthServiceType {

    Baidu("百度")

    ;
    private String name;

    OAuthServiceType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
