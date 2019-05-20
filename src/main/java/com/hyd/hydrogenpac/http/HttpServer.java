package com.hyd.hydrogenpac.http;

import com.hyd.fx.dialog.AlertDialog;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class HttpServer {

    public static final int DEFAULT_PORT = 9999;

    private static final HttpServer instance = new HttpServer();

    public enum Status {
        Running, Stopped
    }

    public static HttpServer getInstance() {
        return instance;
    }

    //////////////////////////////////////////////////////////////

    private NanoHttpdInstance nanoHttpdInstance;

    private List<Consumer<Status>> statusListeners = new ArrayList<>();

    public void addStatusListener(Consumer<Status> listener) {
        this.statusListeners.add(listener);
    }

    public Status getStatus() {
        return (this.nanoHttpdInstance != null
            && this.nanoHttpdInstance.isAlive()) ?
            Status.Running : Status.Stopped;
    }

    private void statusChanged() {
        this.statusListeners.forEach(listener -> listener.accept(this.getStatus()));
    }

    public void startServer(int port) {
        if (this.getStatus() == Status.Running) {
            return;
        }

        try {
            this.nanoHttpdInstance = new NanoHttpdInstance(port);
            this.nanoHttpdInstance.start();
            this.statusChanged();
        } catch (Exception e) {
            AlertDialog.error("启动失败", e);
        }
    }

    public void stopServer() {
        if (this.getStatus() == Status.Stopped) {
            return;
        }

        this.nanoHttpdInstance.stop();
        this.statusChanged();
    }
}
