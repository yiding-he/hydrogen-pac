package com.hyd.hydrogenpac.helper;

import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.scene.control.Label;

public class ClearStatusTimer {

    private static Timer timer = new Timer();

    public static void statusSet(Label statusLabel) {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> statusLabel.setText(""));
            }
        }, 3000);
    }
}
