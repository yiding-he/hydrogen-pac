package com.hyd.hydrogenpac.oauth;

public enum OAuthChannel {

    Baidu("百度"),
    None("(无)"),

    ;
    private String name;

    OAuthChannel(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
