package com.hyd.hydrogenpac;

import com.hyd.fx.Fxml;
import com.hyd.fx.app.AppLogo;
import com.hyd.fx.app.AppPrimaryStage;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Properties;

public class HydrogenPacApplication extends Application {

    public static final Properties APP = new Properties();

    static {
        try {
            APP.load(HydrogenPacApplication.class.getResourceAsStream("/app.properties"));
        } catch (IOException e) {
            // nothing to do
        }
    }

    public void start(Stage primaryStage) throws Exception {
        AppPrimaryStage.setPrimaryStage(primaryStage);
        AppLogo.setPath("/logo.png");
        AppLogo.setStageLogo(primaryStage);

        FXMLLoader fxmlLoader = Fxml.load("/fxml/main.fxml");
        primaryStage.setScene(new Scene((Parent) fxmlLoader.getRoot()));
        primaryStage.setTitle(APP.getProperty("title", "PAC 编辑器"));
        primaryStage.show();
    }
}
