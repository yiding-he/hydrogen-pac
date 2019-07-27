package com.hyd.hydrogenpac.helper;

import com.hyd.hydrogenpac.AppContext;
import com.hyd.hydrogenpac.model.PacConfiguration;
import com.hyd.hydrogenpac.model.PatternList;
import com.hyd.hydrogenpac.model.Proxy;
import org.apache.commons.lang3.StringUtils;

public class DisplayTextHelper {

    public static String getDisplayText(PatternList patternList) {

        if (StringUtils.isBlank(patternList.getProxyName())) {
            return patternList.getName() + "（没有指定代理）";
        }

        PacConfiguration c = AppContext.APP_CONTEXT.getPacConfiguration();
        String proxyName = c.getProxyList().stream()
                .filter(p -> p.getName().equals(patternList.getProxyName()))
                .map(Proxy::getName)
                .findFirst().orElse("没有指定代理");

        return patternList.getName() + "（" + proxyName + "）";
    }
}
