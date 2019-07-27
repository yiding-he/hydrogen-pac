package com.hyd.hydrogenpac.pac;

import com.alibaba.fastjson.JSON;
import com.hyd.fx.system.ResourceHelper;
import com.hyd.hydrogenpac.AppContext;
import com.hyd.hydrogenpac.model.PacConfiguration;
import com.hyd.hydrogenpac.model.PatternList;
import com.hyd.hydrogenpac.model.Proxy;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PacTemplate {

    public static String generatePac() throws IOException {
        PacConfiguration pacConfiguration = AppContext.APP_CONTEXT.getPacConfiguration();
        String template = ResourceHelper.readAsString("/pac-template.js", StandardCharsets.UTF_8);

        List<PatternList> effectivePatternLists = getEffectivePatternLists(pacConfiguration);

        String proxies = JSON.toJSONString(pacConfiguration.getProxyList());
        String patternLists = JSON.toJSONString(effectivePatternLists);

        return template.replace("\"#PROXIES#\"", proxies)
                .replace("\"#PATTERN_LISTS#\"", patternLists);
    }

    private static List<PatternList> getEffectivePatternLists(PacConfiguration pacConfiguration) {

        Set<String> proxyNames = pacConfiguration.getProxyList()
                .stream()
                .map(Proxy::getName).collect(Collectors.toSet());

        return pacConfiguration.getPatternLists()
                .stream()
                .filter(patternList -> proxyNames.contains(patternList.getProxyName()))
                .collect(Collectors.toList());
    }
}
