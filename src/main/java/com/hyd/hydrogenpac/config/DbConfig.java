package com.hyd.hydrogenpac.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "nitrite")
public class DbConfig {

    private String path = "target/db/hydrogen-pac";

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
