package org.eugene.cost.ui.limit;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.eugene.cost.logic.model.limit.Buy;
import org.eugene.cost.logic.util.StringUtil;

import javax.swing.*;

public class MoreFXController {
    @FXML
    private TextField buyPrice;
    @FXML
    private TextField shopOrPlaceBuy;

    @FXML
    private TextArea descriptionBuy;

    @FXML
    private Button addBuy;
    @FXML
    private Button cancel;

    @FXML
    private CheckBox nonLimited;

    private LimitFXController limitFXController;

    private Stage stage;

    private Buy currentBuy;


    public void init(){
        if(currentBuy != null){
            addBuy.setText("Изменить");
            addBuy.setLayoutX(275);
            buyPrice.setText(currentBuy.getPrice());
            shopOrPlaceBuy.setText(currentBuy.getShopOrPlaceBuy());
            descriptionBuy.setText(currentBuy.getDescriptionBuy());
            nonLimited.setSelected(!currentBuy.isLimited());
        }
        addBuy.setOnAction(this::handleAddBuyBtn);
        cancel.setOnAction(this::handleCancelBtn);
    }

    public void setLimitFXController(LimitFXController limitFXController) {
        this.limitFXController = limitFXController;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setCurrentBuy(Buy currentBuy) {
        this.currentBuy = currentBuy;
    }

    private void handleAddBuyBtn(ActionEvent event){
        String price = StringUtil.deleteSpace(buyPrice.getText());
        if(!StringUtil.checkSequence(price)){
            JOptionPane.showMessageDialog(null, "Ошибка заполнения цены!", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Buy buy = new Buy(price,shopOrPlaceBuy.getText(),descriptionBuy.getText(),!nonLimited.isSelected());
        if(currentBuy == null){
            limitFXController.addBuyIntoCurrentDay(buy);
        } else {
            limitFXController.applyChangeBuyIntoCurrentDay(currentBuy,buy);
        }
        stage.close();
    }

    private void handleCancelBtn(ActionEvent event){
        stage.close();
    }
}
