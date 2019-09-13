package org.eugene.cost.ui;

import javafx.application.Application;
import javafx.stage.Stage;
import org.eugene.cost.ui.common.UIStarter;
import org.eugene.cost.ui.payment.PaymentFXController;

public class AppUI extends Application {
    @Override
    public void start(Stage primaryStage) {
        UIStarter<PaymentFXController> paymentFXControllerUIStarter = new UIStarter<PaymentFXController>() {
            @Override
            public void controllerSetting(PaymentFXController controller, Stage primaryStage) {}
        };
        paymentFXControllerUIStarter.start("payment-window.fxml", "Управление финансами");
    }
}
