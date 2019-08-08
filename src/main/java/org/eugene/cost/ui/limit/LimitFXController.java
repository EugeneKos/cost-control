package org.eugene.cost.ui.limit;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.eugene.cost.data.Buy;

public class LimitFXController {
    @FXML
    private DatePicker beginDate;
    @FXML
    private DatePicker finalDate;
    @FXML
    private DatePicker currentDate;

    @FXML
    private ListView<Buy> buyList;

    @FXML
    private TextField sumLimit;
    @FXML
    private TextArea descriptionBuy;

    @FXML
    private Label currentLimit;
    @FXML
    private Label currentLimitDay;
    @FXML
    private Label currentRateDay;

    @FXML
    private Button start;
    @FXML
    private Button addBuy;
    @FXML
    private Button removeBuy;
    @FXML
    private Button moreAboutBuy;
    @FXML
    private Button closeDay;
    @FXML
    private Button resumeDay;
    @FXML
    private Button setting;

    @FXML
    private RadioButton limitedBuys;
    @FXML
    private RadioButton nonLimitedBuys;
}
