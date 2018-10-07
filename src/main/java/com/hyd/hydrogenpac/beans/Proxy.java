package com.hyd.hydrogenpac.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Proxy {

    private String userId;

    private String name;

    private String type;

    private String host;

    private int port;
}
