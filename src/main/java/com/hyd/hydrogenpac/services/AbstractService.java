package com.hyd.hydrogenpac.services;

import com.hyd.hydrogenpac.redis.Redis;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author yiding.he
 */
public abstract class AbstractService {

    @Autowired
    Redis redis;

    String combine(String... strs) {
        return String.join(":", strs);
    }
}
