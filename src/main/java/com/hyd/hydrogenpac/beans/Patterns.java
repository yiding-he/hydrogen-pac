package com.hyd.hydrogenpac.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yiding.he
 */
public class Patterns {

    private String name;

    private int priority;

    private String proxyName;

    private List<String> patternList = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getProxyName() {
        return proxyName;
    }

    public void setProxyName(String proxyName) {
        this.proxyName = proxyName;
    }

    public List<String> getPatternList() {
        return patternList;
    }

    public void setPatternList(List<String> patternList) {
        this.patternList = patternList;
    }
}
