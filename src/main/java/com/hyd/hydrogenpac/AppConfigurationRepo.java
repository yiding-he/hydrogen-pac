package com.hyd.hydrogenpac;

import static com.hyd.hydrogenpac.AppContext.APP_CONTEXT;

import com.alibaba.fastjson.JSON;
import com.hyd.fx.dialog.AlertDialog;
import com.hyd.fx.system.ZipFileCreator;
import com.hyd.fx.system.ZipFileReader;
import com.hyd.hydrogenpac.model.Configuration;
import com.hyd.hydrogenpac.model.EntryNames;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AppConfigurationRepo {

    private static List<Runnable> onConfigurationSaved = new ArrayList<>();

    public static void addConfigurationSavedListener(Runnable runnable) {
        onConfigurationSaved.add(runnable);
    }

    public static Configuration readConfiguration(File file) throws IOException {
        ZipFileReader reader = new ZipFileReader(file);
        String configurationJson = reader.readZipEntryString(EntryNames.CONFIGURATION);
        return Configuration.parse(JSON.parseObject(configurationJson));
    }

    public static void saveConfiguration() {
        if (APP_CONTEXT.getCurrentFile() == null) {
            return;
        }
        saveConfiguration(
            APP_CONTEXT.getConfiguration(),
            new File(APP_CONTEXT.getCurrentFile())
        );
    }

    public static void saveConfiguration(Configuration configuration, File file) {
        try {
            String currentFile = APP_CONTEXT.getCurrentFile();
            if (currentFile != null) {
                ZipFileCreator creator = new ZipFileCreator(file);
                String json = JSON.toJSONString(configuration);
                creator.putEntry(EntryNames.CONFIGURATION, json, "UTF-8");
                creator.close();

                APP_CONTEXT.setCurrentFile(file.getAbsolutePath());
                onConfigurationSaved.forEach(Runnable::run);
            }
        } catch (IOException e) {
            AlertDialog.error("配置保存失败", e);
        }
    }
}
