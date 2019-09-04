package org.eugene.cost.ui.payment;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.image.ImageView;

import javafx.stage.Stage;
import org.eugene.cost.config.SpringContext;
import org.eugene.cost.data.Operation;
import org.eugene.cost.data.Payment;
import org.eugene.cost.data.PaymentType;
import org.eugene.cost.service.IPaymentService;
import org.eugene.cost.ui.common.UIStarter;
import org.eugene.cost.ui.limit.LimitFXController;

public class PaymentFXController {
    @FXML
    private ListView<Operation> operationList;

    @FXML
    private Button operationsControlBtn;
    @FXML
    private Button financeControlBtn;
    @FXML
    private Button limitControlBtn;
    @FXML
    private Button operationHistoryBtn;

    @FXML
    private Label cashBalance;
    @FXML
    private Label cardBalance;
    @FXML
    private Label paymentType;

    @FXML
    private ImageView imageCard;
    @FXML
    private ImageView imageCash;
    @FXML
    private ImageView imageGraph;

    @FXML
    private ComboBox<Payment> cardBox;
    @FXML
    private ComboBox<Payment> cashBox;

    @FXML
    private RadioButton increaseRB;
    @FXML
    private RadioButton descendingRB;

    private IPaymentService paymentService;

    @FXML
    public void initialize(){
        paymentService = SpringContext.getBean(IPaymentService.class);

        cardBox.getItems().addAll(paymentService.getAllByType(PaymentType.CARD));
        cashBox.getItems().addAll(paymentService.getAllByType(PaymentType.CASH));

        limitControlBtn.setOnAction(event -> handleLimitControlBtn());
        financeControlBtn.setOnAction(event -> handleFinanceControlBtn());
        operationsControlBtn.setOnAction(event -> handleOperationsControlBtn());
    }

    private void handleLimitControlBtn(){
        UIStarter<LimitFXController> limitFXControllerUIStarter = new UIStarter<LimitFXController>() {
            @Override
            public void controllerSetting(LimitFXController controller, Stage primaryStage) {

            }
        };
        limitFXControllerUIStarter.start("limit-window.fxml", "Управление лимитами");
    }

    private void handleFinanceControlBtn(){
        UIStarter<PaymentDetailsFXController> paymentDetailsFXControllerUIStarter = new UIStarter<PaymentDetailsFXController>() {
            @Override
            public void controllerSetting(PaymentDetailsFXController controller, Stage primaryStage) {
                controller.init();
            }
        };
        paymentDetailsFXControllerUIStarter.start("payment-details-window.fxml", "Управление финансами");
    }

    private void handleOperationsControlBtn(){
        UIStarter<OperationFXController> operationFXControllerUIStarter = new UIStarter<OperationFXController>() {
            @Override
            public void controllerSetting(OperationFXController controller, Stage primaryStage) {

            }
        };
        operationFXControllerUIStarter.start("operation-window.fxml", "Управление операциями");
    }
}
