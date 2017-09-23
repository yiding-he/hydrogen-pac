package com.hyd.hydrogenpac.services;

import com.hyd.hydrogenpac.beans.Proxy;
import com.hyd.hydrogenpac.beans.User;
import org.dizitart.no2.Document;
import org.dizitart.no2.NitriteCollection;
import org.dizitart.no2.filters.Filters;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProxyService extends AbstractService {

    private NitriteCollection getProxyCollection() {
        return nitrite.getCollection("proxies");
    }

    private Proxy parse(Document document) {
        Proxy proxy = new Proxy();
        proxy.setProxyName(document.get("proxyName", String.class));
        proxy.setProxyType(document.get("proxyType", String.class));
        proxy.setProxyAddress(document.get("proxyAddress", String.class));
        return proxy;
    }

    public List<Proxy> getProxies(User user) {

        if (user == null) {
            return Collections.emptyList();
        }

        return getProxyCollection()
                .find(Filters.eq("userId", user.getUserId()))
                .toList().stream()
                .map(this::parse)
                .collect(Collectors.toList());
    }

    public void addProxy(User user, Proxy proxy) {
        if (user == null) {
            return;
        }

        getProxyCollection().insert(new Document()
                .put("userId", user.getUserId())
                .put("proxyName", proxy.getProxyName())
                .put("proxyType", proxy.getProxyType())
                .put("proxyAddress", proxy.getProxyAddress())
        );
    }

    public void deleteProxy(User user, String name) {
        if (user == null) {
            return;
        }

        getProxyCollection().remove(Filters.and(
                Filters.eq("userId", user.getUserId()),
                Filters.eq("proxyName", name)
        ));
    }
}
