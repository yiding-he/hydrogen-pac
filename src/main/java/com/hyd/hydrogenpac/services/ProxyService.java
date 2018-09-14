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
        return mapper.asObject(document, Proxy.class);
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

        Document proxyDoc = mapper.asDocument(proxy).put("userId", user.getUserId());
        getProxyCollection().insert(proxyDoc);
    }

    public void deleteProxy(User user, String name) {
        if (user == null) {
            return;
        }

        getProxyCollection().remove(Filters.and(
                Filters.eq("userId", user.getUserId()),
                Filters.eq("name", name)
        ));
    }
}
