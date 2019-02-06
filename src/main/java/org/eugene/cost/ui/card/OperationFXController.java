package org.eugene.cost.ui.card;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.eugene.cost.logic.model.card.bank.Bank;
import org.eugene.cost.logic.model.card.op.Debit;
import org.eugene.cost.logic.model.card.op.Enrollment;
import org.eugene.cost.logic.model.card.op.Operations;
import org.eugene.cost.logic.util.StringUtil;

import javax.swing.*;
import java.util.Set;

public class OperationFXController {
    @FXML
    private ComboBox<Bank> paymentSystemOne;
    @FXML
    private ComboBox<Bank> paymentSystemTwo;
    @FXML
    private ComboBox<Operations> operation;
    @FXML
    private Label paymentSystemTwoInfo;

    @FXML
    private TextField paySum;
    @FXML
    private TextArea descriptionOperation;

    @FXML
    private Button ok;
    @FXML
    private Button cancel;

    private Stage stage;

    private BankFXController bankFXController;

    private Bank bankOne;
    private Bank bankTwo;

    private Operations op;

    public void initialize(Set<Bank> banks){
        initMainPaymentSystemAndOperations(banks);
        paymentSystemOne.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            bankOne = newValue;
            initPaymentSystemToTransfer(banks);
        });
        paymentSystemTwo.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> bankTwo = newValue);

        operation.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            op = newValue;
            if(op == Operations.TRANSFER){
                paymentSystemTwo.setVisible(true);
                paymentSystemTwoInfo.setVisible(true);
                descriptionOperation.setDisable(true);
                initPaymentSystemToTransfer(banks);
            } else {
                paymentSystemTwo.setVisible(false);
                paymentSystemTwoInfo.setVisible(false);
                descriptionOperation.setDisable(false);
            }
        });

        ok.setOnAction(this::handleOkBtn);
        cancel.setOnAction(this::handleCancelBtn);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setBankFXController(BankFXController bankFXController) {
        this.bankFXController = bankFXController;
    }

    private void initMainPaymentSystemAndOperations(Set<Bank> banks){
        for (Bank bank : banks){
            paymentSystemOne.getItems().add(bank);
        }
        for (Operations operation : Operations.values()){
            this.operation.getItems().add(operation);
        }
    }

    private void initPaymentSystemToTransfer(Set<Bank> banks){
        paymentSystemTwo.getItems().clear();
        for (Bank bank : banks){
            if(!bank.equals(bankOne)){
                paymentSystemTwo.getItems().add(bank);
            }
        }
    }

    private void handleOkBtn(ActionEvent event){
        if(bankOne == null) {
            JOptionPane.showMessageDialog(null,"Платежная система не была выбрана", "Информация", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        if(op == null){
            JOptionPane.showMessageDialog(null,"Операция не была выбрана", "Информация", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        if(!checkTextPay()){
            JOptionPane.showMessageDialog(null,"Не заполнена или неправильно заполнена сумма операции", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }
        switch (op){
            case ENROLLMENT:
                bankOne.executeOperation(new Enrollment(StringUtil.deleteSpace(paySum.getText()),descriptionOperation.getText()));
                break;
            case DEBIT:
                bankOne.executeOperation(new Debit(StringUtil.deleteSpace(paySum.getText()),descriptionOperation.getText()));
                break;
            case TRANSFER:
                if(bankTwo == null) {
                    JOptionPane.showMessageDialog(null,"Платежная система не была выбрана", "Информация", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                bankOne.executeOperation(new Debit(StringUtil.deleteSpace(paySum.getText()),"Transfer"));
                bankTwo.executeOperation(new Enrollment(StringUtil.deleteSpace(paySum.getText()),"Transfer"));
                break;
        }
        bankFXController.updateBalanceAndHistory();
        bankFXController.saveBanks();
        stage.close();
    }

    private boolean checkTextPay(){
        if(paySum.getText() == null){
            return false;
        }
        return StringUtil.checkSequence(StringUtil.deleteSpace(paySum.getText()));
    }

    private void handleCancelBtn(ActionEvent event){
        stage.close();
    }
}
