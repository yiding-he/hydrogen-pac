package com.hyd.hydrogenpac.model;

import com.alibaba.fastjson.JSONObject;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Proxy {

    private StringProperty name = new SimpleStringProperty();

    private StringProperty type = new SimpleStringProperty();

    private StringProperty host = new SimpleStringProperty();

    private IntegerProperty port = new SimpleIntegerProperty();

    public static Proxy parse(JSONObject proxyObj) {
        Proxy proxy = new Proxy();
        proxy.setName(proxyObj.getString("name"));
        proxy.setType(proxyObj.getString("type"));
        proxy.setHost(proxyObj.getString("host"));
        proxy.setPort(proxyObj.getInteger("port"));
        return proxy;
    }

    public static Proxy cloneOf(Proxy proxy) {
        Proxy clone = new Proxy();
        copyPropsTo(proxy, clone);
        return clone;
    }

    public static void copyPropsTo(Proxy p1, Proxy p2) {
        p2.setName(p1.getName());
        p2.setType(p1.getType());
        p2.setHost(p1.getHost());
        p2.setPort(p1.getPort());
    }

    public String getType() {
        return type.get();
    }

    public StringProperty typeProperty() {
        return type;
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getHost() {
        return host.get();
    }

    public StringProperty hostProperty() {
        return host;
    }

    public void setHost(String host) {
        this.host.set(host);
    }

    public int getPort() {
        return port.get();
    }

    public IntegerProperty portProperty() {
        return port;
    }

    public void setPort(int port) {
        this.port.set(port);
    }
}
