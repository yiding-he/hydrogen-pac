package com.hyd.hydrogenpac.services;

import com.hyd.hydrogenpac.SpringBootTestBase;
import com.hyd.hydrogenpac.beans.Proxy;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ProxyServiceTest extends SpringBootTestBase {

    @Autowired
    private ProxyService proxyService;

    @Test
    public void findProxyByName() {
        final Proxy proxy = proxyService.findProxyByName("0", "SSR");
    }
}