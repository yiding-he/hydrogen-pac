package com.hyd.hydrogenpac.controller;

import com.alibaba.fastjson.JSON;
import com.hyd.fx.app.AppLogo;
import com.hyd.fx.builders.ListViewBuilder;
import com.hyd.fx.builders.TableViewBuilder;
import com.hyd.fx.dialog.AlertDialog;
import com.hyd.fx.dialog.DialogBuilder;
import com.hyd.fx.dialog.FileDialog;
import com.hyd.fx.system.ClipboardHelper;
import com.hyd.fx.system.ZipFileCreator;
import com.hyd.fx.system.ZipFileReader;
import com.hyd.hydrogenpac.AppContext;
import com.hyd.hydrogenpac.HydrogenPacApplication;
import com.hyd.hydrogenpac.helper.DisplayTextHelper;
import com.hyd.hydrogenpac.model.Configuration;
import com.hyd.hydrogenpac.model.EntryNames;
import com.hyd.hydrogenpac.model.PatternList;
import com.hyd.hydrogenpac.model.Proxy;
import com.hyd.hydrogenpac.pac.PacTemplate;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.TableView;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import static com.hyd.fx.app.AppPrimaryStage.getPrimaryStage;

public class MainController {

    private static final String EXT = "*.hpac";

    private static final String EXT_NAME = "PAC 数据文件";

    public TableView<Proxy> tblProxy;

    public ListView<PatternList> lvPatternList;

    public ListView<String> lvPatterns;

    public void initialize() {

        TableViewBuilder.of(tblProxy)
                .addStrPropertyColumn("名称", Proxy::nameProperty)
                .addStrPropertyColumn("类型", Proxy::typeProperty)
                .addStrPropertyColumn("主机", Proxy::hostProperty)
                .addIntPropertyColumn("端口", proxy -> proxy.portProperty().asObject())
                .setColumnWidths(100, 100, 300, 100)
                .setOnItemDoubleClick(this::editProxy);

        ListViewBuilder.of(lvPatternList)
                .setOnItemDoubleClick(this::editPatternList)
                .setOnItemSelected(patternList -> this.lvPatterns.setItems(patternList.getPatterns()))
                .setStringFunction(DisplayTextHelper::getDisplayText);

        ListViewBuilder.of(lvPatterns)
                .setOnItemDoubleClick(pattern -> this.editPattern());

        loadConfiguration(HydrogenPacApplication.getConfiguration());
    }

    private void loadConfiguration(Configuration configuration) {
        this.tblProxy.setItems(configuration.getProxyList());
        this.lvPatternList.setItems(configuration.getPatternLists());
    }


    ///////////////////////////////////////////////////////////////
    public void openFileClicked() throws IOException {
        File file = FileDialog.showOpenFile(getPrimaryStage(), "打开文件", EXT, EXT_NAME);
        ZipFileReader reader = new ZipFileReader(file);

        String configurationJson = reader.readZipEntryString(EntryNames.CONFIGURATION);
        Configuration configuration = Configuration.parse(JSON.parseObject(configurationJson));
        HydrogenPacApplication.setConfiguration(configuration);

        loadConfiguration(configuration);
        AppContext.getInstance().setCurrentFile(file.getAbsolutePath());
    }

    public void saveFileClicked() throws IOException {
        String currentFile = AppContext.getInstance().getCurrentFile();
        if (currentFile == null) {
            saveAsClicked();
        } else {
            saveToFile(new File(currentFile));
        }
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

        AppContext.getInstance().setCurrentFile(file.getAbsolutePath());
    }

    ////////////////////////////////////////////////////////////// PROXY

