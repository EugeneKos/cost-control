package org.eugene.cost.ui.payment;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import org.apache.log4j.Logger;
import org.eugene.cost.config.SpringContext;
import org.eugene.cost.data.OperationType;
import org.eugene.cost.data.Payment;
import org.eugene.cost.data.PaymentOperation;
import org.eugene.cost.exeption.NotEnoughMoneyException;
import org.eugene.cost.service.IOperationService;
import org.eugene.cost.service.IPaymentService;
import org.eugene.cost.ui.common.UIUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.Set;

public class OperationFXController {
    private static Logger LOGGER = Logger.getLogger(OperationFXController.class);

    @FXML
    private ComboBox<Payment> paymentOne;
    @FXML
    private ComboBox<Payment> paymentTwo;
    @FXML
    private ComboBox<OperationType> operationTypeCB;
    @FXML
    private Label paymentTwoInfo;

    @FXML
    private TextField transactionAmount;
    @FXML
    private TextArea operationDescription;

    @FXML
    private Button okBtn;
    @FXML
    private Button cancelBtn;

    @FXML
    private DatePicker dateOfOperation;

    private IPaymentService paymentService;
    private IOperationService operationService;

    private Stage primaryStage;

    private PaymentFXController paymentFXController;

    void init(){
        paymentService = SpringContext.getBean(IPaymentService.class);
        operationService = SpringContext.getBean(IOperationService.class);

        paymentOne.getItems().addAll(paymentService.getAll());

        paymentOne.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> initPaymentTwo(newValue));

        operationTypeCB.getItems().addAll(OperationType.values());

        operationTypeCB.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) ->
                        transferSelected(newValue == OperationType.TRANSFER));

        okBtn.setOnAction(event -> handleOkBtn());
        cancelBtn.setOnAction(event -> handleCancelBtn());

    }

    void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    void setPaymentFXController(PaymentFXController paymentFXController) {
        this.paymentFXController = paymentFXController;
    }

    private void handleOkBtn(){
        String transactionAmountText = transactionAmount.getText();
        if(!UIUtils.isContainsNumbers(transactionAmountText)){
            return;
        }

        Payment paymentOneValue = paymentOne.getValue();
        if(paymentOneValue == null){
            return;
        }

        Payment paymentTwoValue = paymentTwo.getValue();

        OperationType operationTypeValue = operationTypeCB.getValue();
        if(operationTypeValue == null){
            return;
        }
        if(operationTypeValue == OperationType.TRANSFER){
            if(paymentTwoValue == null){
                return;
            }
        }

        String operationDescriptionText = operationDescription.getText();
        if(operationTypeValue != OperationType.TRANSFER && StringUtils.isEmpty(operationDescriptionText)){
            return;
        }

        LocalDate dateOfOperationValue = dateOfOperation.getValue();

        try{
            if(dateOfOperationValue == null){
                operationService.create(new PaymentOperation(paymentOneValue, paymentTwoValue),
                        transactionAmountText, operationDescriptionText, operationTypeValue);
            } else {
                operationService.create(new PaymentOperation(paymentOneValue, paymentTwoValue),
                        transactionAmountText, operationDescriptionText, operationTypeValue, dateOfOperationValue);
            }
        } catch (NotEnoughMoneyException e){
            LOGGER.error(e);
            return;
        }

        paymentFXController.updateAllAfterOperation();
        primaryStage.close();
    }

    private void handleCancelBtn(){
        primaryStage.close();
    }

    private void initPaymentTwo(Payment exclude){
        Set<Payment> payments = paymentService.getAll();
        payments.remove(exclude);
        paymentTwo.getItems().clear();
        paymentTwo.getItems().addAll(payments);
    }

    private void transferSelected(boolean transferType){
        paymentTwoInfo.setVisible(transferType);
        paymentTwo.setVisible(transferType);
        operationDescription.setDisable(transferType);
    }
}
