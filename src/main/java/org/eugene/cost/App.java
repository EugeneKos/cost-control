package org.eugene.cost;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import org.eugene.cost.exeption.ApplicationError;

public class App extends Application {

    public void start(Stage primaryStage){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("ui/payment-control-window.fxml"));
            AnchorPane panel = loader.load();
            Scene scene = new Scene(panel);
            primaryStage.setTitle("Управление финансами");
            primaryStage.setResizable(false);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e){
            throw new ApplicationError("Ошибка запуска приложения", e);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