    public void deleteProxyClicked() {
        Proxy proxy = tblProxy.getSelectionModel().getSelectedItem();
        if (proxy == null) {
            return;
        }

        if (!AlertDialog.confirmYesNo("删除代理", "确定要删除“" + proxy.getName() + "”吗？")) {
            return;
        }

        tblProxy.getItems().remove(proxy);
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

    ////////////////////////////////////////////////////////////// PATTERN LIST

    public void addPatternListClicked() {
        PatternListInfoController controller = new PatternListInfoController();
        controller.setPatternList(new PatternList());

        new DialogBuilder()
                .title("新建模板列表")
                .logo(AppLogo.getLogo())
                .body("/fxml/pattern-list-info.fxml", controller)
                .buttons(ButtonType.OK, ButtonType.CANCEL)
                .onOkButtonClicked(e -> this.addPatternListApply(controller.getPatternList()))
                .showAndWait();
    }

    private void addPatternListApply(PatternList patternList) {
        if (patternList != null) {
            this.lvPatternList.getItems().add(patternList);
        }
    }

    private void editPatternList(PatternList patternList) {
        if (patternList == null) {
            return;
        }

        PatternListInfoController controller = new PatternListInfoController();
        PatternList clone = PatternList.cloneOf(patternList);
        controller.setPatternList(clone);

        new DialogBuilder()
                .title("编辑模板列表")
                .logo(AppLogo.getLogo())
                .body("/fxml/pattern-list-info.fxml", controller)
                .buttons(ButtonType.OK, ButtonType.CANCEL)
                .onOkButtonClicked(e -> PatternList.copyPropsTo(clone, patternList))
                .showAndWait();
    }

    public void deletePattenListClicked() {
        PatternList patternList = lvPatternList.getSelectionModel().getSelectedItem();
        if (patternList == null) {
            return;
        }

        if (!AlertDialog.confirmYesNo("删除模板列表", "确认要删除模板列表“" + patternList.getName() + "”吗？")) {
            return;
        }

        lvPatternList.getItems().remove(patternList);
    }

    public void moveUpPatternListClicked() {
        PatternList patternList = lvPatternList.getSelectionModel().getSelectedItem();
        if (patternList == null) {
            return;
        }

        int index = lvPatternList.getItems().indexOf(patternList);
        if (index > 0) {
            lvPatternList.getItems().remove(patternList);
            lvPatternList.getItems().add(index - 1, patternList);
            lvPatternList.getSelectionModel().select(patternList);
        }
    }

    public void moveDownPatternListClicked() {
        PatternList patternList = lvPatternList.getSelectionModel().getSelectedItem();
        if (patternList == null) {
            return;
        }

        int index = lvPatternList.getItems().indexOf(patternList);
        if (index < lvPatternList.getItems().size() - 1) {
            lvPatternList.getItems().remove(patternList);
            lvPatternList.getItems().add(index + 1, patternList);
            lvPatternList.getSelectionModel().select(patternList);
        }
    }

    ////////////////////////////////////////////////////////////// Export

    public void exportClicked() throws IOException {

        File file = FileDialog.showSaveFile(getPrimaryStage(),
                "导出 PAC 文件", "*.pac", "PAC 文件", "未命名.pac");

        if (file == null) {
            return;
        }

        String pacContent = PacTemplate.generatePac();
        Files.write(file.toPath(), pacContent.getBytes(StandardCharsets.UTF_8));
    }

    public void exportToClipboardClicked() throws IOException {
        String pacContent = PacTemplate.generatePac();
        ClipboardHelper.putString(pacContent);
        AlertDialog.info("导出到剪切板", "PAC 内容已导出到剪切板。");
    }

    ////////////////////////////////////////////////////////////// Pattern

    public void addPatternClicked() {
        PatternInfoController controller = new PatternInfoController();
        controller.setPattern(-1, "");

        new DialogBuilder()
                .title("添加模板")
                .logo(AppLogo.getLogo())
                .body("/fxml/pattern-info.fxml", controller)
                .buttons(ButtonType.OK, ButtonType.CANCEL)
                .onOkButtonClicked(e -> controller.apply(this.lvPatterns.getItems()))
                .showAndWait();
    }

    public void deletePatternClicked() {
        String pattern = lvPatterns.getSelectionModel().getSelectedItem();
        if (pattern == null) {
            return;
        }

        if (!AlertDialog.confirmYesNo("删除模板", "确定要删除“" + pattern + "”吗？")) {
            return;
        }

        lvPatterns.getItems().remove(pattern);
    }

    private void editPattern() {

        MultipleSelectionModel<String> m = this.lvPatterns.getSelectionModel();
        PatternInfoController controller = new PatternInfoController();
        controller.setPattern(m.getSelectedIndex(), m.getSelectedItem());

        new DialogBuilder()
                .title("修改模板")
                .logo(AppLogo.getLogo())
                .body("/fxml/pattern-info.fxml", controller)
                .buttons(ButtonType.OK, ButtonType.CANCEL)
                .onOkButtonClicked(e -> controller.apply(this.lvPatterns.getItems()))
                .showAndWait();
    }

}
