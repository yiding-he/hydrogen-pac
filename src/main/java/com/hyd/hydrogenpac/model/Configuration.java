package com.hyd.hydrogenpac.model;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyd.hydrogenpac.http.HttpServer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Data;

@Data
public class Configuration {

    private ObservableList<Proxy> proxyList = FXCollections.observableArrayList();

    private ObservableList<PatternList> patternLists = FXCollections.observableArrayList();

    private boolean httpServerAutoStart;

    private int httpServerPort = HttpServer.DEFAULT_PORT;

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

        configuration.setHttpServerAutoStart(configObj.getBooleanValue("httpServerAutoStart"));
        configuration.setHttpServerPort(configObj.getIntValue("httpServerPort"));

        return configuration;
    }
}
