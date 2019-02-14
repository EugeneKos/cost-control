package org.eugene.cost.ui.payment;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import org.eugene.cost.logic.model.payment.op.Operation;

import java.util.List;

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

    private List<Operation> historyOperations;

    public void initialize(List<Operation> historyOperations){
        this.historyOperations = historyOperations;
        initRB();
        showHistory.setOnAction(this::handleShowHistoryBtn);
        increase.setOnAction(this::handleIncreaseRB);
        descending.setOnAction(this::handleDescendingRB);
        showOperationHistory();
    }

    private void initRB(){
        increase.setSelected(false);
        descending.setSelected(true);
    }

    private void showOperationHistory(){
        operations.getItems().clear();
        sortingOperationHistory(historyOperations, increase, descending);
        for (Operation operation : historyOperations){
            if(beginDate.getValue() == null || finalDate.getValue() == null){
                operations.getItems().add(operation);
            } else if((beginDate.getValue().isBefore(operation.getDate()) || beginDate.getValue().isEqual(operation.getDate()))
                    && (finalDate.getValue().isAfter(operation.getDate()) || finalDate.getValue().isEqual(operation.getDate()))) {
                operations.getItems().add(operation);
            }
        }
    }

    static void sortingOperationHistory(List<Operation> historyOperations, RadioButton increase, RadioButton descending) {
        historyOperations.sort((op1, op2) -> {
            if (op1.getDate().isAfter(op2.getDate())) {
                if(increase.isSelected()){
                    return 1;
                }
                return -1;
            } else if (op1.getDate().isBefore(op2.getDate())) {
                if(descending.isSelected()){
                    return 1;
                }
                return -1;
            }
            return 0;
        });
    }

    private void handleShowHistoryBtn(ActionEvent event){
        showOperationHistory();
    }

    private void handleIncreaseRB(ActionEvent event){
        if(increase.isSelected()){
            descending.setSelected(false);
            showOperationHistory();
        } else {
            increase.setSelected(true);
        }
    }

    private void handleDescendingRB(ActionEvent event){
        if(descending.isSelected()){
            increase.setSelected(false);
            showOperationHistory();
        } else {
            descending.setSelected(true);
        }
    }
}
