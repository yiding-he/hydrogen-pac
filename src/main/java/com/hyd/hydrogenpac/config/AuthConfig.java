package com.hyd.hydrogenpac.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "auth")
public class AuthConfig {

    private String baiduApiKey;

    private String baiduApiSecret;

    public String getBaiduApiSecret() {
        return baiduApiSecret;
    }

    public void setBaiduApiSecret(String baiduApiSecret) {
        this.baiduApiSecret = baiduApiSecret;
    }

    public String getBaiduApiKey() {
        return baiduApiKey;
    }

    public void setBaiduApiKey(String baiduApiKey) {
        this.baiduApiKey = baiduApiKey;
    }
}
