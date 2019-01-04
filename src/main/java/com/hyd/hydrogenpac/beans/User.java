package com.hyd.hydrogenpac.beans;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class User {

    private String username;

    private String userId;

    private String avatar;

    public User() {
    }
}
