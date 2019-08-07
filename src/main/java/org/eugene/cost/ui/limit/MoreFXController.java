package org.eugene.cost.ui.limit;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.eugene.cost.data.model.payment.Bank;
import org.eugene.cost.data.BuyCategories;

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
}
