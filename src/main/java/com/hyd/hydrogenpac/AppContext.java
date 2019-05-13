package com.hyd.hydrogenpac;

import lombok.Data;

/**
 * @author yiding_he
 */
@Data
public class AppContext {

    public static final AppContext APP_CONTEXT = new AppContext();

    // 当前已经打开的文件路径
    private String currentFile;

    // 上次导出的文件路径，下次导出时直接使用
    private String lastExportFilePath;

}
