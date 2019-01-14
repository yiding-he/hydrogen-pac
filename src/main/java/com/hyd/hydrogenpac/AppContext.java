package com.hyd.hydrogenpac;

import lombok.Data;

/**
 * @author yiding_he
 */
@Data
public class AppContext {

    private static final AppContext instance = new AppContext();

    public static AppContext getInstance() {
        return instance;
    }

    private String currentFile;
}
