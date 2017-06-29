package com.hyd.hydrogenpac.beans;

import java.util.List;

/**
 * @author yiding.he
 */
public class PatternList {

    private List<String> pattens;

    private int priority;

    private String name;

    private ListType listType;

    public List<String> getPattens() {
        return pattens;
    }

    public void setPattens(List<String> pattens) {
        this.pattens = pattens;
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
