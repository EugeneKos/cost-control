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

import org.apache.log4j.Logger;
import org.eugene.cost.config.SpringContext;
import org.eugene.cost.data.Buy;
import org.eugene.cost.data.BuyFilter;
import org.eugene.cost.data.Day;
import org.eugene.cost.data.OperationType;
import org.eugene.cost.data.Payment;
import org.eugene.cost.data.PaymentOperation;
import org.eugene.cost.data.Session;
import org.eugene.cost.data.SessionDetail;
import org.eugene.cost.exeption.NotEnoughMoneyException;
import org.eugene.cost.service.IBuyService;
import org.eugene.cost.service.IDayService;
import org.eugene.cost.service.IOperationService;
import org.eugene.cost.service.IPaymentService;
import org.eugene.cost.service.ISessionService;
import org.eugene.cost.ui.common.UIStarter;
import org.eugene.cost.ui.common.UIUtils;

import java.time.LocalDate;

public class LimitFXController {
    private static Logger LOGGER = Logger.getLogger(LimitFXController.class);

    @FXML
    private DatePicker beginDate;
    @FXML
    private DatePicker finalDate;
    @FXML
    private DatePicker currentDate;

    @FXML
    private ListView<Buy> buyList;

    @FXML
    private TextField limitAmount;
    @FXML
    private TextArea buyDescription;

    @FXML
    private Label currentBalanceLimit;
    @FXML
    private Label currentDayLimit;
    @FXML
    private Label costsPerDay;

    @FXML
    private Button startBtn;
    @FXML
    private Button addBuyBtn;
    @FXML
    private Button removeBuyBtn;
    @FXML
    private Button moreAboutBuyBtn;
    @FXML
    private Button closeDayBtn;
    @FXML
    private Button resumeDayBtn;
    @FXML
    private Button sessionsBtn;

    @FXML
    private RadioButton limitedBuysRB;
    @FXML
    private RadioButton nonLimitedBuysRB;

    private ISessionService sessionService;
    private IDayService dayService;
    private IBuyService buyService;

    private IPaymentService paymentService;
    private IOperationService operationService;

    private Session currentSession;
    private Day currentDay;
    private Buy currentBuy;

    @FXML
    public void initialize(){
        sessionService = SpringContext.getBean(ISessionService.class);
        dayService = SpringContext.getBean(IDayService.class);
        buyService = SpringContext.getBean(IBuyService.class);

        paymentService = SpringContext.getBean(IPaymentService.class);
        operationService = SpringContext.getBean(IOperationService.class);

        buyList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
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
        startBtn.setOnAction(event -> handleStartBtn());
        addBuyBtn.setOnAction(event -> handleAddBuyBtn(false));
        moreAboutBuyBtn.setOnAction(event -> handleMoreAboutBuyBtn());
        removeBuyBtn.setOnAction(event -> handleRemoveBuyBtn());
        limitedBuysRB.setOnAction(event -> handleLimitedRB());
        nonLimitedBuysRB.setOnAction(event -> handleNonLimitedRB());
        closeDayBtn.setOnAction(event -> handleCloseDayBtn());
        resumeDayBtn.setOnAction(event -> handleResumeDayBtn());
        sessionsBtn.setOnAction(event -> handleSessionsBtn());
    }

    private void handleStartBtn(){
        if(UIUtils.isNull(limitAmount.getText()) || beginDate.getValue() == null || finalDate.getValue() == null){
            return;
        }
        limitAmount.setText(UIUtils.deleteSpace(limitAmount.getText()));
        if(!UIUtils.isContainsNumbers(limitAmount.getText())){
            return;
        }
        currentSession = sessionService.create(limitAmount.getText(), beginDate.getValue(), finalDate.getValue());
        afterApplySession(false);
    }

    private void handleLimitedRB() {
        if (limitedBuysRB.isSelected()) {
            nonLimitedBuysRB.setSelected(false);
        }
        displayBuyList();
        displayLimitsAndCost();
    }

    private void handleNonLimitedRB() {
        if (nonLimitedBuysRB.isSelected()) {
            limitedBuysRB.setSelected(false);
        }
        displayBuyList();
        displayLimitsAndCost();
    }

    private void handleAddBuyBtn(boolean changeBuy){
        LimitFXController limitFXController = this;
        UIStarter<BuyFXController> moreFXControllerUIStarter = new UIStarter<BuyFXController>() {
            @Override
            public void controllerSetting(BuyFXController controller, Stage primaryStage) {
                controller.setCurrentSession(currentSession);
                controller.setCurrentDay(currentDay);
                controller.setPrimaryStage(primaryStage);
                controller.setLimitFXController(limitFXController);
                controller.setCurrentBuy(currentBuy);
                controller.setChangeBuy(changeBuy);
                controller.init();
            }
        };
        moreFXControllerUIStarter.start("buy-window.fxml", "Управление покупками");
    }

    private void handleMoreAboutBuyBtn(){
        if(currentBuy == null){
            return;
        }
        handleAddBuyBtn(true);
    }

