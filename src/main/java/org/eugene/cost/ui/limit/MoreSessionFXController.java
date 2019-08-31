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
import org.eugene.cost.data.BuyFilter;
import org.eugene.cost.data.Day;
import org.eugene.cost.data.Session;
import org.eugene.cost.service.IBuyService;
import org.eugene.cost.service.IDayService;
import org.eugene.cost.ui.common.UIUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MoreSessionFXController {
    private static final String EMPTY_CATEGORY = "Не выбрано";

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
    private ComboBox<String> buyCategories;

    private BuyCategories currentBuyCategory;

    private Stage primaryStage;

    private IDayService dayService;
    private IBuyService buyService;

    private Session currentSession;
    private Day currentDay;

    void init(){
        dayService = SpringContext.getBean(IDayService.class);
        buyService = SpringContext.getBean(IBuyService.class);

        buyCategories.getItems().addAll(getStringsBuyCategories());
        buyCategories.setValue(EMPTY_CATEGORY);
        buyCategories.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                            currentBuyCategory = BuyCategories.getBuyCategoriesByName(newValue);
                            handleComboBoxCategories();
                });

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

    private List<String> getStringsBuyCategories(){
        List<String> stringsBuyCategories = new ArrayList<>(Arrays.asList(BuyCategories.values())).stream()
                .map(BuyCategories::getName)
                .collect(Collectors.toList());

        stringsBuyCategories.add(EMPTY_CATEGORY);
        return stringsBuyCategories;
    }

    private void handleDayList(String dateOfDay){
        currentDay = dayService.getDayByDate(currentSession,
                LocalDate.parse(dateOfDay, DateTimeFormatter.ofPattern("dd.MMM.yyyy")));

        currentDateOfDay.setText(dateOfDay);
        displayCostOnDay();
        displayBuyList();
    }

    private void handleComboBoxCategories(){
        displayCostOnDay();
        displayBuyList();
        displayTotalLimitPrice();
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
        displayPrice(totalPrice, Collections.singletonList(currentDay));
    }

    private void displayBuyList(){
        if(currentDay == null){
            return;
        }
        buys.getItems().clear();

        if(!limitedBuys.isSelected() && !nonLimitedBuys.isSelected()){
            buys.getItems().addAll(buyService.getAllBuysByDay(currentDay,
                    new BuyFilter(currentBuyCategory, BuyFilter.Limit.ALL)));

            return;
        }
        buys.getItems().addAll(buyService.getAllBuysByDay(currentDay,
                new BuyFilter(currentBuyCategory,
                        (limitedBuys.isSelected() ? BuyFilter.Limit.YES : BuyFilter.Limit.NO)
                )
        ));
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
        displayPrice(totalLimitPrice, allDays);
    }

    private void displayPrice(Label label, List<Day> days){
        if(!limitedBuys.isSelected() && !nonLimitedBuys.isSelected()){
            label.setText(
                    buyService.getCostsBuys(
                            days, new BuyFilter(currentBuyCategory, BuyFilter.Limit.ALL)
                    ) + UIUtils.RUB
            );

            return;
        }

        label.setText(buyService.getCostsBuys(
                days, new BuyFilter(currentBuyCategory,
                        (limitedBuys.isSelected() ? BuyFilter.Limit.YES : BuyFilter.Limit.NO))
        ) + UIUtils.RUB);
    }
}
