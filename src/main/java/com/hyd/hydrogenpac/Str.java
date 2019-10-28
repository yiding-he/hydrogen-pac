package com.hyd.hydrogenpac;

import java.util.function.Supplier;

public class Str extends com.hyd.fx.utils.Str {

    @SafeVarargs
    public static String defaultIfBlank(Supplier<String>... suppliers) {
        String value;
        for (Supplier<String> supplier : suppliers) {
            value = supplier.get();
            if (isNotBlank(value)) {
                return value;
            }
        }
        return null;
    }

    public static String substringAfterLast(String s, String search) {
        if (s == null) {
            return null;
        }

        if (search == null) {
            return "";
        }

        int i = s.lastIndexOf(search);
        if (i == -1) {
            return "";
        } else {
            return s.substring(i + search.length());
        }
    }
}
