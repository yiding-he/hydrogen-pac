package com.hyd.hydrogenpac.controller;

import com.hyd.hydrogenpac.model.Proxy;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;

public class ProxyInfoController {

    public TextField txtName;

    public TextField txtHost;

    public Spinner<Integer> spnPort;

    private Proxy proxy;

    public void initialize() {
        this.txtName.textProperty().bindBidirectional(proxy.nameProperty());
        this.txtHost.textProperty().bindBidirectional(proxy.hostProperty());
        this.spnPort.getValueFactory().valueProperty().bindBidirectional(proxy.portProperty().asObject());
    }

    public Proxy getProxy() {
        return proxy;
    }

    public void setProxy(Proxy proxy) {
        this.proxy = proxy;
    }
}
