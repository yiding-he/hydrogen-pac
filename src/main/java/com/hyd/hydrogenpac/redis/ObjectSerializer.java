package com.hyd.hydrogenpac.redis;

/**
 * @author yiding.he
 */
public interface ObjectSerializer {

    <T> T deserialize(String str, Class<T> type);

    String serialize(Object object);
}
