package org.eugene.cost.ui.limit;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import org.eugene.cost.config.SpringContext;
import org.eugene.cost.data.Buy;
import org.eugene.cost.data.BuyCategories;
import org.eugene.cost.data.Day;
import org.eugene.cost.data.Session;
import org.eugene.cost.service.IBuyService;
import org.eugene.cost.service.IDayService;
import org.eugene.cost.ui.common.UIUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class MoreSessionFXController {
    @FXML
    private ListView<String> dayList;
    @FXML
    private ListView<Buy> buys;

    @FXML
    private TextArea descriptionBuy;

    @FXML
    private Button close;

    @FXML
    private Label totalPrice;
    @FXML
    private Label currentDateOfDay;
    @FXML
    private Label totalLimitPrice;

    @FXML
    private RadioButton limitedBuys;
    @FXML
    private RadioButton nonLimitedBuys;

    @FXML
    private ComboBox<BuyCategories> buyCategories;

    private Stage primaryStage;

    private IDayService dayService;
    private IBuyService buyService;

    private Session currentSession;
    private Day currentDay;

    void init(){
        dayService = SpringContext.getBean(IDayService.class);
        buyService = SpringContext.getBean(IBuyService.class);

        buyCategories.getItems().addAll(BuyCategories.values());

        displayAllDays();
        displayTotalLimitPrice();

        dayList.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> handleDayList(newValue));

        buys.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> displayDescriptionBuy(newValue));

        limitedBuys.setOnAction(event -> handleLimitedBuys());
        nonLimitedBuys.setOnAction(event -> handleNonLimitedBuys());
        close.setOnAction(event -> handleBtnClose());
    }

    void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    void setCurrentSession(Session currentSession) {
        this.currentSession = currentSession;
    }

    private void handleDayList(String dateOfDay){
        currentDay = dayService.getDayByDate(currentSession,
                LocalDate.parse(dateOfDay, DateTimeFormatter.ofPattern("dd.MMM.yyyy")));

        currentDateOfDay.setText(dateOfDay);
        displayCostOnDay();
        displayBuyList();
    }

    private void handleLimitedBuys() {
        if (limitedBuys.isSelected()) {
            nonLimitedBuys.setSelected(false);
        }
        displayCostOnDay();
        displayBuyList();
        displayTotalLimitPrice();
    }

    private void handleNonLimitedBuys() {
        if (nonLimitedBuys.isSelected()) {
            limitedBuys.setSelected(false);
        }
        displayCostOnDay();
        displayBuyList();
        displayTotalLimitPrice();
    }

    private void handleBtnClose(){
        primaryStage.close();
    }

    private void displayAllDays(){
        List<Day> allDays = dayService.getAllDays(currentSession);
        dayList.getItems().addAll(allDays.stream()
                .map(this::getFormattedDateByDay)
                .collect(Collectors.toList()));
    }

    private String getFormattedDateByDay(Day day){
        return day.getDate().format(DateTimeFormatter.ofPattern("dd.MMM.yyyy"));
    }

    private void displayCostOnDay(){
        if(currentDay == null){
            return;
        }
        if(!limitedBuys.isSelected() && !nonLimitedBuys.isSelected()){
            totalPrice.setText(buyService.getCostsBuys(currentDay) + UIUtils.RUB);
            return;
        }
        totalPrice.setText(buyService.getCostsBuys(currentDay, limitedBuys.isSelected()) + UIUtils.RUB);
    }

    private void displayBuyList(){
        if(currentDay == null){
            return;
        }
        buys.getItems().clear();

        if(!limitedBuys.isSelected() && !nonLimitedBuys.isSelected()){
            buys.getItems().addAll(buyService.getAllBuysByDay(currentDay));
        }
        else if(limitedBuys.isSelected()){
            buys.getItems().addAll(buyService.getAllLimitedBuysByDay(currentDay));
        } else {
            buys.getItems().addAll(buyService.getAllNonLimitedBuysByDay(currentDay));
        }
    }

    private void displayDescriptionBuy(Buy buy){
        if(buy == null){
            descriptionBuy.setText("");
            return;
        }
        descriptionBuy.setText(buy.getDescriptionBuy());
    }

    private void displayTotalLimitPrice(){
        List<Day> allDays = dayService.getAllDays(currentSession);
        if(!limitedBuys.isSelected() && !nonLimitedBuys.isSelected()){
            totalLimitPrice.setText(buyService.getCostsBuys(allDays) + UIUtils.RUB);
            return;
        }
        totalLimitPrice.setText(buyService.getCostsBuys(allDays, limitedBuys.isSelected()) + UIUtils.RUB);
    }
}
