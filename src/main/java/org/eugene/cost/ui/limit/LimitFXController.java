package org.eugene.cost.ui.limit;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Paint;
import org.eugene.cost.App;
import org.eugene.cost.logic.exeption.IncorrectDateException;
import org.eugene.cost.logic.model.payment.bank.Bank;
import org.eugene.cost.logic.model.payment.op.Enrollment;
import org.eugene.cost.logic.model.limit.Buy;
import org.eugene.cost.logic.model.limit.Day;
import org.eugene.cost.logic.model.limit.Session;
import org.eugene.cost.logic.model.limit.SessionRepository;
import org.eugene.cost.logic.util.Calculate;
import org.eugene.cost.logic.util.FileManager;
import org.eugene.cost.logic.util.StringUtil;
import org.eugene.cost.ui.payment.BankFXController;

import javax.swing.*;
import java.time.LocalDate;
import java.util.Set;

public class LimitFXController {
    @FXML
    private DatePicker beginDate;
    @FXML
    private DatePicker finalDate;
    @FXML
    private DatePicker currentDate;

    @FXML
    private ListView<Buy> buyList;

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

    private App app;

    private SessionRepository sessionRepository;

    private Session session;

    private Day currentDay;

    private Buy currentBuy;

    private Set<Bank> banks;

    private BankFXController bankFXController;


