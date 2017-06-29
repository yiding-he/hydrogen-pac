package com.hyd.hydrogenpac.beans;

import java.util.List;

/**
 * @author yiding.he
 */
public class PatternList {

    private List<String> patternList;

    private int priority;

    private String name;

    private ListType listType;

    public List<String> getPatternList() {
        return patternList;
    }

    public void setPatternList(List<String> patternList) {
        this.patternList = patternList;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ListType getListType() {
        return listType;
    }

    public void setListType(ListType listType) {
        this.listType = listType;
    }
}
