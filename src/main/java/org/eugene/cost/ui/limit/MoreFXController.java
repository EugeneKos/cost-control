package org.eugene.cost.ui.limit;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.eugene.cost.logic.model.card.bank.Bank;
import org.eugene.cost.logic.model.card.op.Debit;
import org.eugene.cost.logic.model.card.op.Enrollment;
import org.eugene.cost.logic.model.limit.Buy;
import org.eugene.cost.logic.util.StringUtil;
import org.eugene.cost.ui.card.BankFXController;

import javax.swing.*;
import java.util.Set;

public class MoreFXController {
    @FXML
    private TextField buyPrice;
    @FXML
    private TextField shopOrPlaceBuy;

    @FXML
    private TextArea descriptionBuy;

    @FXML
    private Button addBuy;
    @FXML
    private Button cancel;

    @FXML
    private CheckBox nonLimited;

    @FXML
    private ComboBox<Bank> paymentSystem;

    private LimitFXController limitFXController;

    private Stage stage;

    private Buy currentBuy;

    private BankFXController bankFXController;

    private Set<Bank> banks;
    private Bank currentBank;


    public void init(){
        initPaymentSystem();
        if(currentBuy != null){
            addBuy.setText("Изменить");
            addBuy.setLayoutX(315);
            buyPrice.setText(currentBuy.getPrice());
            shopOrPlaceBuy.setText(currentBuy.getShopOrPlaceBuy());
            descriptionBuy.setText(currentBuy.getDescriptionBuy());
            nonLimited.setSelected(!currentBuy.isLimited());
            initPaymentSystem(currentBuy.getPayment());
            paymentSystem.setDisable(true);
            currentBank = initCurrentBank(currentBuy.getPayment());
            if(currentBank == null){
                addBuy.setDisable(true);
                JOptionPane.showMessageDialog(null,
                        "Платежная система по данной покупке не найдена.\n" +
                                "Изменить покупку невозможно!", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        }
        paymentSystem.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> currentBank = newValue);
        addBuy.setOnAction(this::handleAddBuyBtn);
        cancel.setOnAction(this::handleCancelBtn);
    }

    public void setLimitFXController(LimitFXController limitFXController) {
        this.limitFXController = limitFXController;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setCurrentBuy(Buy currentBuy) {
        this.currentBuy = currentBuy;
    }

    public void setBanks(Set<Bank> banks) {
        this.banks = banks;
    }

    public void setBankFXController(BankFXController bankFXController) {
        this.bankFXController = bankFXController;
    }

    private void initPaymentSystem(){
        for (Bank bank : banks){
            paymentSystem.getItems().add(bank);
        }
    }

    private void initPaymentSystem(Bank payment){
        for (Bank bank : banks){
            if(bank.equals(payment)){
                paymentSystem.setValue(bank);
            }
        }
    }

    private Bank initCurrentBank(Bank payment){
        for (Bank bank : banks){
            if(bank.equals(payment)){
                return bank;
            }
        }
        return null;
    }

    private void handleAddBuyBtn(ActionEvent event){
        String price = StringUtil.deleteSpace(buyPrice.getText());
        if(!StringUtil.checkSequence(price)){
            JOptionPane.showMessageDialog(null,
                    "Ошибка заполнения цены!", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if(currentBank == null){
            JOptionPane.showMessageDialog(null,
                    "Платежная система не была выбрана", "Информация", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        Buy buy = new Buy(price,shopOrPlaceBuy.getText(),descriptionBuy.getText(),!nonLimited.isSelected(), currentBank);
        if(currentBuy == null){
            limitFXController.addBuyIntoCurrentDay(buy);
        } else {
            limitFXController.applyChangeBuyIntoCurrentDay(currentBuy,buy);
            currentBank.executeOperation(new Enrollment(currentBuy.getPrice(),
                    "Отмена списание средств [ "+currentBuy.getShopOrPlaceBuy()+" ]"));
        }
        currentBank.executeOperation(new Debit(price, "[ "+buy.getShopOrPlaceBuy()+" ]"));
        bankFXController.updateBalanceAndHistory();
        bankFXController.saveBanks();
        stage.close();
    }

    private void handleCancelBtn(ActionEvent event){
        stage.close();
    }
}
