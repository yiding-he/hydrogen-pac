package com.hyd.hydrogenpac.controller;

import static com.hyd.fx.app.AppPrimaryStage.getPrimaryStage;
import static com.hyd.fx.enhancements.ListCellEnhancements.*;
import static com.hyd.hydrogenpac.AppContext.APP_CONTEXT;

import com.hyd.fx.app.AppLogo;
import com.hyd.fx.app.AppPrimaryStage;
import com.hyd.fx.builders.TableViewBuilder;
import com.hyd.fx.cells.ListCellFactory;
import com.hyd.fx.dialog.*;
import com.hyd.fx.helpers.DragDropHelper;
import com.hyd.fx.system.ClipboardHelper;
import com.hyd.hydrogenpac.*;
import com.hyd.hydrogenpac.helper.ClearStatusTimer;
import com.hyd.hydrogenpac.helper.DisplayTextHelper;
import com.hyd.hydrogenpac.http.HttpServer;
import com.hyd.hydrogenpac.http.HttpServer.Status;
import com.hyd.hydrogenpac.model.*;
import com.hyd.hydrogenpac.pac.PacTemplate;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Predicate;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.input.DataFormat;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class MainController {

    private static final String EXT = "*.hpac";

    private static final String EXT_NAME = "PAC 数据文件";

    public TableView<Proxy> tblProxy;

    public ListView<PatternList> lvPatternList;

    public ListView<String> lvPatterns;

    public Label lblStatus;

    public MenuItem menuHttpServer;

    public TabPane mainPane;

    public void initialize() {

        DragDropHelper.of(mainPane)
            .acceptDrop(DataFormat.FILES)
            .whenDataDropped(DataFormat.FILES, obj -> {
                @SuppressWarnings("unchecked") List<File> files = (List<File>) obj;
                onFileListDropped(files);
            });

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

        AppConfigurationRepo.addConfigurationSavedListener(() -> {
            setStatus("文件已保存。");
            updateStageTitle(APP_CONTEXT.getCurrentFile());
            exportQuietly();
        });

        HttpServer.getInstance().addStatusListener(status -> {
            if (status == Status.Running) {
                setStatus("HTTP 服务已开启。");
            } else if (status == Status.Stopped) {
                setStatus("HTTP 服务已停止。");
            }
        });

        loadConfiguration(APP_CONTEXT.getPacConfiguration());
        loadFile();
        initServerMenuItem();
    }

    private void loadFile() {
        String filePath = StringUtils.defaultIfBlank(
            readFromPreferences(),
            readFromCurrentDirectory()
        );

        if (StringUtils.isNotEmpty(filePath)) {
            readHpacFile(new File(filePath));
        }
    }

    private String readFromPreferences() {
        return AppContext.PREFERENCES.get(Prefs.LastOpenFile.name(), null);
    }

    private String readFromCurrentDirectory() {
        Value<String> filePath = Value.empty();

        try {
            Predicate<Path> isHpacFile = path ->
                Files.isRegularFile(path) && path.getFileName().toString().endsWith(".hpac");

            Files.list(Paths.get("."))
                .filter(isHpacFile)
                .findFirst()
                .ifPresent(path -> {
                    String message = "当前目录下找到文件 " + path.getFileName() + "，是否打开？";
                    if (AlertDialog.confirmYesNo("打开文件", message)) {
                        filePath.set(path.toFile().getAbsolutePath());
                    }
                });
        } catch (IOException e) {
            log.error("", e);
        }

        return filePath.get();
    }

    private void onFileListDropped(List<File> files) {
        if (files == null || files.size() != 1) {
            AlertDialog.error("错误", "只允许拖放一个文件进来。");
            return;
        }

        File file = files.get(0);
        readHpacFile(file);
    }

    private void initServerMenuItem() {
        Status httpServerStatus = HttpServer.getInstance().getStatus();
        updateServerMenuItem(httpServerStatus);

        HttpServer.getInstance().addStatusListener(this::updateServerMenuItem);
    }

    private void updateServerMenuItem(Status httpServerStatus) {
        if (httpServerStatus == Status.Running) {
            menuHttpServer.setText("HTTP 嵌入服务器[运行中]...");
        } else if (httpServerStatus == Status.Stopped) {
            menuHttpServer.setText("HTTP 嵌入服务器[已停止]...");
        }
    }

    private void setStatus(String text) {
        String timeStamp = new SimpleDateFormat("HH:mm:ss").format(new Date());
        this.lblStatus.setText("[" + timeStamp + "] " + text);
        ClearStatusTimer.statusSet(this.lblStatus);
    }

    private void loadConfiguration(PacConfiguration pacConfiguration) {
        this.tblProxy.setItems(pacConfiguration.getProxyList());
        this.lvPatternList.setItems(pacConfiguration.getPatternLists());

        PacConfiguration c = APP_CONTEXT.getPacConfiguration();
        if (c.isHttpServerAutoStart()) {
            int port = c.getHttpServerPort() == 0 ? HttpServer.DEFAULT_PORT : c.getHttpServerPort();
            HttpServer.getInstance().startServer(port);
        }

    }

    ///////////////////////////////////////////////////////////////
    public void openFileClicked() {
        File file = FileDialog.showOpenFile(getPrimaryStage(), "打开文件", EXT, EXT_NAME);
        if (file == null) {
            return;
        }

        readHpacFile(file);
        APP_CONTEXT.setLastExportFilePath(null);
    }

    private void readHpacFile(File file) {
        try {
            PacConfiguration pacConfiguration = AppConfigurationRepo.readConfiguration(file);
            APP_CONTEXT.setPacConfiguration(pacConfiguration);
            APP_CONTEXT.setCurrentFile(file.getAbsolutePath());
            loadConfiguration(pacConfiguration);
            updateStageTitle(file.getAbsolutePath());
            AppContext.PREFERENCES.put(Prefs.LastOpenFile.name(), file.getAbsolutePath());
        } catch (IOException e) {
            AlertDialog.error("打开文件错误", e);
        }
    }

    private static void saveQuietly() {
        String currentFile = APP_CONTEXT.getCurrentFile();
        if (currentFile != null) {
            AppConfigurationRepo.saveConfiguration(APP_CONTEXT.getPacConfiguration(), createFile(currentFile));
        }
    }

    public void saveFileClicked() {
        String currentFile = APP_CONTEXT.getCurrentFile();
        if (currentFile == null) {
            saveAsClicked();
        } else {
            AppConfigurationRepo.saveConfiguration(
                APP_CONTEXT.getPacConfiguration(), createFile(currentFile)
            );
        }
    }

    public void saveAsClicked() {
        File file = chooseFile("保存", EXT, EXT_NAME, "未命名.hpac");
        if (file != null) {
            AppConfigurationRepo.saveConfiguration(APP_CONTEXT.getPacConfiguration(), file);
        }
    }

    private static void updateStageTitle(String filePath) {
        String title = APP_CONTEXT.getTitle() + " - " + filePath;
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

    public void exportClicked() {

        File file = APP_CONTEXT.getLastExportFilePath() != null ?
            createFile(APP_CONTEXT.getLastExportFilePath()) :
            chooseFile("导出 PAC 文件", "*.pac", "PAC 文件", getPacFileName());

        if (file == null) {
            return;
        } else {
            APP_CONTEXT.setLastExportFilePath(file.getAbsolutePath());
        }

        exportToFile(file);
    }

    private String getPacFileName() {
        String exportFilePath = getExportFilePath();
        if (exportFilePath == null) {
            return "未命名.pac";
        } else {
            return StringUtils.substringAfterLast(exportFilePath, File.separator);
        }
    }

    private void exportToFile(File file) {
        try {
            String pacContent = PacTemplate.generatePac();
            Files.write(file.toPath(), pacContent.getBytes(StandardCharsets.UTF_8));
            setStatus("导出完毕。");
        } catch (IOException e) {
            AlertDialog.error("导出 PAC 失败", e);
        }
    }

    /**
     * 尝试自动导出
     */
    private void exportQuietly() {
        String exportFilePath = getExportFilePath();
        if (exportFilePath != null) {
            exportToFile(createFile(exportFilePath));
        }
    }

    private String getExportFilePath() {
        String exportFilePath = null;

        if (APP_CONTEXT.getLastExportFilePath() != null) {
            exportFilePath = APP_CONTEXT.getLastExportFilePath();

        } else if (APP_CONTEXT.getCurrentFile() != null) {
            exportFilePath = APP_CONTEXT.getCurrentFile().replace(".hpac", ".pac");
        }
        return exportFilePath;
    }

    private static File createFile(String path) {
        return new File(path);
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

    public void copyUrlClicked() {
        try {
            String exportFilePath = getExportFilePath();
            if (exportFilePath != null) {
                ClipboardHelper.putString(createFile(exportFilePath).toURI().toURL().toExternalForm());
                setStatus("文件的 URL 地址已经复制到剪切板。");
            }
        } catch (MalformedURLException e) {
            AlertDialog.error("导出 PAC 文件路径失败", e);
        }
    }

    public void serverMenuItemClicked() {
        ServerInfoController controller = new ServerInfoController();

        new DialogBuilder()
            .title("HTTP 嵌入服务器")
            .logo(AppLogo.getLogo())
            .body("/fxml/server-info.fxml", controller)
            .buttons(ButtonType.CLOSE)
            .onOkButtonClicked(e -> {
            })
            .onStageShown(event -> {
            })
            .showAndWait();
    }
}
