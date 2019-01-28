package org.eugene.cost.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Paint;
import org.eugene.cost.App;
import org.eugene.cost.logic.exeption.IncorrectDateException;
import org.eugene.cost.logic.model.Buy;
import org.eugene.cost.logic.model.Day;
import org.eugene.cost.logic.model.Session;
import org.eugene.cost.logic.model.Sessions;
import org.eugene.cost.logic.util.Calculate;
import org.eugene.cost.logic.util.FileManager;
import org.eugene.cost.logic.util.StringUtil;

import javax.swing.*;
import java.time.LocalDate;

public class MainFxController {
    @FXML
    private DatePicker beginDate;
    @FXML
    private DatePicker finalDate;
    @FXML
    private DatePicker currentDate;

    @FXML
    private ListView<String> buyList;

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

    private Sessions sessions;

    private Session session;

    private Day currentDay;

    private int currentBuyIntoBuyList = -1;


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
            currentBuyIntoBuyList = buyList.getSelectionModel().getSelectedIndex();
            displayBuyDescription(currentBuyIntoBuyList);
        });
        loadSessions();
        blockBtnAfterInitSession(true);
    }

    private void loadSessions() {
        sessions = FileManager.loadSessions();
        if (sessions == null) {
            sessions = new Sessions();
        }
    }

    public void setApp(App app) {
        this.app = app;
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
        for (Buy buy : currentDay.getBuyList()) {
            if (limitedBuys.isSelected()) {
                if (buy.isLimited()) {
                    buyList.getItems().add(buy.toString());
                }
            } else if (nonLimitedBuys.isSelected()) {
                if (!buy.isLimited()) {
                    buyList.getItems().add(buy.toString());
                }
            } else {
                buyList.getItems().add(buy.toString());
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
            JOptionPane.showMessageDialog(null, "Ошибка заполнения лимита!", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (beginDate.getValue() == null || finalDate.getValue() == null) {
            JOptionPane.showMessageDialog(null, "Начальная и конечная даты сессии не установлены!", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            session = new Session(limit, beginDate.getValue(), finalDate.getValue());
            session.calculateMediumLimit();
            sessions.addSession(session);
            currentDate.setValue(beginDate.getValue());
            currentDay = session.getDay(beginDate.getValue());
            setButtonOnCloseDay(currentDay.isClose());
            updateLimitsAndRate();
            blockBtnBeforeInitSession(true);
            blockBtnAfterInitSession(false);
            FileManager.save(sessions);
        } catch (IncorrectDateException e) {
            JOptionPane.showMessageDialog(null, "Некорректно выставлены начальная и конечная даты сессии!", "Ошибка", JOptionPane.ERROR_MESSAGE);
            ;
        }
    }

    private void handleBtnSetting(ActionEvent event) {
        app.openSetting(this, sessions, session == null);
    }

    private void handleBtnAddBuy(ActionEvent event) {
        app.openMore(this, null);
    }

    private void handleBtnRemoveBuy(ActionEvent event) {
        if (currentBuyIntoBuyList == -1) {
            JOptionPane.showMessageDialog(null, "Покупка не выбрана!", "Информация", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        currentDay.removeBuy(currentDay.getBuy(currentBuyIntoBuyList), session);
        buyList.getItems().remove(currentBuyIntoBuyList);
        updateLimitsAndRate();
        FileManager.save(sessions);
    }

    private void handleBtnMoreAboutBuy(ActionEvent event) {
        if (currentBuyIntoBuyList == -1) {
            JOptionPane.showMessageDialog(null, "Покупка не выбрана!", "Информация", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        app.openMore(this, currentDay.getBuy(currentBuyIntoBuyList));
    }

    private void handleBtnCloseDay(ActionEvent event) {
        currentDay.setClose(true);
        session.calculateMediumLimit();
        updateLimitsAndRate();
        setButtonOnCloseDay(true);
        FileManager.save(sessions);
    }

    private void handleBtnResumeDay(ActionEvent event) {
        currentDay.setClose(false);
        session.calculateMediumLimit();
        updateLimitsAndRate();
        setButtonOnCloseDay(false);
        FileManager.save(sessions);
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

    private void displayBuyDescription(int buyIndex) {
        if (buyIndex < 0) {
            descriptionBuy.setText("");
            return;
        }
        if (currentDay.getBuy(buyIndex) == null) return;
        descriptionBuy.setText(currentDay.getBuy(buyIndex).getDescriptionBuy());
    }

    public void addBuyIntoCurrentDay(Buy buy) {
        currentDay.addBuy(buy, session);
        updateBuyList();
        updateLimitsAndRate();
        FileManager.save(sessions);
    }

    public void applyChangeBuyIntoCurrentDay(Buy currentBuy, Buy newBuy) {
        currentDay.removeBuy(currentBuy, session);
        currentDay.addBuy(currentBuyIntoBuyList, newBuy, session);
        updateBuyList();
        updateLimitsAndRate();
        FileManager.save(sessions);
    }

    public void applySession(Session session) {
        this.session = session;
        this.session.autoCloseDays();
        blockBtnBeforeInitSession(true);
        blockBtnAfterInitSession(false);
        beginDate.setValue(session.getBeginDate());
        finalDate.setValue(session.getFinalDate());
        currentDate.setValue(beginDate.getValue());
        sumLimit.setText(session.getLimit());
        currentDay = session.getDay(beginDate.getValue());
        setButtonOnCloseDay(currentDay.isClose());
        updateLimitsAndRate();
        FileManager.save(sessions);
    }

    private void blockBtnBeforeInitSession(boolean disable) {
        sumLimit.setEditable(!disable);
        beginDate.setDisable(disable);
        finalDate.setDisable(disable);
        start.setDisable(disable);
    }

    private void blockBtnAfterInitSession(boolean disable) {
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
