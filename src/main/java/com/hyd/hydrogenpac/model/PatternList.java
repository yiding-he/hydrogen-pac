package com.hyd.hydrogenpac.model;

import com.alibaba.fastjson.JSONObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class PatternList {

    private StringProperty name = new SimpleStringProperty();

    private ObservableList<String> patterns = FXCollections.observableArrayList();

    private StringProperty proxyName = new SimpleStringProperty();

    public static PatternList parse(JSONObject patternListObj) {
        PatternList patternList = new PatternList();
        patternList.setName(patternListObj.getString("name"));
        patternList.setProxyName(patternListObj.getString("proxyName"));
        patternList.getPatterns().addAll(patternListObj.getJSONArray("patterns").toJavaList(String.class));
        return patternList;
    }

    public static void copyPropsTo(PatternList p1, PatternList p2) {
        p2.setName(p1.getName());
        p2.setProxyName(p1.getProxyName());
        p2.setPatterns(p1.getPatterns());
    }

    public static PatternList cloneOf(PatternList patternList) {
        PatternList clone = new PatternList();
        copyPropsTo(patternList, clone);
        return clone;
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public ObservableList<String> getPatterns() {
        return patterns;
    }

    public void setPatterns(ObservableList<String> patterns) {
        this.patterns = patterns;
    }

    public String getProxyName() {
        return proxyName.get();
    }

    public StringProperty proxyNameProperty() {
        return proxyName;
    }

    public void setProxyName(String proxyName) {
        this.proxyName.set(proxyName);
    }

}
