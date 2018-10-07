package com.hyd.hydrogenpac.services;

import com.hyd.hydrogenpac.beans.Proxy;
import com.hyd.hydrogenpac.beans.User;
import org.dizitart.no2.objects.ObjectFilter;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import static org.dizitart.no2.objects.filters.ObjectFilters.and;
import static org.dizitart.no2.objects.filters.ObjectFilters.eq;

@Service
public class ProxyService extends AbstractService<Proxy> {

    @Override
    protected Class<Proxy> getRepositoryType() {
        return Proxy.class;
    }

    public List<Proxy> findProxies(User user) {

        if (user == null) {
            return Collections.emptyList();
        }

        return repository
                .find(eq("userId", user.getUserId()))
                .toList();
    }

    public Proxy findProxyByName(String userId, String proxyName) {
        final ObjectFilter filter = and(
                eq("userId", userId),
                eq("name", proxyName)
        );
        return repository.find(filter).firstOrDefault();
    }

    public void addProxy(Proxy proxy) {
        repository.insert(proxy);
    }

    public void deleteProxy(String userId, String proxyName) {
        repository.remove(and(
                eq("userId", userId),
                eq("name", proxyName)
        ));
    }
}
