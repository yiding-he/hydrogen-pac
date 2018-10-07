package com.hyd.hydrogenpac;

import java.util.HashMap;
import java.util.Map;

public class Errors {

    private static final Map<String, String> ERROR_MAPPINGS = new HashMap<>();

    static {
        ERROR_MAPPINGS.put("1", "Proxy name already exists.");
        ERROR_MAPPINGS.put("", "");
        ERROR_MAPPINGS.put("", "");
        ERROR_MAPPINGS.put("", "");
    }

    public static String getErrorMessage(String code) {
        return ERROR_MAPPINGS.get(code);
    }
}
