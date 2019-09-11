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
import org.eugene.cost.service.IOperationService;
import org.eugene.cost.service.IPaymentService;
import org.eugene.cost.ui.common.UIStarter;
import org.eugene.cost.ui.common.UIUtils;
import org.eugene.cost.ui.limit.LimitFXController;

public class PaymentFXController {
    private final String CARD_TEXT = "карты";
    private final String CASH_TEXT = "наличных";

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
    private IOperationService operationService;

    @FXML
    public void initialize(){
        paymentService = SpringContext.getBean(IPaymentService.class);
        operationService = SpringContext.getBean(IOperationService.class);

        updateCardAndCashCB();

        cardBox.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    displayBalance(newValue, cardBalance);
                    displayOperations(newValue, CARD_TEXT);
                });

        cashBox.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    displayBalance(newValue, cashBalance);
                    displayOperations(newValue, CASH_TEXT);
                });

        increaseRB.setSelected(true);
        descendingRB.setSelected(false);

        increaseRB.setOnAction(event -> handleIncreaseRB());
        descendingRB.setOnAction(event -> handleDescendingRB());

        imageCard.setOnMouseClicked(event -> handleImageCard());
        imageCash.setOnMouseClicked(event -> handleImageCash());

        limitControlBtn.setOnAction(event -> handleLimitControlBtn());
        financeControlBtn.setOnAction(event -> handleFinanceControlBtn());
        operationsControlBtn.setOnAction(event -> handleOperationsControlBtn());
    }

    private void handleLimitControlBtn(){
        UIStarter<LimitFXController> limitFXControllerUIStarter = new UIStarter<LimitFXController>() {
            @Override
            public void controllerSetting(LimitFXController controller, Stage primaryStage) {}
        };
        limitFXControllerUIStarter.start("limit-window.fxml", "Управление лимитами");
    }

    private void handleFinanceControlBtn(){
        PaymentFXController paymentFXController = this;
        UIStarter<PaymentDetailsFXController> paymentDetailsFXControllerUIStarter = new UIStarter<PaymentDetailsFXController>() {
            @Override
            public void controllerSetting(PaymentDetailsFXController controller, Stage primaryStage) {
                controller.setPaymentFXController(paymentFXController);
                controller.init();
            }
        };
        paymentDetailsFXControllerUIStarter.start("payment-details-window.fxml", "Управление финансами");
    }

    private void handleOperationsControlBtn(){
        PaymentFXController paymentFXController = this;
        UIStarter<OperationFXController> operationFXControllerUIStarter = new UIStarter<OperationFXController>() {
            @Override
            public void controllerSetting(OperationFXController controller, Stage primaryStage) {
                controller.setPrimaryStage(primaryStage);
                controller.setPaymentFXController(paymentFXController);
                controller.init();
            }
        };
        operationFXControllerUIStarter.start("operation-window.fxml", "Управление операциями");
    }

    private void handleIncreaseRB(){
        if(increaseRB.isSelected()){
            descendingRB.setSelected(false);
        } else {
            increaseRB.setSelected(true);
        }
        displayOperations();
    }

    private void handleDescendingRB(){
        if(descendingRB.isSelected()){
            increaseRB.setSelected(false);
        } else {
            descendingRB.setSelected(true);
        }
        displayOperations();
    }

    private void handleImageCard(){
        Payment payment = cardBox.getValue();
        displayBalance(payment, cardBalance);
        displayOperations(payment, CARD_TEXT);
    }

    private void handleImageCash(){
        Payment payment = cashBox.getValue();
        displayBalance(payment, cashBalance);
        displayOperations(payment, CASH_TEXT);
    }

    private void updateCardAndCashCB(){
        cardBox.getItems().clear();
        cardBox.getItems().addAll(paymentService.getAllByType(PaymentType.CARD));
        cashBox.getItems().clear();
        cashBox.getItems().addAll(paymentService.getAllByType(PaymentType.CASH));
    }

    private void displayBalance(Payment payment, Label balance){
        if(payment == null){
            balance.setText("0" + UIUtils.RUB);
            return;
        }
        balance.setText(payment.getBalance() + UIUtils.RUB);
    }

    private void displayOperations(Payment payment, String paymentTypeText){
        paymentType.setText(paymentTypeText);
        operationList.getItems().clear();
        if(payment != null){
            operationList.getItems()
                    .addAll(operationService.getOperationsByPayment(payment, increaseRB.isSelected()));
        }
    }

    private void displayOperations(){
        String paymentTypeText = paymentType.getText();
        if(CARD_TEXT.equals(paymentTypeText)){
            displayOperations(cardBox.getValue(), CARD_TEXT);
        } else {
            displayOperations(cashBox.getValue(), CASH_TEXT);
        }
    }

    void updateAllAfterOperation(){
        displayBalance(cardBox.getValue(), cardBalance);
        displayBalance(cashBox.getValue(), cashBalance);
        displayOperations();
    }

    void updateAllAfterUpdatePayments(){
        updateCardAndCashCB();
        updateAllAfterOperation();
    }
}
