package org.eugene.cost.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.eugene.cost.logic.model.Buy;
import org.eugene.cost.logic.model.Day;
import org.eugene.cost.logic.model.Session;
import org.eugene.cost.logic.util.Calculate;

import java.time.format.DateTimeFormatter;

public class MoreSessionFxController {
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
    private RadioButton limitedBuys;
    @FXML
    private RadioButton nonLimitedBuys;

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
        limitedBuys.setOnAction(this::handleLimitedRB);
        nonLimitedBuys.setOnAction(this::handleNonLimitedRB);
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
        if (index < 0) return;
        buyList.getItems().clear();
        String total = "0";
        for (Buy buy : session.getDayList().get(index).getBuyList()) {
            if(limitedBuys.isSelected()){
                if(buy.isLimited()){
                    buyList.getItems().add(buy.toString());
                    total = Calculate.plus(total,buy.getPrice());
                }
            }
            else if(nonLimitedBuys.isSelected()){
                if(!buy.isLimited()){
                    buyList.getItems().add(buy.toString());
                    total = Calculate.plus(total,buy.getPrice());
                }
            } else {
                buyList.getItems().add(buy.toString());
                total = Calculate.plus(total,buy.getPrice());
            }

        }
        currentDay.setText("Траты за " + session.getDayList().get(index).getDate().format(DateTimeFormatter.ofPattern("dd/MMM/yyyy")));
        totalPrice.setText(total + " Руб.");
    }

    private void displayBuyDescription(int index){
        if (index < 0) {
            descriptionBuy.setText("");
            return;
        }
        descriptionBuy.setText(session.getDayList().get(currentDayIntoDayList).getBuyList().get(index).getDescriptionBuy());
    }

    private void handleLimitedRB(ActionEvent event){
        if(limitedBuys.isSelected()){
            nonLimitedBuys.setSelected(false);
        }
        updateBuyList(currentDayIntoDayList);
    }

    private void handleNonLimitedRB(ActionEvent event){
        if(nonLimitedBuys.isSelected()){
            limitedBuys.setSelected(false);
        }
        updateBuyList(currentDayIntoDayList);
    }
}
