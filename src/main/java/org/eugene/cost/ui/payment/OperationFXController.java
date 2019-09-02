package org.eugene.cost.ui.payment;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import org.eugene.cost.data.OperationType;
import org.eugene.cost.data.Payment;

public class OperationFXController {
    @FXML
    private ComboBox<Payment> paymentOne;
    @FXML
    private ComboBox<Payment> paymentTwo;
    @FXML
    private ComboBox<OperationType> operationTypeCB;
    @FXML
    private Label paymentTwoInfo;

    @FXML
    private TextField transactionAmount;
    @FXML
    private TextArea operationDescription;

    @FXML
    private Button okBtn;
    @FXML
    private Button cancelBtn;

    @FXML
    private DatePicker dateOfOperation;
}
