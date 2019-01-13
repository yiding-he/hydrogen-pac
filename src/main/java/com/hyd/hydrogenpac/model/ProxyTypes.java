package com.hyd.hydrogenpac.model;

import java.util.Arrays;
import java.util.List;

public interface ProxyTypes {

    String PROXY = "PROXY";

    String SOCKS5 = "SOCKS5";

    String DIRECT = "DIRECT";

    static List<String> values() {
        return Arrays.asList(PROXY, SOCKS5, DIRECT);
    }
}
