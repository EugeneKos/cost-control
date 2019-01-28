package org.eugene.cost;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.eugene.cost.logic.model.Buy;
import org.eugene.cost.logic.model.Sessions;
import org.eugene.cost.ui.MainFxController;
import org.eugene.cost.ui.MoreFxController;
import org.eugene.cost.ui.SettingsFxController;

import java.io.IOException;

public class App extends Application {
    private Stage parent;

    public void start(Stage primaryStage) throws Exception {
        parent = primaryStage;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("ui/cost-control-window.fxml"));
        AnchorPane panel = loader.load();
        MainFxController controller = loader.getController();
        controller.setApp(this);
        Scene scene = new Scene(panel);
        primaryStage.setTitle("Контроль расходов");
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void openMore(MainFxController mainFxController, Buy buy) {
        try {
            Stage primaryStage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("ui/more.fxml"));
            AnchorPane panel = loader.load();
            MoreFxController controller = loader.getController();
            controller.setMainFxController(mainFxController);
            controller.setStage(primaryStage);
            controller.setCurrentBuy(buy);
            controller.init();
            Scene scene = new Scene(panel);
            primaryStage.initOwner(parent);
            primaryStage.initModality(Modality.APPLICATION_MODAL);
            primaryStage.setTitle("Добавить покупку");
            primaryStage.setResizable(false);
            primaryStage.setScene(scene);
            primaryStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openSetting(MainFxController mainFxController, Sessions sessions, boolean allowRemove){
        try {
            Stage primaryStage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("ui/settings.fxml"));
            AnchorPane panel = loader.load();
            SettingsFxController controller = loader.getController();
            controller.setMainFxController(mainFxController);
            controller.setStage(primaryStage);
            controller.setSessions(sessions);
            controller.init(allowRemove);
            Scene scene = new Scene(panel);
            primaryStage.initOwner(parent);
            primaryStage.initModality(Modality.APPLICATION_MODAL);
            primaryStage.setTitle("Настройки");
            primaryStage.setResizable(false);
            primaryStage.setScene(scene);
            primaryStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
