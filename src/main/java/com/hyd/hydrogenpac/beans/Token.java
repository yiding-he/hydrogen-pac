package com.hyd.hydrogenpac.beans;

import lombok.Data;

@Data
public class Token {

    private String userId;

    private String token;

    private long expiration;
}