    private DateCell colorHandleCurrentDate(DatePicker datePicker) {
        DateCell dateCell = new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                if (item.isBefore(beginDate.getValue()) || item.isAfter(finalDate.getValue()) || item.isEqual(finalDate.getValue())) {
                    setDisable(true);
                    setStyle("-fx-background-color: #ee4040");
                } else if (session.getDay(item).isClose()) {
                    setStyle("-fx-background-color: #1fda1c");
                }
            }
        };
        return dateCell;
    }

    private DateCell colorHandleBeginDate(DatePicker datePicker) {
        DateCell dateCell = new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                if (item.isBefore(LocalDate.now())) {
                    setDisable(true);
                    setStyle("-fx-background-color: #ee4040");
                }
            }
        };
        return dateCell;
    }

    private DateCell colorHandleFinalDate(DatePicker datePicker) {
        DateCell dateCell = new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                if (item.isBefore(LocalDate.now()) || item.isEqual(LocalDate.now())) {
                    setDisable(true);
                    setStyle("-fx-background-color: #ee4040");
                }
            }
        };
        return dateCell;
    }

    @FXML
    public void initialize() {
        beginDate.setDayCellFactory(this::colorHandleBeginDate);
        finalDate.setDayCellFactory(this::colorHandleFinalDate);
        start.setOnAction(this::handleBtnStart);
        setting.setOnAction(this::handleBtnSetting);
        currentDate.setOnAction(this::handleCurrentDate);
        currentDate.setDayCellFactory(this::colorHandleCurrentDate);
        addBuy.setOnAction(this::handleBtnAddBuy);
        removeBuy.setOnAction(this::handleBtnRemoveBuy);
        moreAboutBuy.setOnAction(this::handleBtnMoreAboutBuy);
        closeDay.setOnAction(this::handleBtnCloseDay);
        resumeDay.setOnAction(this::handleBtnResumeDay);
        limitedBuys.setOnAction(this::handleLimitedRB);
        nonLimitedBuys.setOnAction(this::handleNonLimitedRB);
        buyList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            currentBuy = newValue;
            displayBuyDescription();
        });
        loadSessionRepository();
        disableBtnAfterInitSession(true);
    }

    private void loadSessionRepository() {
        sessionRepository = (SessionRepository) FileManager.loadRepository("sessions");
        if (sessionRepository == null) {
            sessionRepository = new SessionRepository();
        }
    }

    public void setApp(App app) {
        this.app = app;
    }

    public void setBanks(Set<Bank> banks) {
        this.banks = banks;
    }

    public void setBankFXController(BankFXController bankFXController) {
        this.bankFXController = bankFXController;
    }

    private void handleCurrentDate(ActionEvent event) {
        LocalDate current = currentDate.getValue();
        currentDay = session.getDay(current);
        setButtonOnCloseDay(currentDay.isClose());
        updateBuyList();
        updateLimitsAndRate();
    }

    private void updateBuyList() {
        buyList.getItems().clear();
        currentBuy = null;
        for (Buy buy : currentDay.getBuyList()) {
            if (limitedBuys.isSelected()) {
                if (buy.isLimited()) {
                    buyList.getItems().add(buy);
                }
            } else if (nonLimitedBuys.isSelected()) {
                if (!buy.isLimited()) {
                    buyList.getItems().add(buy);
                }
            } else {
                buyList.getItems().add(buy);
            }
        }
    }

    private void updateLimitsAndRate() {
        currentLimit.setText(session.getBalance() + " Руб.");
        if (session.getBalance().contains("-")) {
            currentLimit.setTextFill(Paint.valueOf("red"));
        } else {
            currentLimit.setTextFill(Paint.valueOf("green"));
        }
        if (currentDay != null) {
            if (currentDay.isClose()) {
                currentLimitDay.setText("День закрыт");
                currentLimitDay.setTextFill(Paint.valueOf("red"));
            } else {
                currentLimitDay.setText(currentDay.getLimit() + " Руб.");
                if (currentDay.getLimit().contains("-")) {
                    currentLimitDay.setTextFill(Paint.valueOf("red"));
                } else {
                    currentLimitDay.setTextFill(Paint.valueOf("green"));
                }
            }
            currentRateDay.setText(calculateLimitedOrNonLimitedBuy() + " Руб.");
        }
    }

    private String calculateLimitedOrNonLimitedBuy() {
        String rate = "0";
        for (Buy buy : currentDay.getBuyList()) {
            if (limitedBuys.isSelected()) {
                if (buy.isLimited()) {
                    rate = Calculate.plus(rate, buy.getPrice());
                }
            } else if (nonLimitedBuys.isSelected()) {
                if (!buy.isLimited()) {
                    rate = Calculate.plus(rate, buy.getPrice());
                }
            } else {
                rate = Calculate.plus(rate, buy.getPrice());
            }
        }
        return rate;
    }

    private void handleBtnStart(ActionEvent event) {
        String limit = StringUtil.deleteSpace(sumLimit.getText());
        if (!StringUtil.checkSequence(limit)) {
            JOptionPane.showMessageDialog(null,
                    "Ошибка заполнения лимита!", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (beginDate.getValue() == null || finalDate.getValue() == null) {
            JOptionPane.showMessageDialog(null,
                    "Начальная и конечная даты сессии не установлены!",
                    "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            session = new Session(limit, beginDate.getValue(), finalDate.getValue());
            session.calculateMediumLimit();
            sessionRepository.addSession(session);
            currentDate.setValue(beginDate.getValue());
            currentDay = session.getDay(beginDate.getValue());
            setButtonOnCloseDay(currentDay.isClose());
            updateLimitsAndRate();
            disableBtnBeforeInitSession(true);
            disableBtnAfterInitSession(false);
            FileManager.saveRepository(sessionRepository);
        } catch (IncorrectDateException e) {
            JOptionPane.showMessageDialog(null,
                    "Некорректно выставлены начальная и конечная даты сессии!",
                    "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleBtnSetting(ActionEvent event) {
        app.openSetting(this, sessionRepository, session == null);
    }

    private void handleBtnAddBuy(ActionEvent event) {
        app.openMore(this, null, banks, currentDay, bankFXController);
    }

    private void handleBtnRemoveBuy(ActionEvent event) {
        if (currentBuy == null) {
            JOptionPane.showMessageDialog(null,
                    "Покупка не выбрана!", "Информация", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        currentDay.removeBuy(currentBuy, session);
        for (Bank bank : banks){
            if(bank.equals(currentBuy.getPayment())){
                bank.executeOperation(new Enrollment(currentBuy.getPrice(),
                        "Отмена списание средств [ "+currentBuy.getShopOrPlaceBuy()+" ]"));
            }
        }
        buyList.getItems().remove(currentBuy);
        bankFXController.updateBalanceAndHistory();
        bankFXController.saveBanks();
        updateLimitsAndRate();
        FileManager.saveRepository(sessionRepository);
    }

    private void handleBtnMoreAboutBuy(ActionEvent event) {
        if (currentBuy == null) {
            JOptionPane.showMessageDialog(null,
                    "Покупка не выбрана!", "Информация", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        app.openMore(this, currentBuy, banks, currentDay, bankFXController);
    }

    private void handleBtnCloseDay(ActionEvent event) {
        currentDay.setClose(true);
        session.calculateMediumLimit();
        updateLimitsAndRate();
        setButtonOnCloseDay(true);
        FileManager.saveRepository(sessionRepository);
    }

    private void handleBtnResumeDay(ActionEvent event) {
        currentDay.setClose(false);
        session.calculateMediumLimit();
        updateLimitsAndRate();
        setButtonOnCloseDay(false);
        FileManager.saveRepository(sessionRepository);
    }

    private void handleLimitedRB(ActionEvent event) {
        if (limitedBuys.isSelected()) {
            nonLimitedBuys.setSelected(false);
        }
        updateBuyList();
        updateLimitsAndRate();
    }

    private void handleNonLimitedRB(ActionEvent event) {
        if (nonLimitedBuys.isSelected()) {
            limitedBuys.setSelected(false);
        }
        updateBuyList();
        updateLimitsAndRate();
    }

    private void setButtonOnCloseDay(boolean isCloseDay) {
        resumeDay.setDisable(!isCloseDay);
        addBuy.setDisable(isCloseDay);
        removeBuy.setDisable(isCloseDay);
        moreAboutBuy.setDisable(isCloseDay);
        closeDay.setDisable(isCloseDay);
    }

    private void displayBuyDescription() {
        if (currentBuy == null) {
            descriptionBuy.setText("");
            return;
        }
        descriptionBuy.setText(currentBuy.getDescriptionBuy());
    }

    public void addBuyIntoCurrentDay(Buy buy) {
        currentDay.addBuy(buy, session);
        updateBuyList();
        updateLimitsAndRate();
        FileManager.saveRepository(sessionRepository);
    }

    public void applyChangeBuyIntoCurrentDay(Buy currentBuy, Buy newBuy) {
        currentDay.removeBuy(currentBuy, session);
        //currentDay.addBuy(currentBuyIntoBuyList, newBuy, session);
        currentDay.addBuy(newBuy, session);
        updateBuyList();
        updateLimitsAndRate();
        FileManager.saveRepository(sessionRepository);
    }

    public void applySession(Session session) {
        this.session = session;
        this.session.autoCloseDays();
        disableBtnBeforeInitSession(true);
        disableBtnAfterInitSession(false);
        beginDate.setValue(session.getBeginDate());
        finalDate.setValue(session.getFinalDate());
        sumLimit.setText(session.getLimit());
        if(session.isActiveSession()){
            currentDate.setValue(LocalDate.now());
            currentDay = session.getDay(LocalDate.now());
        } else {
            currentDate.setValue(beginDate.getValue());
            currentDay = session.getDay(beginDate.getValue());
        }
        setButtonOnCloseDay(currentDay.isClose());
        updateLimitsAndRate();
        FileManager.saveRepository(sessionRepository);
    }

    private void disableBtnBeforeInitSession(boolean disable) {
        sumLimit.setEditable(!disable);
        beginDate.setDisable(disable);
        finalDate.setDisable(disable);
        start.setDisable(disable);
    }

    private void disableBtnAfterInitSession(boolean disable) {
        currentDate.setDisable(disable);
        addBuy.setDisable(disable);
        removeBuy.setDisable(disable);
        moreAboutBuy.setDisable(disable);
        closeDay.setDisable(disable);
        resumeDay.setDisable(disable);
        limitedBuys.setDisable(disable);
        nonLimitedBuys.setDisable(disable);
    }
}
