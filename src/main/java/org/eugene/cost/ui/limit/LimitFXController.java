package org.eugene.cost.ui.limit;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import org.eugene.cost.config.SpringContext;
import org.eugene.cost.data.Buy;
import org.eugene.cost.data.BuyFilter;
import org.eugene.cost.data.Day;
import org.eugene.cost.data.Session;
import org.eugene.cost.data.SessionDetail;
import org.eugene.cost.service.IBuyService;
import org.eugene.cost.service.IDayService;
import org.eugene.cost.service.ISessionService;
import org.eugene.cost.ui.common.UIStarter;
import org.eugene.cost.ui.common.UIUtils;

import java.time.LocalDate;

public class LimitFXController {
    @FXML
    private DatePicker beginDate;
    @FXML
    private DatePicker finalDate;
    @FXML
    private DatePicker currentDate;

    @FXML
    private ListView<Buy> buys;

    @FXML
    private TextField sumLimit;
    @FXML
    private TextArea descriptionBuy;

    @FXML
    private Label currentLimit;
    @FXML
    private Label currentLimitDay;
    @FXML
    private Label currentRateDay;

    @FXML
    private Button start;
    @FXML
    private Button addBuy;
    @FXML
    private Button removeBuy;
    @FXML
    private Button moreAboutBuy;
    @FXML
    private Button closeDay;
    @FXML
    private Button resumeDay;
    @FXML
    private Button setting;

    @FXML
    private RadioButton limitedBuys;
    @FXML
    private RadioButton nonLimitedBuys;

    private ISessionService sessionService;
    private IDayService dayService;
    private IBuyService buyService;

    private Session currentSession;
    private Day currentDay;
    private Buy currentBuy;

