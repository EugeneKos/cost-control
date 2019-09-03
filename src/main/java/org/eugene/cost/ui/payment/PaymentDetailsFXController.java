package org.eugene.cost.ui.payment;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import org.eugene.cost.data.Payment;

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
}
