package com.hyd.hydrogenpac.helper;

import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.scene.control.Label;

public class ClearStatusTimer {

    private static Timer timer = new Timer(true);

    private static TimerTask currentTask;

    public static void statusSet(Label statusLabel) {

        if (currentTask != null) {
            currentTask.cancel();
        }

        currentTask = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> statusLabel.setText(""));
            }
        };

        timer.schedule(currentTask, 3000);
    }

}
