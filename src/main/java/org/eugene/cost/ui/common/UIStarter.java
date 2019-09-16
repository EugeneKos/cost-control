package org.eugene.cost.ui.common;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import org.eugene.cost.exeption.ApplicationError;

public abstract class UIStarter<T> {
    public void start(String fxml, String title){
        try {
            Stage primaryStage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("ui/" + fxml));
            AnchorPane panel = loader.load();
            T controller = loader.getController();
            controllerSetting(controller, primaryStage);
            Scene scene = new Scene(panel);
            primaryStage.setTitle(title);
            primaryStage.setResizable(isResizable());
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e){
            throw new ApplicationError("Ошибка запуска приложения", e);
        }
    }

    protected boolean isResizable(){
        return false;
    }

    public abstract void controllerSetting(T controller, Stage primaryStage);
}
