package org.eugene.cost.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import org.eugene.cost.logic.model.Buy;
import org.eugene.cost.logic.model.Day;
import org.eugene.cost.logic.model.Session;

public class MoreSessionFxController {
    @FXML
    private ListView<String> dayList;
    @FXML
    private ListView<String> buyList;

    @FXML
    private TextArea descriptionBuy;

    @FXML
    private Button close;

    private Stage stage;

    private Session session;

    private int currentDayIntoDayList = -1;

    public void init(){
        updateDayList();
        dayList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            currentDayIntoDayList = dayList.getSelectionModel().getSelectedIndex();
            updateBuyList(currentDayIntoDayList);
        });
        buyList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            displayBuyDescription(buyList.getSelectionModel().getSelectedIndex());
        });
        close.setOnAction(this::handleCloseBtn);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    private void handleCloseBtn(ActionEvent event){
        stage.close();
    }

    private void updateDayList(){
        dayList.getItems().clear();
        for (Day day : session.getDayList()){
            dayList.getItems().add(day.getDate().toString());
        }
    }

    private void updateBuyList(int index){
        buyList.getItems().clear();
        for (Buy buy : session.getDayList().get(index).getBuyList()) {
            buyList.getItems().add(buy.toString());
        }
    }

    private void displayBuyDescription(int index){
        if (index < 0) {
            descriptionBuy.setText("");
            return;
        }
        descriptionBuy.setText(session.getDayList().get(currentDayIntoDayList).getBuyList().get(index).getDescriptionBuy());
    }
}
