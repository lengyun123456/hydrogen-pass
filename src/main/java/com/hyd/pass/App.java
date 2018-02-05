package com.hyd.pass;

import com.hyd.fx.app.AppPrimaryStage;
import com.hyd.pass.fx.Icons;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author yiding.he
 */
public class App extends Application {

    public static final String APP_NAME = "Hydrogen Pass 密码管理";

    public static void main(String[] args) {
        Application.launch(App.class);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        AppPrimaryStage.setPrimaryStage(primaryStage);

        Parent parent = FXMLLoader.load(getClass().getResource("/fxml/main.fxml"));
        primaryStage.getIcons().add(Icons.Logo.getImage());
        primaryStage.setTitle(APP_NAME);
        primaryStage.setScene(new Scene(parent));
        primaryStage.show();
    }
}
