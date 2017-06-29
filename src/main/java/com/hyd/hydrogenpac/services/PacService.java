package com.hyd.hydrogenpac.services;

import com.hyd.hydrogenpac.beans.ListType;
import com.hyd.hydrogenpac.beans.PatternList;
import com.hyd.hydrogenpac.redis.Redis.RedisHash;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static java.util.Comparator.comparing;

/**
 * @author yiding.he
 */
@Service
public class PacService extends AbstractService {

    private static final String KEY_PREFIX = "hydrogen-pac-patterns";

    public List<PatternList> getPatternLists(String user) {
        List<PatternList> result = new ArrayList<>();

        String keyOfListOfKeys = combine(KEY_PREFIX, user, "list");
        List<String> listOfKeys = redis.getList(keyOfListOfKeys).all();

        listOfKeys.forEach(keyOfPatternListHash -> {
            RedisHash hash = redis.getHash(keyOfPatternListHash);

            // basic properties from redis hash
            PatternList patternList = new PatternList();
            patternList.setName(hash.get("name"));
            patternList.setListType(ListType.valueOf(hash.get("type")));
            patternList.setPriority(Integer.parseInt(hash.get("priority")));

            // list content from redis list
            String keyOfListItems = combine(keyOfPatternListHash, "items");
            patternList.setPatternList(redis.getList(keyOfListItems).all());

            result.add(patternList);
        });

        result.sort(comparing(PatternList::getPriority));
        return result;
    }

    // create a list with an increased priority number
    public void createPatternList(String user, String listName) {
        String keyOfListOfKeys = combine(KEY_PREFIX, user, "list");
        int newPriority = (int) (redis.getList(keyOfListOfKeys).size() + 1);

        String keyOfPatternListHash = KEY_PREFIX;

    }
}
