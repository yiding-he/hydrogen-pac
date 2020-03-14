package com.hyd.hydrogenpac.controller;

import com.hyd.fx.builders.ComboBoxBuilder;
import com.hyd.fx.components.IntegerSpinner;
import com.hyd.hydrogenpac.model.Proxy;
import com.hyd.hydrogenpac.model.ProxyTypes;
import java.util.Objects;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;

public class ProxyInfoController {

    public TextField txtName;

    public TextField txtHost;

    public IntegerSpinner spnPort;

    public ComboBox<String> cmbType;

    private Proxy proxy;

    public void initialize() {
        this.txtName.textProperty().bindBidirectional(proxy.nameProperty());
        this.txtHost.textProperty().bindBidirectional(proxy.hostProperty());
        this.spnPort.getValueFactory().valueProperty().bindBidirectional(proxy.portProperty().asObject());

        ComboBoxBuilder.of(cmbType)
                .setItems(ProxyTypes.values())
                .setInitialValue(type -> Objects.equals(proxy.getType(), type))
                .setOnChange(type -> proxy.setType(type));
    }

    public Proxy getProxy() {
        return proxy;
    }

    public void setProxy(Proxy proxy) {
        this.proxy = proxy;
    }
}
