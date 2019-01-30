package com.hyd.hydrogenpac;

import com.hyd.fx.Fxml;
import com.hyd.fx.app.AppLogo;
import com.hyd.fx.app.AppPrimaryStage;
import com.hyd.fx.dialog.FileDialog;
import com.hyd.hydrogenpac.model.Configuration;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class HydrogenPacApplication extends Application {

    public static final Properties APP = new Properties();

    static {
        APP.setProperty("title", "PAC 编辑器");
    }

    private static Configuration configuration = new Configuration();

    static {
        try {
            APP.load(HydrogenPacApplication.class.getResourceAsStream("/app.properties"));
        } catch (IOException e) {
            // nothing to do
        }
    }

    public static Configuration getConfiguration() {
        return configuration;
    }

    public static void setConfiguration(Configuration configuration) {
        HydrogenPacApplication.configuration = configuration;
    }

    public void start(Stage primaryStage) throws Exception {
        FileDialog.setInitDirectory(new File("."));
        AppPrimaryStage.setPrimaryStage(primaryStage);
        AppLogo.setPath("/logo.png");
        AppLogo.setStageLogo(primaryStage);

        FXMLLoader fxmlLoader = Fxml.load("/fxml/main.fxml");
        primaryStage.setScene(new Scene(fxmlLoader.getRoot()));
        if (primaryStage.getTitle() == null) {
            primaryStage.setTitle(APP.getProperty("title"));
        }
        primaryStage.show();
    }
}
