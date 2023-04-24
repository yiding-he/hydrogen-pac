package com.hyd.hydrogenpac;

import com.hyd.fx.Fxml;
import com.hyd.fx.app.AppLogo;
import com.hyd.fx.app.AppPrimaryStage;
import com.hyd.fx.dialog.FileDialog;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.util.Arrays;

import static com.hyd.hydrogenpac.AppContext.APP_CONTEXT;

/**
 * 运行时需要加上 JVM 参数：
 * <pre>
 *     --module-path [本机 javafx-sdk-20.0.1\lib]
 *     --add-modules javafx.controls
 *     --add-exports javafx.base/com.sun.javafx.binding=ALL-UNNAMED
 *     --add-exports javafx.base/com.sun.javafx.reflect=ALL-UNNAMED
 *     --add-exports javafx.base/com.sun.javafx.beans=ALL-UNNAMED
 *     --add-exports javafx.controls/com.sun.javafx.scene.control.behavior=ALL-UNNAMED
 *     --add-exports javafx.controls/com.sun.javafx.scene.control=ALL-UNNAMED
 *     --add-exports javafx.graphics/com.sun.javafx.stage=ALL-UNNAMED
 *     --add-exports javafx.graphics/com.sun.javafx.util=ALL-UNNAMED
 * </pre>
 */
@SpringBootApplication
public class HydrogenPacMain {

    static {
        APP_CONTEXT.setTitle(AppContext.DEFAULT_TITLE);
    }

    public static void main(String[] args) {
        APP_CONTEXT.setAppArguments(Arrays.asList(args));
        SpringApplication.run(HydrogenPacMain.class, args);
        Application.launch(HydrogenPacApplication.class, args);
    }

    public static class HydrogenPacApplication extends Application {

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
}
