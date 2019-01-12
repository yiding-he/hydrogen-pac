package com.hyd.hydrogenpac.model;

import com.alibaba.fastjson.JSONObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class PatternList {

    private StringProperty name = new SimpleStringProperty();

    private ObservableList<String> pattens = FXCollections.observableArrayList();

    private StringProperty proxyName = new SimpleStringProperty();

    public static PatternList parse(JSONObject patternListObj) {
        PatternList patternList = new PatternList();
        patternList.setName(patternListObj.getString("name"));
        patternList.getPattens().addAll(patternListObj.getJSONArray("patterns").toJavaList(String.class));
        return patternList;
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

    public ObservableList<String> getPattens() {
        return pattens;
    }

    public void setPattens(ObservableList<String> pattens) {
        this.pattens = pattens;
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
