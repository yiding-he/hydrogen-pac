package com.hyd.hydrogenpac.pac;

import com.alibaba.fastjson.JSON;
import com.hyd.fx.system.ResourceHelper;
import com.hyd.hydrogenpac.HydrogenPacApplication;
import com.hyd.hydrogenpac.model.Configuration;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class PacTemplate {

    public static String generatePac() throws IOException {
        Configuration configuration = HydrogenPacApplication.getConfiguration();
        String template = ResourceHelper.readAsString("/pac-template.js", StandardCharsets.UTF_8);

        String proxies = JSON.toJSONString(configuration.getProxyList());
        String patternLists = JSON.toJSONString(configuration.getPatternLists());

        return template.replace("\"#PROXIES#\"", proxies)
                .replace("\"#PATTERN_LISTS#\"", patternLists);
    }
}
