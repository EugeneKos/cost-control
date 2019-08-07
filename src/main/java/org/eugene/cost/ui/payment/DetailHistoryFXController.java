package org.eugene.cost.ui.payment;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import org.eugene.cost.data.model.payment.Operation;

public class DetailHistoryFXController {
    @FXML
    private ListView<Operation> operations;

    @FXML
    private DatePicker beginDate;
    @FXML
    private DatePicker finalDate;

    @FXML
    private Button showHistory;

    @FXML
    private RadioButton increase;
    @FXML
    private RadioButton descending;
}
