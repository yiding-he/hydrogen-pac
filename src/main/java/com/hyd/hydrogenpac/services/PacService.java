package com.hyd.hydrogenpac.services;

import com.hyd.hydrogenpac.beans.PatternList;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author yiding.he
 */
@Service
public class PacService extends AbstractService {

    public static final String KEY_PREFIX = "hydrogen-pac-patterns";

    public List<PatternList> getPatternLists(String user) {
        String key = KEY_PREFIX + ":" + user;
        List<String> patternKeys = getRedis().opsForList().range(key, 0, -1);
    }
}
