package com.hyd.hydrogenpac.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yiding.he
 */
public class Pac {

    private List<PatternList> patternLists = new ArrayList<>();

    public List<PatternList> getPatternLists() {
        return patternLists;
    }

    public void setPatternLists(List<PatternList> patternLists) {
        this.patternLists = patternLists;
    }
}
