package com.hyd.hydrogenpac.controller;

import com.alibaba.fastjson.JSON;
import com.hyd.fx.app.AppLogo;
import com.hyd.fx.builders.TableViewBuilder;
import com.hyd.fx.dialog.DialogBuilder;
import com.hyd.fx.dialog.FileDialog;
import com.hyd.fx.system.ZipFileCreator;
import com.hyd.fx.system.ZipFileReader;
import com.hyd.hydrogenpac.HydrogenPacApplication;
import com.hyd.hydrogenpac.model.Configuration;
import com.hyd.hydrogenpac.model.EntryNames;
import com.hyd.hydrogenpac.model.Proxy;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;

import java.io.File;
import java.io.IOException;

import static com.hyd.fx.app.AppPrimaryStage.getPrimaryStage;

public class MainController {

    public static final String EXT = "*.hpac";

    public static final String EXT_NAME = "PAC 数据文件";

    public TableView<Proxy> tblProxy;

    public void initialize() {

        TableViewBuilder.of(tblProxy)
                .addStrPropertyColumn("名称", Proxy::nameProperty)
                .addStrPropertyColumn("主机", Proxy::hostProperty)
                .addIntPropertyColumn("端口", proxy -> proxy.portProperty().asObject())
                .setOnItemDoubleClick(this::editProxy);

        loadConfiguration(HydrogenPacApplication.getConfiguration());
    }

    private void loadConfiguration(Configuration configuration) {
        this.tblProxy.setItems(configuration.getProxyList());
    }


    ///////////////////////////////////////////////////////////////
    public void openFileClicked() throws IOException {
        File file = FileDialog.showOpenFile(getPrimaryStage(), "打开文件", EXT, EXT_NAME);
        ZipFileReader reader = new ZipFileReader(file);

        String configurationJson = reader.readZipEntryString(EntryNames.CONFIGURATION);
        Configuration configuration = Configuration.parse(JSON.parseObject(configurationJson));
        HydrogenPacApplication.setConfiguration(configuration);

        loadConfiguration(configuration);
    }

    public void saveFileClicked() throws IOException {
    }

    public void saveAsClicked() throws IOException {
        File file = FileDialog.showSaveFile(getPrimaryStage(), "保存", EXT, EXT_NAME, "未命名.hpac");
        if (file == null) {
            return;
        }

        saveToFile(file);
    }

    private void saveToFile(File file) throws IOException {
        ZipFileCreator creator = new ZipFileCreator(file);
        String json = JSON.toJSONString(HydrogenPacApplication.getConfiguration());
        creator.putEntry(EntryNames.CONFIGURATION, json, "UTF-8");
        creator.close();
    }

    public void addProxyClicked() {
        ProxyInfoController controller = new ProxyInfoController();
        controller.setProxy(new Proxy());

        new DialogBuilder()
                .title("添加代理")
                .logo(AppLogo.getLogo())
                .body("/fxml/proxy-info.fxml", controller)
                .buttons(ButtonType.OK, ButtonType.CANCEL)
                .onOkButtonClicked(e -> this.addProxyApply(controller.getProxy()))
                .showAndWait();
    }

    private void addProxyApply(Proxy proxy) {
        if (proxy != null) {
            this.tblProxy.getItems().add(proxy);
        }
    }

    private void editProxy(Proxy proxy) {
        ProxyInfoController controller = new ProxyInfoController();
        Proxy clone = Proxy.cloneOf(proxy);
        controller.setProxy(clone);

        new DialogBuilder()
                .title("修改代理 - " + proxy.getName())
                .logo(AppLogo.getLogo())
                .body("/fxml/proxy-info.fxml", controller)
                .buttons(ButtonType.OK, ButtonType.CANCEL)
                .onOkButtonClicked(e -> this.editProxyApply(proxy, clone))
                .showAndWait();
    }

    private void editProxyApply(Proxy proxy, Proxy clone) {
        Proxy.copyPropsTo(clone, proxy);
    }
}