    @FXML
    public void initialize(){
        sessionService = SpringContext.getBean(ISessionService.class);
        dayService = SpringContext.getBean(IDayService.class);
        buyService = SpringContext.getBean(IBuyService.class);

        buys.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            currentBuy = newValue;
            displayBuyDescription();
        });

        beginDate.setDayCellFactory(param -> colorHandleBeginDate());
        finalDate.setDayCellFactory(param -> colorHandleFinalDate());
        currentDate.setDayCellFactory(param -> colorHandleCurrentDate());

        currentDate.setOnAction(event -> handleCurrentDate());

        initBtnHandles();

        disableBtnBeforeChooseSession(true);
    }

    void setCurrentSession(Session currentSession) {
        this.currentSession = currentSession;
    }

    private DateCell colorHandleBeginDate() {
        return new DateCell(){
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                if (item.isBefore(LocalDate.now())) {
                    setDisable(true);
                    setStyle(UIUtils.RED_COLOR);
                }
            }
        };
    }

    private DateCell colorHandleFinalDate() {
        return new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                if (item.isBefore(LocalDate.now()) || item.isEqual(LocalDate.now())) {
                    setDisable(true);
                    setStyle(UIUtils.RED_COLOR);
                }
            }
        };
    }

    private DateCell colorHandleCurrentDate() {
        return new DateCell(){
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                if (item.isBefore(beginDate.getValue()) || item.isAfter(finalDate.getValue())) {
                    setDisable(true);
                    setStyle(UIUtils.RED_COLOR);
                }
                else if(dayService.getDayByDate(currentSession, item).isClose()){
                    setStyle(UIUtils.GREEN_COLOR);
                }
            }
        };
    }

    private void handleCurrentDate() {
        LocalDate current = currentDate.getValue();
        currentDay = dayService.getDayByDate(currentSession, current);
        displayLimitsAndCost();
        displayBuyList();
        disableBtnAfterCloseDay(currentDay.isClose());
    }

    private void initBtnHandles(){
        start.setOnAction(event -> handleBtnStart());
        addBuy.setOnAction(event -> handleAddBuy(false));
        moreAboutBuy.setOnAction(event -> handleMoreAboutBuy());
        removeBuy.setOnAction(event -> handleBtnRemoveBuy());
        limitedBuys.setOnAction(event -> handleLimitedRB());
        nonLimitedBuys.setOnAction(event -> handleNonLimitedRB());
        closeDay.setOnAction(event -> handleBtnCloseDay());
        resumeDay.setOnAction(event -> handleBtnResumeDay());
        setting.setOnAction(event -> handleBtnSetting());
    }

    private void handleBtnStart(){
        if(UIUtils.isNull(sumLimit.getText()) || beginDate.getValue() == null || finalDate.getValue() == null){
            return;
        }
        sumLimit.setText(UIUtils.deleteSpace(sumLimit.getText()));
        if(!UIUtils.isContainsNumbers(sumLimit.getText())){
            return;
        }
        currentSession = sessionService.create(sumLimit.getText(), beginDate.getValue(), finalDate.getValue());
        afterApplySession(false);
    }

    private void handleLimitedRB() {
        if (limitedBuys.isSelected()) {
            nonLimitedBuys.setSelected(false);
        }
        displayBuyList();
        displayLimitsAndCost();
    }

    private void handleNonLimitedRB() {
        if (nonLimitedBuys.isSelected()) {
            limitedBuys.setSelected(false);
        }
        displayBuyList();
        displayLimitsAndCost();
    }

    private void handleAddBuy(boolean changeBuy){
        LimitFXController limitFXController = this;
        UIStarter<MoreFXController> moreFXControllerUIStarter = new UIStarter<MoreFXController>() {
            @Override
            public void controllerSetting(MoreFXController controller, Stage primaryStage) {
                controller.setCurrentSession(currentSession);
                controller.setCurrentDay(currentDay);
                controller.setPrimaryStage(primaryStage);
                controller.setLimitFXController(limitFXController);
                controller.setCurrentBuy(currentBuy);
                controller.setChangeBuy(changeBuy);
                controller.init();
            }
        };
        moreFXControllerUIStarter.start("more.fxml", "Управление покупками");
    }

    private void handleMoreAboutBuy(){
        if(currentBuy == null){
            return;
        }
        handleAddBuy(true);
    }

    private void handleBtnRemoveBuy() {
        if (currentBuy == null) {
            return;
        }
        buyService.removeBuy(currentBuy, currentDay, currentSession);
        sessionService.update(currentSession);
        displayLimitsAndCost();
        displayBuyList();
    }

    private void handleBtnCloseDay() {
        dayService.closeDay(currentDay);
        sessionService.calculateMediumLimit(currentSession);
        displayLimitsAndCost();
        disableBtnAfterCloseDay(true);
    }

    private void handleBtnResumeDay() {
        dayService.resumeDay(currentDay);
        sessionService.calculateMediumLimit(currentSession);
        displayLimitsAndCost();
        disableBtnAfterCloseDay(false);
    }

    private void handleBtnSetting(){
        LimitFXController limitFXController = this;
        UIStarter<SettingsFXController> settingsFXControllerUIStarter = new UIStarter<SettingsFXController>() {
            @Override
            public void controllerSetting(SettingsFXController controller, Stage primaryStage) {
                controller.setLimitFXController(limitFXController);
                controller.setPrimaryStage(primaryStage);
                if(currentSession != null){
                    controller.setSessionDetail(new SessionDetail(
                            currentSession.getLimit(), currentSession.getBeginDate(), currentSession.getFinalDate()
                    ));
                }
                controller.init();
            }
        };
        settingsFXControllerUIStarter.start("sessions-window.fxml", "Управление лимитами");
    }

    void afterApplySession(boolean loadedSession){
        if(loadedSession){
            sumLimit.setText(currentSession.getLimit());
            beginDate.setValue(currentSession.getBeginDate());
            finalDate.setValue(currentSession.getFinalDate());
        }
        currentDate.setValue(currentSession.getBeginDate());
        currentDay = dayService.getDayByDate(currentSession, currentSession.getBeginDate());
        sumLimit.setEditable(false);
        beginDate.setDisable(true);
        finalDate.setDisable(true);
        start.setDisable(true);
        displayLimitsAndCost();
        displayBuyList();
        disableBtnBeforeChooseSession(false);
        disableBtnAfterCloseDay(currentDay.isClose());
    }

    void displayLimitsAndCost() {
        String balance = currentSession.getBalance();
        displayLabel(currentLimit, balance + UIUtils.RUB, balance.contains("-"));
        if(currentDay == null){
            return;
        }
        if(currentDay.isClose()){
            displayLabel(currentLimitDay, "День закрыт", currentDay.isClose());
        } else {
            String dayLimit = currentDay.getLimit();
            displayLabel(currentLimitDay, dayLimit + UIUtils.RUB, dayLimit.contains("-"));
        }
        displayCostOnDay();
    }

    private void displayCostOnDay(){
        if(!limitedBuys.isSelected() && !nonLimitedBuys.isSelected()){
            currentRateDay.setText(buyService.getCostsBuys(currentDay,
                    new BuyFilter(null, BuyFilter.Limit.ALL)) + UIUtils.RUB);

            return;
        }
        currentRateDay.setText(buyService.getCostsBuys(currentDay,
                new BuyFilter(null,
                        (limitedBuys.isSelected() ? BuyFilter.Limit.YES : BuyFilter.Limit.NO)))
                + UIUtils.RUB);
    }

    private void displayLabel(Label label, String text, boolean condition){
        label.setText(text);
        if(condition){
            label.setTextFill(Paint.valueOf("red"));
        } else {
            label.setTextFill(Paint.valueOf("green"));
        }
    }

    void displayBuyList() {
        buys.getItems().clear();
        if(!limitedBuys.isSelected() && !nonLimitedBuys.isSelected()){
            buys.getItems().addAll(buyService.getAllBuysByDay(currentDay,
                    new BuyFilter(null, BuyFilter.Limit.ALL)));

            return;
        }
        buys.getItems().addAll(buyService.getAllBuysByDay(currentDay,
                new BuyFilter(null,
                        (limitedBuys.isSelected() ? BuyFilter.Limit.YES : BuyFilter.Limit.NO)
                )
        ));
    }

    private void displayBuyDescription(){
        if(currentBuy != null){
            descriptionBuy.setText(currentBuy.getDescriptionBuy());
        } else {
            descriptionBuy.setText("");
        }
    }

    private void disableBtnBeforeChooseSession(boolean disable){
        currentDate.setDisable(disable);
        addBuy.setDisable(disable);
        removeBuy.setDisable(disable);
        moreAboutBuy.setDisable(disable);
        closeDay.setDisable(disable);
        resumeDay.setDisable(disable);
        limitedBuys.setDisable(disable);
        nonLimitedBuys.setDisable(disable);
    }

    private void disableBtnAfterCloseDay(boolean disable){
        addBuy.setDisable(disable);
        removeBuy.setDisable(disable);
        moreAboutBuy.setDisable(disable);
        closeDay.setDisable(disable);
        resumeDay.setDisable(!disable);
    }
}
