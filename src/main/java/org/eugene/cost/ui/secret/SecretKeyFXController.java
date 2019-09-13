package org.eugene.cost.ui.secret;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

import org.eugene.cost.cache.PaymentCache;
import org.eugene.cost.cache.SessionCache;
import org.eugene.cost.config.SpringContext;
import org.eugene.cost.file.encryption.EncryptionService;
import org.eugene.cost.service.util.PropertyLoader;
import org.eugene.cost.ui.common.MessageType;
import org.eugene.cost.ui.common.UIStarter;
import org.eugene.cost.ui.common.UIUtils;
import org.eugene.cost.ui.payment.PaymentFXController;
import org.springframework.util.StringUtils;

public class SecretKeyFXController {
    @FXML
    private PasswordField passwordField;

    @FXML
    private Button okBtn;
    @FXML
    private Button cancelBtn;

    private Stage primaryStage;

    private EncryptionService encryptionService;

    private PaymentCache paymentCache;
    private SessionCache sessionCache;

    public void init(){
        encryptionService = SpringContext.getBean(EncryptionService.class);

        paymentCache = SpringContext.getBean(PaymentCache.class);
        sessionCache = SpringContext.getBean(SessionCache.class);

        okBtn.setOnAction(event -> handleOkBtn());
        cancelBtn.setOnAction(event -> handleCancelBtn());
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    private void handleOkBtn(){
        boolean dataEncryption = Boolean.valueOf(PropertyLoader.getProperty("data.encryption"));

        String secretKey = passwordField.getText();
        if(dataEncryption && StringUtils.isEmpty(secretKey)){
            UIUtils.showOptionPane("Секретный ключ должен быть введен!",
                    "Предупреждение", MessageType.WARNING);
            return;
        }

        encryptionService.setSecretKey(secretKey);
        paymentCache.initialize();
        sessionCache.initialize();

        startPayment();
        primaryStage.close();
    }

    private void handleCancelBtn(){
        primaryStage.close();
    }

    private void startPayment(){
        UIStarter<PaymentFXController> paymentFXControllerUIStarter = new UIStarter<PaymentFXController>() {
            @Override
            public void controllerSetting(PaymentFXController controller, Stage primaryStage) {}
        };
        paymentFXControllerUIStarter.start("payment-window.fxml", "Управление расходами");
    }
}
