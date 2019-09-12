package org.eugene.cost.ui.payment;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import org.eugene.cost.config.SpringContext;
import org.eugene.cost.data.Payment;
import org.eugene.cost.data.PaymentType;
import org.eugene.cost.service.IPaymentService;
import org.eugene.cost.service.util.PaymentUtils;
import org.eugene.cost.ui.common.UIUtils;

public class PaymentDetailsFXController {
    @FXML
    private ListView<Payment> cardList;
    @FXML
    private ListView<Payment> cashList;

    @FXML
    private TextField cardNumber;
    @FXML
    private TextField cashDescription;

    @FXML
    private TextField cardBalance;
    @FXML
    private TextField cashBalance;

    @FXML
    private Button addCardBtn;
    @FXML
    private Button removeCardBtn;

    @FXML
    private Button addCashBtn;
    @FXML
    private Button removeCashBtn;

    @FXML
    private DatePicker createCardDate;
    @FXML
    private DatePicker createCashDate;

    private PaymentFXController paymentFXController;

    private IPaymentService paymentService;

    private Payment currentCard;
    private Payment currentCash;

    void init(){
        paymentService = SpringContext.getBean(IPaymentService.class);

        updateCardList();
        updateCashList();

        cardList.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    currentCard = newValue;
                    displayCardOrCashInformation(newValue, cardNumber, cardBalance, createCardDate);
                });

        cashList.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    currentCash = newValue;
                    displayCardOrCashInformation(newValue, cashDescription, cashBalance, createCashDate);
                });

        addCardBtn.setOnAction(event -> handleAddCardBtn());
        addCashBtn.setOnAction(event -> handleAddCashBtn());

        removeCardBtn.setOnAction(event -> handleRemoveCardBtn());
        removeCashBtn.setOnAction(event -> handleRemoveCashBtn());
    }

    void setPaymentFXController(PaymentFXController paymentFXController) {
        this.paymentFXController = paymentFXController;
    }

    private void updateCardList(){
        cardList.getItems().clear();
        cardList.getItems().addAll(paymentService.getAllByType(PaymentType.CARD));
    }

    private void updateCashList(){
        cashList.getItems().clear();
        cashList.getItems().addAll(paymentService.getAllByType(PaymentType.CASH));
    }

    private void handleAddCardBtn(){
        String cardNumberText = cardNumber.getText();
        String cardBalanceText = cardBalance.getText();
        Payment payment = createPayment(cardNumberText, cardBalanceText, PaymentType.CARD);
        if(payment == null){
            return;
        }
        if(cardList.getItems().contains(payment)){
            return;
        }
        updateCardList();
        paymentFXController.updateAllAfterUpdatePayments();
    }

    private void handleAddCashBtn(){
        String cashDescriptionText = cashDescription.getText();
        String cashBalanceText = cashBalance.getText();
        Payment payment = createPayment(cashDescriptionText, cashBalanceText, PaymentType.CASH);
        if(payment == null){
            return;
        }
        if(cashList.getItems().contains(payment)){
            return;
        }
        updateCashList();
        paymentFXController.updateAllAfterUpdatePayments();
    }

    private void handleRemoveCardBtn(){
        if(currentCard == null){
            return;
        }
        paymentService.delete(currentCard);
        updateCardList();
        paymentFXController.updateAllAfterUpdatePayments();
    }

    private void handleRemoveCashBtn(){
        if(currentCash == null){
            return;
        }
        paymentService.delete(currentCash);
        updateCashList();
        paymentFXController.updateAllAfterUpdatePayments();
    }

    private void displayCardOrCashInformation(Payment payment, TextField paymentIdentify,
                                              TextField balanceLabel, DatePicker dateOfCreate){
        if(payment == null){
            paymentIdentify.setText("");
            balanceLabel.setText("");
            dateOfCreate.setValue(null);
        } else {
            paymentIdentify.setText(payment.getIdentify());
            balanceLabel.setText(payment.getBalance());
            dateOfCreate.setValue(payment.getDateOfCreation());
        }
    }

    private Payment createPayment(String identify, String balance, PaymentType paymentType){
        if(!PaymentUtils.isValidIdentify(identify)){
            return null;
        }
        balance = UIUtils.deleteSpace(balance);
        if(!UIUtils.isContainsNumbers(balance)){
            return null;
        }
        return paymentService.create(identify, balance, paymentType);
    }
}
