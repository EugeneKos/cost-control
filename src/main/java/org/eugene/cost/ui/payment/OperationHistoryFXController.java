package org.eugene.cost.ui.payment;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;

import org.eugene.cost.data.Operation;

public class OperationHistoryFXController {
    @FXML
    private ListView<Operation> operationList;

    @FXML
    private DatePicker beginHistoriesDate;
    @FXML
    private DatePicker finalHistoriesDate;

    @FXML
    private Button showHistoryBtn;

    @FXML
    private RadioButton increaseRB;
    @FXML
    private RadioButton descendingRB;
}
