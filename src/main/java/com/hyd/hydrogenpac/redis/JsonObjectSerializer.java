package com.hyd.hydrogenpac.redis;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * @author yiding.he
 */
public class JsonObjectSerializer implements ObjectSerializer {

    @Override
    public <T> T deserialize(String str, Class<T> type) {
        return JSON.parseObject(str, type);
    }

    @Override
    public String serialize(Object object) {
        return JSON.toJSONString(object, SerializerFeature.DisableCircularReferenceDetect);
    }
}
