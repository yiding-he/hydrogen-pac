package com.hyd.hydrogenpac.controller;

import static com.hyd.fx.app.AppPrimaryStage.getPrimaryStage;
import static com.hyd.fx.enhancements.ListCellEnhancements.acceptDrag;
import static com.hyd.fx.enhancements.ListCellEnhancements.addClassOnDragEnter;
import static com.hyd.fx.enhancements.ListCellEnhancements.canDrag;
import static com.hyd.fx.enhancements.ListCellEnhancements.setOnDoubleClicked;
import static com.hyd.fx.enhancements.ListCellEnhancements.setOnSelected;
import static com.hyd.hydrogenpac.HydrogenPacApplication.APP;

import com.alibaba.fastjson.JSON;
import com.hyd.fx.app.AppLogo;
import com.hyd.fx.app.AppPrimaryStage;
import com.hyd.fx.builders.TableViewBuilder;
import com.hyd.fx.cells.ListCellFactory;
import com.hyd.fx.dialog.AlertDialog;
import com.hyd.fx.dialog.DialogBuilder;
import com.hyd.fx.dialog.FileDialog;
import com.hyd.fx.system.ClipboardHelper;
import com.hyd.fx.system.ZipFileCreator;
import com.hyd.fx.system.ZipFileReader;
import com.hyd.hydrogenpac.AppContext;
import com.hyd.hydrogenpac.HydrogenPacApplication;
import com.hyd.hydrogenpac.helper.ClearStatusTimer;
import com.hyd.hydrogenpac.helper.DisplayTextHelper;
import com.hyd.hydrogenpac.model.Configuration;
import com.hyd.hydrogenpac.model.EntryNames;
import com.hyd.hydrogenpac.model.PatternList;
import com.hyd.hydrogenpac.model.Proxy;
import com.hyd.hydrogenpac.pac.PacTemplate;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.function.Predicate;
import javafx.collections.ObservableList;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Cell;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.TableView;

public class MainController {

    private static final String EXT = "*.hpac";

    private static final String EXT_NAME = "PAC 数据文件";

    public TableView<Proxy> tblProxy;

    public ListView<PatternList> lvPatternList;

    public ListView<String> lvPatterns;

    public Label lblStatus;

    public void initialize() throws Exception {

        TableViewBuilder.of(tblProxy)
            .addStrPropertyColumn("名称", Proxy::nameProperty)
            .addStrPropertyColumn("类型", Proxy::typeProperty)
            .addStrPropertyColumn("主机", Proxy::hostProperty)
            .addIntPropertyColumn("端口", proxy -> proxy.portProperty().asObject())
            .setColumnWidths(100, 100, 300, 100)
            .setOnItemDoubleClick(this::editProxy);

        lvPatternList.setCellFactory(new ListCellFactory<PatternList>()
            .withTextFunction(DisplayTextHelper::getDisplayText)
            .setCellInitializer(cell -> {
                setOnDoubleClicked(cell, this::editPatternList);
                setOnSelected(cell, patternList -> this.lvPatterns.setItems(patternList.getPatterns()));
                addClassOnDragEnter(cell, "list-cell-drag-hover");
                acceptDrag(cell, data -> {
                    ObservableList<String> patterns = cell.getItem().getPatterns();
                    patterns.add(String.valueOf(data));
                    Collections.sort(patterns);
                });
            })
        );

        lvPatterns.setCellFactory(new ListCellFactory<String>()
            .setCellInitializer(cell -> {
                setOnDoubleClicked(cell, pattern -> this.editPattern());
                canDrag(cell, Cell::getItem, () -> {
                    cell.getListView().getItems().remove(cell.getItem());
                    saveQuietly();
                });
            })
        );

        loadConfiguration(HydrogenPacApplication.getConfiguration());

        checkCurrentDirectoryFiles();
    }

    private void setStatus(String text) {
        String timeStamp = new SimpleDateFormat("HH:mm:ss").format(new Date());
        this.lblStatus.setText("[" + timeStamp + "] " + text);
        ClearStatusTimer.statusSet(this.lblStatus);
    }

    private void checkCurrentDirectoryFiles() throws IOException {
        Predicate<Path> isHpacFile = path ->
            Files.isRegularFile(path) && path.getFileName().toString().endsWith(".hpac");

        Files.list(Paths.get("."))
            .filter(isHpacFile)
            .findFirst()
            .ifPresent(path -> {
                String message = "当前目录下找到文件 " + path.getFileName() + "，是否打开？";
                if (AlertDialog.confirmYesNo("打开文件", message)) {
                    try {
                        readHpacFile(path.toFile());
                    } catch (IOException e) {
                        AlertDialog.error("打开文件失败", e);
                    }
                }
            });
    }

    private void loadConfiguration(Configuration configuration) {
        this.tblProxy.setItems(configuration.getProxyList());
        this.lvPatternList.setItems(configuration.getPatternLists());
    }


    ///////////////////////////////////////////////////////////////
    public void openFileClicked() throws IOException {
        File file = FileDialog.showOpenFile(getPrimaryStage(), "打开文件", EXT, EXT_NAME);
        if (file == null) {
            return;
        }

        readHpacFile(file);
        lastExportFilePath = null;
    }

