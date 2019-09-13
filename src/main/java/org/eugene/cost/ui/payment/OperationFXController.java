package org.eugene.cost.ui.payment;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
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
import org.eugene.cost.ui.common.MessageType;
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

        dateOfOperation.setDayCellFactory(param -> colorHandleDateOfOperation());

        paymentOne.getItems().addAll(paymentService.getAll());

        paymentOne.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    initPaymentTwo(newValue);
                    dateOfOperation.setValue(null);
                });

        paymentTwo.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> dateOfOperation.setValue(null));

        operationTypeCB.getItems().addAll(OperationType.values());

        operationTypeCB.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    transferSelected(newValue == OperationType.TRANSFER);
                    dateOfOperation.setValue(null);
                });

        okBtn.setOnAction(event -> handleOkBtn());
        cancelBtn.setOnAction(event -> handleCancelBtn());

    }

    void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    void setPaymentFXController(PaymentFXController paymentFXController) {
        this.paymentFXController = paymentFXController;
    }

    private DateCell colorHandleDateOfOperation() {
        return new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                OperationType operationTypeCBValue = operationTypeCB.getValue();

                if(operationTypeCBValue == null
                        || (operationTypeCBValue == OperationType.TRANSFER && paymentTwo.getValue() == null)
                        || paymentOne.getValue() == null){

                    setDisable(true);
                    setStyle(UIUtils.RED_COLOR);
                    return;
                }

                LocalDate startDate;

                if(operationTypeCBValue != OperationType.TRANSFER){
                    startDate = paymentOne.getValue().getDateOfCreation();
                } else {
                    startDate = paymentOne.getValue().getDateOfCreation()
                            .isAfter(paymentTwo.getValue().getDateOfCreation())
                            ? paymentOne.getValue().getDateOfCreation()
                            : paymentTwo.getValue().getDateOfCreation();
                }

                if (item.isBefore(startDate) || item.isAfter(LocalDate.now())) {
                    setDisable(true);
                    setStyle(UIUtils.RED_COLOR);
                }
            }
        };
    }

    private void handleOkBtn(){
        String transactionAmountText = transactionAmount.getText();
        if(!UIUtils.isContainsNumbers(transactionAmountText)){
            UIUtils.showOptionPane("Сумма операции заполнена некорректно!",
                    "Ошибка", MessageType.ERROR);
            return;
        }

        Payment paymentOneValue = paymentOne.getValue();
        if(paymentOneValue == null){
            UIUtils.showOptionPane("Платежная система 1 не выбрана!",
                    "Предупреждение", MessageType.WARNING);
            return;
        }

        Payment paymentTwoValue = paymentTwo.getValue();

        OperationType operationTypeValue = operationTypeCB.getValue();
        if(operationTypeValue == null){
            UIUtils.showOptionPane("Тип операции не выбран!",
                    "Предупреждение", MessageType.WARNING);
            return;
        }
        if(operationTypeValue == OperationType.TRANSFER){
            if(paymentTwoValue == null){
                UIUtils.showOptionPane("Платежная система 2 не выбрана!",
                        "Предупреждение", MessageType.WARNING);
                return;
            }
        }

        String operationDescriptionText = operationDescription.getText();
        if(operationTypeValue != OperationType.TRANSFER && StringUtils.isEmpty(operationDescriptionText)){
            UIUtils.showOptionPane("Описание операции должно быть заполнено!",
                    "Предупреждение", MessageType.WARNING);
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
            UIUtils.showOptionPane(e.getMessage(), "Ошибка", MessageType.ERROR);
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
