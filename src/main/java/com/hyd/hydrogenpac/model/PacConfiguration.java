package com.hyd.hydrogenpac.model;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyd.hydrogenpac.http.HttpServer;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PacConfiguration {

    private List<Proxy> proxyList = new ArrayList<>();

    private List<PatternList> patternLists = new ArrayList<>();

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
