package com.hyd.hydrogenpac.controller;

import com.hyd.fx.system.ClipboardHelper;
import com.hyd.hydrogenpac.http.HttpServer;
import com.hyd.hydrogenpac.http.HttpServer.Status;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;

public class ServerInfoController {

    public Spinner<Integer> spnPort;

    public Hyperlink lnkServerAddress;

    public Label lblServerStatus;

    public Label lblCopyStatus;

    public Button btnSwitchServer;

    private HttpServer httpServer = HttpServer.getInstance();

    public void initialize() {
        SimpleStringProperty prefix = new SimpleStringProperty("http://localhost:");
        SimpleStringProperty suffix = new SimpleStringProperty("/pac");
        lnkServerAddress.textProperty().bind(
            Bindings.concat(prefix, spnPort.valueProperty(), suffix)
        );

        httpServer.addStatusListener(status -> {
            updateLblStatus(status);
            updateBtnSwitchServer(status);
        });

        this.updateLblStatus(httpServer.getStatus());
        this.updateBtnSwitchServer(httpServer.getStatus());
    }

    private void updateBtnSwitchServer(Status status) {
        if (status == Status.Running) {
            btnSwitchServer.setText("停止服务");
        } else if (status == Status.Stopped) {
            btnSwitchServer.setText("开启服务");
        }
    }

    private void updateLblStatus(Status status) {
        if (status == Status.Running) {
            lblServerStatus.setText("运行中");
            lblServerStatus.setStyle("-fx-text-fill: #358535");
        } else if (status == Status.Stopped) {
            lblServerStatus.setText("已停止");
            lblServerStatus.setStyle("-fx-text-fill: #852526");
        }
    }

    public void switchServerClicked() {
        if (httpServer.getStatus() == Status.Stopped) {
            httpServer.startServer(spnPort.getValue());
        } else if (httpServer.getStatus() == Status.Running) {
            httpServer.stopServer();
        }
    }

    public void lnkServerAddressClicked() {
        ClipboardHelper.putString(this.lnkServerAddress.getText());
        this.lblCopyStatus.setText("(已复制)");
    }
}
