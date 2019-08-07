package org.eugene.cost.ui.payment;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.eugene.cost.data.OperationType;
import org.eugene.cost.data.model.payment.Bank;

public class OperationFXController {
    @FXML
    private ComboBox<Bank> paymentSystemOne;
    @FXML
    private ComboBox<Bank> paymentSystemTwo;
    @FXML
    private ComboBox<OperationType> operation;
    @FXML
    private Label paymentSystemTwoInfo;

    @FXML
    private TextField paySum;
    @FXML
    private TextArea descriptionOperation;

    @FXML
    private Button ok;
    @FXML
    private Button cancel;

    @FXML
    private DatePicker datePicker;
}
