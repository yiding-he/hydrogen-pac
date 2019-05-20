package com.hyd.hydrogenpac;

import static com.hyd.hydrogenpac.AppContext.APP_CONTEXT;

import com.hyd.fx.Fxml;
import com.hyd.fx.app.AppLogo;
import com.hyd.fx.app.AppPrimaryStage;
import com.hyd.fx.dialog.FileDialog;
import java.io.File;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HydrogenPacApplication extends Application {

    static {
        APP_CONTEXT.setTitle(AppContext.DEFAULT_TITLE);
    }

    public void start(Stage primaryStage) throws Exception {
        FileDialog.setInitDirectory(new File("."));
        AppPrimaryStage.setPrimaryStage(primaryStage);
        AppLogo.setPath("/logo.png");
        AppLogo.setStageLogo(primaryStage);

        FXMLLoader fxmlLoader = Fxml.load("/fxml/main.fxml");
        primaryStage.setScene(new Scene(fxmlLoader.getRoot()));
        if (primaryStage.getTitle() == null) {
            primaryStage.setTitle(APP_CONTEXT.getTitle());
        }
        primaryStage.show();
    }
}