    private void readHpacFile(File file) throws IOException {
        ZipFileReader reader = new ZipFileReader(file);

        String configurationJson = reader.readZipEntryString(EntryNames.CONFIGURATION);
        Configuration configuration = Configuration.parse(JSON.parseObject(configurationJson));
        HydrogenPacApplication.setConfiguration(configuration);

        loadConfiguration(configuration);
        AppContext.getInstance().setCurrentFile(file.getAbsolutePath());
        updateStageTitle(file);
    }

    private void saveQuietly() {
        try {
            String currentFile = AppContext.getInstance().getCurrentFile();
            if (currentFile != null) {
                saveToFile(new File(currentFile));
            }

            // 自动导出
            if (lastExportFilePath != null) {
                exportClicked();
            } else {
                setStatus("文件已保存。");
            }
        } catch (IOException e) {
            AlertDialog.error("文件保存失败", e);
        }
    }

    public void saveFileClicked() throws IOException {
        String currentFile = AppContext.getInstance().getCurrentFile();
        if (currentFile == null) {
            saveAsClicked();
        } else {
            saveToFile(new File(currentFile));
            setStatus("文件已保存。");
        }
    }

    public void saveAsClicked() throws IOException {
        File file = chooseFile("保存", EXT, EXT_NAME, "未命名.hpac");
        if (file != null) {
            saveToFile(file);
        }
    }

    private void saveToFile(File file) throws IOException {
        ZipFileCreator creator = new ZipFileCreator(file);
        String json = JSON.toJSONString(HydrogenPacApplication.getConfiguration());
        creator.putEntry(EntryNames.CONFIGURATION, json, "UTF-8");
        creator.close();

        AppContext.getInstance().setCurrentFile(file.getAbsolutePath());
        updateStageTitle(file);
    }

    private void updateStageTitle(File file) {
        String title = APP.getProperty("title") + " - " + file.getName();
        AppPrimaryStage.getPrimaryStage().setTitle(title);
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
        saveQuietly();
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
            .onStageShown(event -> controller.txtName.requestFocus())
            .showAndWait();
    }

    private void addProxyApply(Proxy proxy) {
        if (proxy != null) {
            this.tblProxy.getItems().add(proxy);
            saveQuietly();
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
            .onStageShown(event -> controller.txtName.requestFocus())
            .showAndWait();
    }

    private void editProxyApply(Proxy proxy, Proxy clone) {
        Proxy.copyPropsTo(clone, proxy);
        saveQuietly();
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
            .onStageShown(event -> controller.txtName.requestFocus())
            .showAndWait();
    }

    private void addPatternListApply(PatternList patternList) {
        if (patternList != null) {
            this.lvPatternList.getItems().add(patternList);
            saveQuietly();
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
            .onOkButtonClicked(e -> editPatternListApply(patternList, clone))
            .onStageShown(event -> controller.txtName.requestFocus())
            .showAndWait();
    }

    private void editPatternListApply(PatternList patternList, PatternList clone) {
        PatternList.copyPropsTo(clone, patternList);
        this.lvPatternList.refresh();
        saveQuietly();
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
        saveQuietly();
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

        saveQuietly();
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

        saveQuietly();
    }

    ////////////////////////////////////////////////////////////// Export

    // 上次导出的文件路径，下次导出时直接使用
    private String lastExportFilePath;

    public void exportClicked() throws IOException {

        File file;
        if (lastExportFilePath != null) {
            file = new File(lastExportFilePath);
        } else {
            file = chooseFile("导出 PAC 文件", "*.pac", "PAC 文件", "未命名.pac");
        }

        if (file == null) {
            return;
        } else {
            lastExportFilePath = file.getAbsolutePath();
        }

        String pacContent = PacTemplate.generatePac();
        Files.write(file.toPath(), pacContent.getBytes(StandardCharsets.UTF_8));

        ClipboardHelper.putString(file.toURI().toURL().toExternalForm());
        setStatus("导出完毕。文件的 URL 地址已经复制到剪切板。");
    }

    private File chooseFile(String s, String s2, String s3, String s4) {
        return FileDialog.showSaveFile(getPrimaryStage(),
            s, s2, s3, s4);
    }

    public void exportToClipboardClicked() throws IOException {
        String pacContent = PacTemplate.generatePac();
        ClipboardHelper.putString(pacContent);
        setStatus("PAC 内容已导出到剪切板。");
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
            .onOkButtonClicked(e -> addEditPatternApply(controller))
            .onStageShown(event -> controller.txtPattern.requestFocus())
            .showAndWait();
    }

    private void addEditPatternApply(PatternInfoController controller) {
        ObservableList<String> items = this.lvPatterns.getItems();
        controller.apply(items);
        saveQuietly();
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
        saveQuietly();
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
            .onOkButtonClicked(e -> addEditPatternApply(controller))
            .onStageShown(event -> controller.txtPattern.requestFocus())
            .showAndWait();
    }

}
