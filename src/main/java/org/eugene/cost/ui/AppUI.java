package org.eugene.cost.ui;

import javafx.application.Application;
import javafx.stage.Stage;

import org.eugene.cost.ui.common.UIStarter;
import org.eugene.cost.ui.secret.SecretKeyFXController;

public class AppUI extends Application {
    @Override
    public void start(Stage primaryStage) {
        UIStarter<SecretKeyFXController> secretKeyFXControllerUIStarter = new UIStarter<SecretKeyFXController>() {
            @Override
            public void controllerSetting(SecretKeyFXController controller, Stage primaryStage) {
                controller.setPrimaryStage(primaryStage);
                controller.init();
            }
        };
        secretKeyFXControllerUIStarter.start("secret-key-window.fxml", "Ввод секретного ключа");
    }
}