    private void handleRemoveBuyBtn() {
        if (currentBuy == null) {
            return;
        }
        Payment payment = paymentService.getByIdentify(currentBuy.getPaymentIdentify());
        if(payment == null){
            return;
        }
        try {
            operationService.create(new PaymentOperation(payment, null), currentBuy.getPrice(),
                    "Отмена покупки. " + currentBuy.getShopOrPlaceBuy()
                            + ": " + currentBuy.getDescriptionBuy(),
                    OperationType.ENROLLMENT);

        } catch (NotEnoughMoneyException e) {
            LOGGER.error(e);
            return;
        }
        buyService.removeBuy(currentBuy, currentDay, currentSession);
        sessionService.update(currentSession);
        displayLimitsAndCost();
        displayBuyList();
    }

    private void handleCloseDayBtn() {
        dayService.closeDay(currentDay);
        sessionService.calculateMediumLimit(currentSession);
        displayLimitsAndCost();
        disableBtnAfterCloseDay(true);
    }

    private void handleResumeDayBtn() {
        dayService.resumeDay(currentDay);
        sessionService.calculateMediumLimit(currentSession);
        displayLimitsAndCost();
        disableBtnAfterCloseDay(false);
    }

    private void handleSessionsBtn(){
        LimitFXController limitFXController = this;
        UIStarter<SessionsFXController> sessionsFXControllerUIStarter = new UIStarter<SessionsFXController>() {
            @Override
            public void controllerSetting(SessionsFXController controller, Stage primaryStage) {
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
        sessionsFXControllerUIStarter.start("sessions-window.fxml", "Управление лимитами");
    }

    void afterApplySession(boolean loadedSession){
        if(loadedSession){
            limitAmount.setText(currentSession.getLimit());
            beginDate.setValue(currentSession.getBeginDate());
            finalDate.setValue(currentSession.getFinalDate());
        }
        currentDate.setValue(currentSession.getBeginDate());
        currentDay = dayService.getDayByDate(currentSession, currentSession.getBeginDate());
        limitAmount.setEditable(false);
        beginDate.setDisable(true);
        finalDate.setDisable(true);
        startBtn.setDisable(true);
        displayLimitsAndCost();
        displayBuyList();
        disableBtnBeforeChooseSession(false);
        disableBtnAfterCloseDay(currentDay.isClose());
    }

    void displayLimitsAndCost() {
        String balance = currentSession.getBalance();
        displayLabel(currentBalanceLimit, balance + UIUtils.RUB, balance.contains("-"));
        if(currentDay == null){
            return;
        }
        if(currentDay.isClose()){
            displayLabel(currentDayLimit, "День закрыт", currentDay.isClose());
        } else {
            String dayLimit = currentDay.getLimit();
            displayLabel(currentDayLimit, dayLimit + UIUtils.RUB, dayLimit.contains("-"));
        }
        displayCostPerDay();
    }

    private void displayCostPerDay(){
        if(!limitedBuysRB.isSelected() && !nonLimitedBuysRB.isSelected()){
            costsPerDay.setText(buyService.getCostsBuys(currentDay,
                    new BuyFilter(null, BuyFilter.Limit.ALL)) + UIUtils.RUB);

            return;
        }
        costsPerDay.setText(buyService.getCostsBuys(currentDay,
                new BuyFilter(null,
                        (limitedBuysRB.isSelected() ? BuyFilter.Limit.YES : BuyFilter.Limit.NO)))
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
        buyList.getItems().clear();
        if(!limitedBuysRB.isSelected() && !nonLimitedBuysRB.isSelected()){
            buyList.getItems().addAll(buyService.getAllBuysByDay(currentDay,
                    new BuyFilter(null, BuyFilter.Limit.ALL)));

            return;
        }
        buyList.getItems().addAll(buyService.getAllBuysByDay(currentDay,
                new BuyFilter(null,
                        (limitedBuysRB.isSelected() ? BuyFilter.Limit.YES : BuyFilter.Limit.NO)
                )
        ));
    }

    private void displayBuyDescription(){
        if(currentBuy != null){
            buyDescription.setText(currentBuy.getDescriptionBuy());
        } else {
            buyDescription.setText("");
        }
    }

    private void disableBtnBeforeChooseSession(boolean disable){
        currentDate.setDisable(disable);
        addBuyBtn.setDisable(disable);
        removeBuyBtn.setDisable(disable);
        moreAboutBuyBtn.setDisable(disable);
        closeDayBtn.setDisable(disable);
        resumeDayBtn.setDisable(disable);
        limitedBuysRB.setDisable(disable);
        nonLimitedBuysRB.setDisable(disable);
    }

    private void disableBtnAfterCloseDay(boolean disable){
        addBuyBtn.setDisable(disable);
        removeBuyBtn.setDisable(disable);
        moreAboutBuyBtn.setDisable(disable);
        closeDayBtn.setDisable(disable);
        resumeDayBtn.setDisable(!disable);
    }
}
