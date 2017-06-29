package com.hyd.hydrogenpac.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author yiding.he
 */
public abstract class AbstractService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    RedisTemplate<String, Object> getRedis() {
        return redisTemplate;
    }
}
