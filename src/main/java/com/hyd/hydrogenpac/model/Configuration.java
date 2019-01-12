package com.hyd.hydrogenpac.model;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Configuration {

    private ObservableList<Proxy> proxyList = FXCollections.observableArrayList();

    private ObservableList<PatternList> patternLists = FXCollections.observableArrayList();

    public static Configuration parse(JSONObject configObj) {
        Configuration configuration = new Configuration();

        JSONArray proxyArr = configObj.getJSONArray("proxyList");
        proxyArr.forEach(obj -> {
            JSONObject proxyObj = (JSONObject) obj;
            configuration.getProxyList().add(Proxy.parse(proxyObj));
        });

        JSONArray patternListArr = configObj.getJSONArray("patternLists");
        patternListArr.forEach(obj -> {
            JSONObject patternListObj = (JSONObject) obj;
            configuration.getPatternLists().add(PatternList.parse(patternListObj));
        });

        return configuration;
    }

    public ObservableList<Proxy> getProxyList() {
        return proxyList;
    }

    public void setProxyList(ObservableList<Proxy> proxyList) {
        this.proxyList = proxyList;
    }

    public ObservableList<PatternList> getPatternLists() {
        return patternLists;
    }

    public void setPatternLists(ObservableList<PatternList> patternLists) {
        this.patternLists = patternLists;
    }
}
