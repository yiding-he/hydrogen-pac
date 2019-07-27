package com.hyd.hydrogenpac;

import com.hyd.hydrogenpac.model.PacConfiguration;
import java.util.prefs.Preferences;
import lombok.Data;

/**
 * @author yiding_he
 */
@Data
public class AppContext {

    public static final String DEFAULT_TITLE = "PAC 编辑器";

    public static final AppContext APP_CONTEXT = new AppContext();

    public static final Preferences PREFERENCES = Preferences.userRoot().node("com.hyd.pac");

    // 当前已经打开的文件路径
    private String currentFile;

    // 上次导出的文件路径，下次导出时直接使用
    private String lastExportFilePath;

    private PacConfiguration pacConfiguration = new PacConfiguration();

    private String title;
}
