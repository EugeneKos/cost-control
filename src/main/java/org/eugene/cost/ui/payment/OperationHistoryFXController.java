package org.eugene.cost.ui.payment;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.stage.Stage;

import org.eugene.cost.config.SpringContext;
import org.eugene.cost.data.Operation;
import org.eugene.cost.data.OperationFilter;
import org.eugene.cost.data.Payment;
import org.eugene.cost.service.IOperationService;
import org.eugene.cost.service.util.DateUtils;
import org.eugene.cost.ui.common.UIUtils;

import java.time.LocalDate;
import java.util.List;

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

    private Stage primaryStage;

    private IOperationService operationService;

    private Payment payment;

    private String defaultTitle;

    void init(){
        operationService = SpringContext.getBean(IOperationService.class);

        increaseRB.setSelected(true);
        descendingRB.setSelected(false);

        displayOperationHistory();

        beginHistoriesDate.setDayCellFactory(param -> colorHandleDate());
        finalHistoriesDate.setDayCellFactory(param -> colorHandleDate());

        increaseRB.setOnAction(event -> handleIncreaseRB());
        descendingRB.setOnAction(event -> handleDescendingRB());

        showHistoryBtn.setOnAction(event -> handleShowHistoryBtn());
    }

    void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    void setPayment(Payment payment) {
        this.payment = payment;
    }

    private DateCell colorHandleDate() {
        return new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                if (item.isBefore(payment.getDateOfCreation()) || item.isAfter(LocalDate.now())) {
                    setDisable(true);
                    setStyle(UIUtils.RED_COLOR);
                }
            }
        };
    }

    private void handleIncreaseRB(){
        if(increaseRB.isSelected()){
            descendingRB.setSelected(false);
        } else {
            increaseRB.setSelected(true);
        }
    }

    private void handleDescendingRB(){
        if(descendingRB.isSelected()){
            increaseRB.setSelected(false);
        } else {
            descendingRB.setSelected(true);
        }
    }

    private void handleShowHistoryBtn(){
        displayOperationHistory();
        changeTitle();
        beginHistoriesDate.setValue(null);
        finalHistoriesDate.setValue(null);
    }

    private void displayOperationHistory(){
        OperationFilter filter = new OperationFilter(increaseRB.isSelected(),
                beginHistoriesDate.getValue(),
                finalHistoriesDate.getValue()
        );

        List<Operation> operationsByPayment = operationService
                .getOperationsByPayment(payment, filter);

        operationList.getItems().clear();
        operationList.getItems().addAll(operationsByPayment);
    }

    private void changeTitle(){
        if(defaultTitle == null){
            defaultTitle = primaryStage.getTitle();
        }

        String beginDate = beginHistoriesDate.getValue() == null
                ? DateUtils.dateToString(payment.getDateOfCreation())
                : DateUtils.dateToString(beginHistoriesDate.getValue());

        String finalDate = finalHistoriesDate.getValue() == null
                ? DateUtils.dateToString(LocalDate.now())
                : DateUtils.dateToString(finalHistoriesDate.getValue());

        String newTitle = defaultTitle + " Показ истории С " + beginDate + " ПО " + finalDate;
        primaryStage.setTitle(newTitle);
    }
}
