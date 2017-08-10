package com.hyd.hydrogenpac.services;

import com.hyd.hydrogenpac.beans.PatternList;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * @author yiding.he
 */
@Service
public class PacService extends AbstractService {

    private static final String KEY_PREFIX = "hydrogen-pac-patterns";

    public List<PatternList> getPatternLists(String user) {
        return Collections.emptyList();
    }
}
