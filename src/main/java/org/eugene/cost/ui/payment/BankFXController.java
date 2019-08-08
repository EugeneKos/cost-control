package org.eugene.cost.ui.payment;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import org.eugene.cost.data.Operation;
import org.eugene.cost.data.Card;
import org.eugene.cost.data.Cash;

public class BankFXController {
    @FXML
    private ListView<Operation> operations;

    @FXML
    private Button operationControl;
    @FXML
    private Button financeControl;
    @FXML
    private Button limitControl;
    @FXML
    private Button detailHistory;

    @FXML
    private Label cashBalance;
    @FXML
    private Label cardBalance;
    @FXML
    private Label bankType;

    @FXML
    private ImageView imageCard;
    @FXML
    private ImageView imageCash;
    @FXML
    private ImageView imageGraph;

    @FXML
    private ComboBox<Card> cardBox;
    @FXML
    private ComboBox<Cash> cashBox;

    @FXML
    private RadioButton increase;
    @FXML
    private RadioButton descending;
}
