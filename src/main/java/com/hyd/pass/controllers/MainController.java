package com.hyd.pass.controllers;

import com.hyd.fx.app.AppPrimaryStage;
import com.hyd.fx.dialog.AlertDialog;
import com.hyd.fx.dialog.FileDialog;
import com.hyd.fx.system.ClipboardHelper;
import com.hyd.pass.App;
import com.hyd.pass.Logger;
import com.hyd.pass.conf.UserConfig;
import com.hyd.pass.dialogs.*;
import com.hyd.pass.fx.EntryListView;
import com.hyd.pass.model.*;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.WindowEvent;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.List;
import java.util.function.Consumer;

import static com.hyd.fx.system.ClipboardHelper.getApplicationClipboard;
import static com.hyd.pass.fx.AuthenticationTableRow.CLIP_KEY;

/**
 * @author yiding.he
 */
public class MainController extends BaseController {

    private static final Logger logger = Logger.getLogger(MainController.class);

    public CheckMenuItem mnuAutoSave;

    public CheckMenuItem mnuAutoOpen;

    public CheckMenuItem mnuNoteWrap;

    public VBox tagsPane = new VBox();

    public TextField txtTagSearch;

    public TextField txtEntrySearch;

    public EntryListView lvEntries;

    public void initialize() {

        AppPrimaryStage.getPrimaryStage().setOnCloseRequest(this::beforeClose);
        AppPrimaryStage.getPrimaryStage().addEventFilter(KeyEvent.KEY_PRESSED, this::keyEventHandler);

        mnuAutoSave.setSelected(UserConfig.getBoolean("auto_save_on_exit", false));
        mnuAutoOpen.setSelected(UserConfig.getBoolean("auto_open_on_start", false));
        mnuNoteWrap.setSelected(UserConfig.getBoolean("note_wrap_text", false));

        txtTagSearch.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                String tagName = txtTagSearch.getText();
                if (tagName.trim().length() > 0) {
                    Label label = new Label(tagName);
                    label.getStyleClass().add("tag");
                    tagsPane.getChildren().add(label);
                    txtTagSearch.selectAll();
                }
            }
        });

        txtEntrySearch.textProperty().addListener((_ob, _old, _new) ->
                lvEntries.getItems().forEach(entry -> entry.highlightIfMatch(_new)));

        ///////////////////////////////////////////////

        tryAutoOpenRecentFile();
    }

    private void loadTags(PasswordLib passwordLib) {
        List<Tag> tags = passwordLib.getTags();
        if (tags != null) {
        }
    }

    private void tryAutoOpenRecentFile() {
        if (UserConfig.getBoolean("auto_open_on_start", false)) {
            String filePath = UserConfig.getString("latest_file", "");
            if (StringUtils.isNotBlank(filePath)) {
                File file = new File(filePath);
                if (file.exists() && file.canRead()) {
                    openPasswordLibFile(file);
                }
            }
        }
    }

    private void keyEventHandler(KeyEvent event) {
        if (event.isControlDown() && event.getCode() == KeyCode.X) {
            withCurrentAuthentication(auth -> ClipboardHelper.putString(auth.getUsername()));
            event.consume();
        } else if (event.isControlDown() && event.getCode() == KeyCode.C) {
            withCurrentAuthentication(auth -> ClipboardHelper.putString(auth.getPassword()));
            event.consume();
        }
    }

    private void withCurrentAuthentication(Consumer<Authentication> consumer) {
        Authentication item = getCurrentAuthentication();
        if (item != null) {
            consumer.accept(item);
        }
    }

    private Authentication getCurrentAuthentication() {
        return null;
    }

    @SuppressWarnings("unused")
    private void noteTextChanged(ObservableValue<? extends String> ob, String _old, String text) {
        Entry currentEntry = App.getCurrentEntry();
        if (currentEntry != null) {
            currentEntry.setNote(text);
            App.setPasswordLibChanged();
        }
    }

    @SuppressWarnings("unused")
    private void selectedEntryChanged(
            ObservableValue<? extends Entry> ob,
            Entry _old,
            Entry selected
    ) {
        App.setCurrentEntry(selected);
        refreshAuthenticationList();
    }

    private void beforeClose(WindowEvent event) {

        if (App.getPasswordLib() == null) {
            return;
        }

        if (App.getPasswordLib().isChanged()) {

            if (mnuAutoSave.isSelected()) {
                trySaveOnExit(event);
                return;
            }

            ButtonType buttonType = AlertDialog.confirmYesNoCancel("保存文件",
                    "文件尚未保存。是否保存然后退出？\n\n" +
                            "点击“是”则保存文件然后退出，点击“否”则直接退出，点击“取消”不退出。");

            if (buttonType == ButtonType.CANCEL) {
                event.consume();
                return;
            }

            if (buttonType == ButtonType.YES) {
                trySaveOnExit(event);
            }
        }
    }

    private void trySaveOnExit(WindowEvent event) {
        try {
            App.getPasswordLib().save();
        } catch (Exception e) {
            logger.error("保存文件失败", e);
            AlertDialog.error("保存文件失败: ", e);
            event.consume();
        }
    }

    public void openFileClicked() {
        File file = FileDialog.showOpenFile(
                AppPrimaryStage.getPrimaryStage(), "打开密码库文件", App.FILE_EXT, App.FILE_EXT_NAME);

        if (file != null) {
            openPasswordLibFile(file);
        }
    }

    private void openPasswordLibFile(File file) {
        EnterPasswordDialog dialog = new EnterPasswordDialog(file.getName());
        ButtonType buttonType = dialog.showAndWait().orElse(ButtonType.CANCEL);

        if (buttonType == ButtonType.OK) {
            try {
                String masterPassword = dialog.getPassword();
                PasswordLib passwordLib = new PasswordLib(file, masterPassword, false);
                loadPasswordLib(passwordLib);
                App.setPasswordLib(passwordLib);
            } catch (PasswordLibException e) {
                logger.error("打开文件失败", e);
                AlertDialog.error("密码不正确", e);
            }
        }
    }

    public void newFileClicked() {
        runSafe(this::newFileClicked0);
    }

    private void newFileClicked0() {
        CreatePasswordDialog createPasswordDialog = new CreatePasswordDialog();
        ButtonType buttonType = createPasswordDialog.showAndWait().orElse(ButtonType.CANCEL);

        if (buttonType == ButtonType.OK) {

            // 先保存现有密码库
            if (App.getPasswordLib() != null) {
                App.getPasswordLib().save();
            }

            try {
                createPasswordLib(createPasswordDialog);
            } catch (Exception e) {
                logger.error("创建密码库失败", e);
                AlertDialog.error("创建密码库失败", e);
            }
        }
    }

    private void createPasswordLib(CreatePasswordDialog createPasswordDialog) {
        PasswordLib passwordLib = new PasswordLib(
                createPasswordDialog.getSaveFile(),
                createPasswordDialog.getMasterPassword(),
                true
        );

        passwordLib.save();
        loadPasswordLib(passwordLib);
        App.setPasswordLib(passwordLib);
    }

    private void loadPasswordLib(PasswordLib passwordLib) {
        UserConfig.setString("latest_file", passwordLib.filePath());
        loadCategories(passwordLib);
        loadTags(passwordLib);
        loadEntries(passwordLib);
        AppPrimaryStage.getPrimaryStage().setTitle(App.APP_NAME + " - " + passwordLib.filePath());
    }

    private void loadEntries(PasswordLib passwordLib) {
        this.lvEntries.getItems().setAll(passwordLib.getEntries());
    }

    private void loadCategories(PasswordLib passwordLib) {
        Category rootCategory = passwordLib.getRootCategory();
        TreeItem<Category> root = loadCategory(rootCategory);
    }

    private TreeItem<Category> loadCategory(Category category) {
        TreeItem<Category> treeItem = new TreeItem<>(category);
        for (Category child : category.getChildren()) {
            treeItem.getChildren().add(loadCategory(child));
        }
        return treeItem;
    }

    private void refreshEntryList() {
    }

    private void refreshAuthenticationList() {
        Entry currentEntry = App.getCurrentEntry();
    }

    public void saveClicked() {
        if (App.getPasswordLib() != null) {
            App.getPasswordLib().save();
            AlertDialog.info("保存完毕", "密码库已成功保存。");
        }
    }

    public void newEntryClicked() {
        EntryInfoDialog dialog = new EntryInfoDialog(null);
        ButtonType buttonType = dialog.showAndWait().orElse(ButtonType.CANCEL);

        if (buttonType == ButtonType.OK) {
            Entry entry = dialog.getEntry();
            refreshEntryList();
            App.setPasswordLibChanged();
        }
    }

    public void newLoginClicked() {
        AuthenticationInfoDialog dialog = new AuthenticationInfoDialog(null);

        if (dialog.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            App.getCurrentEntry().getAuthentications().add(dialog.getAuthentication());
            refreshAuthenticationList();
        }
    }

    public void autoOpenToggleClicked() {
        UserConfig.setString("auto_open_on_start", String.valueOf(mnuAutoOpen.isSelected()));
    }

    public void autoSaveToggleClicked() {
        UserConfig.setString("auto_save_on_exit", String.valueOf(mnuAutoSave.isSelected()));
    }

    public void noteWrapToggleClicked() {
        // txtNote.setWrapText(mnuNoteWrap.isSelected());
        UserConfig.setString("note_wrap_text", String.valueOf(mnuNoteWrap.isSelected()));
    }

    public void exitClicked() {
        AppPrimaryStage.getPrimaryStage().close();
    }

    public void changeMasterPasswordClicked() {
        PasswordLib passwordLib = App.getPasswordLib();
        if (passwordLib == null) {
            return;
        }

        ChangeMasterPasswordDialog dialog = new ChangeMasterPasswordDialog();
        if (dialog.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            App.setMasterPassword(dialog.getNewPassword());
            passwordLib.save();
            AlertDialog.info("密码已修改", "密码库已经按照新的主密码重新加密保存。");
        }
    }

    public void onAuthTablePaste() {
        Authentication a = getApplicationClipboard(CLIP_KEY);
        if (a != null) {
            Entry currentEntry = App.getCurrentEntry();
            if (currentEntry != null) {
                currentEntry.getAuthentications().add(a.clone());
                refreshAuthenticationList();
            }
        }
    }
}
