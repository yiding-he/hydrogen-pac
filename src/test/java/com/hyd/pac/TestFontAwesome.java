package com.hyd.pac;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.utils.FontAwesomeIconFactory;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class TestFontAwesome {

    public static void main(String[] args) {
        Application.launch(FontAwesomeIconsDemoApp.class, args);
    }

    public static class FontAwesomeIconsDemoApp extends Application {

        @Override
        public void start(Stage primaryStage) {
            FlowPane iconsPane = new FlowPane();
            iconsPane.setHgap(5);
            iconsPane.setVgap(5);
            for (FontAwesomeIcon icon : FontAwesomeIcon.values()) {
                Text iconText = FontAwesomeIconFactory.get().createIcon(icon, "2em");
                iconText.setFill(Color.web("#66AACC"));
                Button iconButton = new Button();
                iconButton.setGraphic(iconText);
                iconButton.setText(icon.name());
                iconsPane.getChildren().add(iconButton);
            }

            ScrollPane scrollPane = new ScrollPane(iconsPane);
            scrollPane.setFitToWidth(true);
            scrollPane.setPadding(new Insets(10));

            Scene scene = new Scene(scrollPane, 1000, 500);
            primaryStage.setScene(scene);
            primaryStage.setTitle("FontAwesomeFX: FontAwesomeIcons Demo: " + FontAwesomeIcon.values().length + " Icons");
            primaryStage.show();
        }

    }
}
