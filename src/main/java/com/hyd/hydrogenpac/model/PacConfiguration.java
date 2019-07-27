package com.hyd.hydrogenpac.model;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyd.hydrogenpac.http.HttpServer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Data;

@Data
public class PacConfiguration {

    private ObservableList<Proxy> proxyList = FXCollections.observableArrayList();

    private ObservableList<PatternList> patternLists = FXCollections.observableArrayList();

    private boolean httpServerAutoStart;

    private int httpServerPort = HttpServer.DEFAULT_PORT;

    public static PacConfiguration parse(JSONObject configObj) {
        PacConfiguration pacConfiguration = new PacConfiguration();

        JSONArray proxyArr = configObj.getJSONArray("proxyList");
        proxyArr.forEach(obj -> {
            JSONObject proxyObj = (JSONObject) obj;
            pacConfiguration.getProxyList().add(Proxy.parse(proxyObj));
        });

        JSONArray patternListArr = configObj.getJSONArray("patternLists");
        patternListArr.forEach(obj -> {
            JSONObject patternListObj = (JSONObject) obj;
            pacConfiguration.getPatternLists().add(PatternList.parse(patternListObj));
        });

        pacConfiguration.setHttpServerAutoStart(configObj.getBooleanValue("httpServerAutoStart"));
        pacConfiguration.setHttpServerPort(configObj.getIntValue("httpServerPort"));

        return pacConfiguration;
    }
}
