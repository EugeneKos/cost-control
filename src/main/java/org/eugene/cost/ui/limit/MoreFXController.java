package org.eugene.cost.ui.limit;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.eugene.cost.logic.model.limit.Day;
import org.eugene.cost.logic.model.payment.bank.Bank;
import org.eugene.cost.logic.model.payment.op.Debit;
import org.eugene.cost.logic.model.payment.op.Enrollment;
import org.eugene.cost.logic.model.limit.Buy;
import org.eugene.cost.logic.model.limit.BuyCategories;
import org.eugene.cost.logic.util.StringUtil;
import org.eugene.cost.ui.payment.BankFXController;

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

    @FXML
    private ComboBox<BuyCategories> buyCategories;

    private LimitFXController limitFXController;

    private Stage stage;

    private Day currentDay;

    private Buy currentBuy;

    private BankFXController bankFXController;

    private Set<Bank> banks;
    private Bank currentBank;

    private BuyCategories buyCategory;


    public void init(){
        initPaymentSystem();
        initBuyCategories();
        if(currentBuy != null){
            addBuy.setText("Изменить");
            addBuy.setLayoutX(315);
            buyPrice.setText(currentBuy.getPrice());
            shopOrPlaceBuy.setText(currentBuy.getShopOrPlaceBuy());
            descriptionBuy.setText(currentBuy.getDescriptionBuy());
            nonLimited.setSelected(!currentBuy.isLimited());
            buyCategories.setValue(currentBuy.getBuyCategories());
            initPaymentSystem(currentBuy.getPayment());
            paymentSystem.setDisable(true);
            currentBank = initCurrentBank(currentBuy.getPayment());
            buyCategory = currentBuy.getBuyCategories();
            if(currentBank == null){
                addBuy.setDisable(true);
                JOptionPane.showMessageDialog(null,
                        "Платежная система по данной покупке не найдена.\n" +
                                "Изменить покупку невозможно!", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        }
        paymentSystem.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> currentBank = newValue);
        buyCategories.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> buyCategory = newValue);
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

    public void setCurrentDay(Day currentDay) {
        this.currentDay = currentDay;
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

    private void initBuyCategories(){
        for (BuyCategories buyCategory : BuyCategories.values()){
            this.buyCategories.getItems().add(buyCategory);
        }
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
        if(buyCategory == null){
            JOptionPane.showMessageDialog(null,
                    "Категория покупки не была выбрана", "Информация", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        Buy buy = new Buy(price,shopOrPlaceBuy.getText(),descriptionBuy.getText(),!nonLimited.isSelected(), currentBank, buyCategory);
        if(currentBuy == null){
            limitFXController.addBuyIntoCurrentDay(buy);
            currentBank.executeOperation(new Debit(price, "[ "+buy.getShopOrPlaceBuy()+" ] "+buy.getDescriptionBuy(), currentDay.getDate()));
        } else {
            limitFXController.applyChangeBuyIntoCurrentDay(currentBuy,buy);
            if(!currentBuy.getPrice().equals(buy.getPrice())){
                currentBank.executeOperation(new Enrollment(currentBuy.getPrice(),
                        "Отмена списание средств [ "+currentBuy.getShopOrPlaceBuy()+" ] "+buy.getDescriptionBuy(), currentDay.getDate()));
                currentBank.executeOperation(new Debit(price, "[ "+buy.getShopOrPlaceBuy()+" ] "+buy.getDescriptionBuy(), currentDay.getDate()));
            }
        }
        bankFXController.updateBalanceAndHistory();
        bankFXController.saveBanks();
        stage.close();
    }

    private void handleCancelBtn(ActionEvent event){
        stage.close();
    }
}
