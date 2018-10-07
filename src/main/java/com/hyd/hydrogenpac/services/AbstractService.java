package com.hyd.hydrogenpac.services;

import org.dizitart.no2.Nitrite;
import org.dizitart.no2.objects.ObjectRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

/**
 * @author yiding.he
 */
public abstract class AbstractService<T> {

    @Autowired
    protected Nitrite nitrite;

    ObjectRepository<T> repository;

    protected abstract Class<T> getRepositoryType();

    @PostConstruct
    protected void init() {
        this.repository = nitrite.getRepository(getRepositoryType());
    }
}
