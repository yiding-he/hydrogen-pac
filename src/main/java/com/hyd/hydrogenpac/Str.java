package com.hyd.hydrogenpac;

import java.util.function.Supplier;
import org.apache.commons.lang3.StringUtils;

public class Str {

    @SafeVarargs
    public static String defaultIfBlank(Supplier<String>... suppliers) {
        String value;
        for (Supplier<String> supplier : suppliers) {
            value = supplier.get();
            if (StringUtils.isNotBlank(value)) {
                return value;
            }
        }
        return null;
    }
}
