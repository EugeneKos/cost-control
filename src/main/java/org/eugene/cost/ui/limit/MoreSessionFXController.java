package org.eugene.cost.ui.limit;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.eugene.cost.data.BuyCategories;

public class MoreSessionFXController {
    @FXML
    private ListView<String> dayList;
    @FXML
    private ListView<String> buyList;

    @FXML
    private TextArea descriptionBuy;

    @FXML
    private Button close;

    @FXML
    private Label totalPrice;
    @FXML
    private Label currentDay;
    @FXML
    private Label totalLimitPrice;

    @FXML
    private RadioButton limitedBuys;
    @FXML
    private RadioButton nonLimitedBuys;

    @FXML
    private ComboBox<BuyCategories> buyCategories;
}
