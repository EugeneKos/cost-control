package org.eugene.cost.ui.limit;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.eugene.cost.config.SpringContext;
import org.eugene.cost.data.Payment;
import org.eugene.cost.data.Buy;
import org.eugene.cost.data.BuyCategories;
import org.eugene.cost.data.Day;
import org.eugene.cost.data.Session;
import org.eugene.cost.service.IBuyService;
import org.eugene.cost.service.ISessionService;
import org.eugene.cost.ui.common.UIUtils;

public class BuyFXController {
    @FXML
    private TextField buyPrice;
    @FXML
    private TextField shopOrPlaceBuy;

    @FXML
    private TextArea buyDescription;

    @FXML
    private Button addBuyBtn;
    @FXML
    private Button cancelBtn;

    @FXML
    private CheckBox nonLimitedCB;

    @FXML
    private ComboBox<Payment> paymentTypeCB;

    @FXML
    private ComboBox<BuyCategories> buyCategoriesCB;

    private Stage primaryStage;

    private LimitFXController limitFXController;

    private ISessionService sessionService;
    private IBuyService buyService;

    private Session currentSession;
    private Day currentDay;
    private Buy currentBuy;

    private BuyCategories buyCategory;

    private boolean changeBuy;


    void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    void setLimitFXController(LimitFXController limitFXController) {
        this.limitFXController = limitFXController;
    }

    void setCurrentSession(Session currentSession) {
        this.currentSession = currentSession;
    }

    void setCurrentDay(Day currentDay) {
        this.currentDay = currentDay;
    }

    void setCurrentBuy(Buy currentBuy) {
        this.currentBuy = currentBuy;
    }

    void setChangeBuy(boolean changeBuy) {
        this.changeBuy = changeBuy;
    }

    void init(){
        buyService = SpringContext.getBean(IBuyService.class);
        sessionService = SpringContext.getBean(ISessionService.class);

        buyCategoriesCB.getItems().addAll(BuyCategories.values());
        buyCategoriesCB.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> buyCategory = newValue);

        initBtnHandles();
    }

    private void initBtnHandles(){
        if(changeBuy){
            addBuyBtn.setText("Изменить");
            addBuyBtn.setLayoutX(315);
            addBuyBtn.setOnAction(event -> handleChangeBuyBtn());
            displayCurrentBuy();
        } else {
            addBuyBtn.setOnAction(event -> handleAddBuyBtn());
        }

        cancelBtn.setOnAction(event -> handleCancelBtn());
    }

    private void displayCurrentBuy(){
        buyPrice.setText(currentBuy.getPrice());
        shopOrPlaceBuy.setText(currentBuy.getShopOrPlaceBuy());
        buyDescription.setText(currentBuy.getDescriptionBuy());
        nonLimitedCB.setSelected(!currentBuy.isLimited());
        buyCategoriesCB.setValue(currentBuy.getBuyCategories());
    }

    private void handleAddBuyBtn(){
        if(isNonValidated()){
            return;
        }
        addBuy();
        sessionService.update(currentSession);
        primaryStage.close();
    }

    private void handleChangeBuyBtn(){
        if(isNonValidated()){
            return;
        }
        buyService.removeBuy(currentBuy, currentDay, currentSession);
        addBuy();
        sessionService.update(currentSession);
        primaryStage.close();
    }

    private void handleCancelBtn(){
        primaryStage.close();
    }

    private void addBuy(){
        buyService.addBuy(buyPrice.getText(), shopOrPlaceBuy.getText(), buyDescription.getText(),
                !nonLimitedCB.isSelected(), buyCategory, currentDay, currentSession);

        limitFXController.displayLimitsAndCost();
        limitFXController.displayBuyList();
    }

    private boolean isNonValidated(){
        if(UIUtils.isNull(buyPrice.getText(), shopOrPlaceBuy.getText(), buyDescription.getText())
                || buyCategory == null){
            return true;
        }
        buyPrice.setText(UIUtils.deleteSpace(buyPrice.getText()));
        return !UIUtils.isContainsNumbers(buyPrice.getText());
    }
}
