package com.hyd.pass.dialogs;

import com.hyd.fx.app.AppLogo;
import com.hyd.fx.dialog.AlertDialog;
import com.hyd.fx.dialog.BasicDialog;
import com.hyd.fx.dialog.DialogBuilder;
import com.hyd.pass.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * @author yidin
 */
public class ChangeMasterPasswordDialog extends BasicDialog {

    @FXML
    public PasswordField txtOldPassword;

    @FXML
    public PasswordField txtNewPassword;

    @FXML
    public PasswordField txtNewPassword2;

    private String newPassword;

    public ChangeMasterPasswordDialog() {
        new DialogBuilder()
                .title("修改主密码")
                .logo(AppLogo.getLogo())
                .body("/fxml/change-master-password.fxml", this)
                .buttons(ButtonType.OK, ButtonType.CANCEL)
                .onOkButtonClicked(this::onOkButtonClicked)
                .onStageShown(event -> txtOldPassword.requestFocus())
                .applyTo(this);

    }

    public String getNewPassword() {
        return newPassword;
    }

    private void onOkButtonClicked(ActionEvent actionEvent) {

        String oldPassword = txtOldPassword.getText();
        String newPassword = txtNewPassword.getText();
        String newPassword2 = txtNewPassword2.getText();

        if (!Objects.equals(oldPassword, App.getMasterPassword())) {
            AlertDialog.error("修改主密码失败", "旧密码不正确");
            actionEvent.consume();
            return;
        }

        if (StringUtils.isAnyBlank(newPassword, newPassword2)) {
            AlertDialog.error("修改主密码失败", "新密码不能为空");
            actionEvent.consume();
            return;
        }

        if (!Objects.equals(newPassword, newPassword2)) {
            AlertDialog.error("修改主密码失败", "两次输入的新密码不一致");
            actionEvent.consume();
            return;
        }

        this.newPassword = newPassword;
    }
}
