package org.eugene.cost.ui.payment;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.eugene.cost.data.Card;
import org.eugene.cost.data.Cash;

public class FinanceFXController {
    @FXML
    private ListView<Card> cards;
    @FXML
    private ListView<Cash> cashes;

    @FXML
    private TextField cardNumber;
    @FXML
    private TextField cashDescription;

    @FXML
    private TextField cardBalance;
    @FXML
    private TextField cashBalance;

    @FXML
    private Button addCard;
    @FXML
    private Button removeCard;

    @FXML
    private Button addCash;
    @FXML
    private Button removeCash;

    @FXML
    private DatePicker cardsDate;
    @FXML
    private DatePicker cashesDate;
}
