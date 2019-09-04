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

import java.time.LocalDate;

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

    private IPaymentService paymentService;

    void init(){
        paymentService = SpringContext.getBean(IPaymentService.class);

        cardList.getItems().addAll(paymentService.getAllByType(PaymentType.CARD));
        cashList.getItems().addAll(paymentService.getAllByType(PaymentType.CASH));

        addCardBtn.setOnAction(event -> handleAddCardBtn());
    }

    private void handleAddCardBtn(){
        // todo: Сделать проверку всех полей
        String cardNumberText = cardNumber.getText();
        String cardBalanceText = cardBalance.getText();
        LocalDate date = createCardDate.getValue();
        if(date == null){
            paymentService.create(cardNumberText, cardBalanceText, PaymentType.CARD);
        } else {
            paymentService.create(cardNumberText, cardBalanceText, PaymentType.CARD, date);
        }
    }
}
