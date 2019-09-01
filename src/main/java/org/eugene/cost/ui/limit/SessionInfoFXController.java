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

public class SessionInfoFXController {
    private static final String EMPTY_CATEGORY = "Не выбрано";

    @FXML
    private ListView<String> dayList;
    @FXML
    private ListView<Buy> buyList;

    @FXML
    private TextArea buyDescription;

    @FXML
    private Button closeBtn;

    @FXML
    private Label costsPerDay;
    @FXML
    private Label currentDateOfDay;
    @FXML
    private Label costsPerLimit;

    @FXML
    private RadioButton limitedBuysRB;
    @FXML
    private RadioButton nonLimitedBuysRB;

    @FXML
    private ComboBox<String> buyCategoriesCB;

    private BuyCategories currentBuyCategory;

    private Stage primaryStage;

    private IDayService dayService;
    private IBuyService buyService;

    private Session currentSession;
    private Day currentDay;

    void init(){
        dayService = SpringContext.getBean(IDayService.class);
        buyService = SpringContext.getBean(IBuyService.class);

        buyCategoriesCB.getItems().addAll(getStringsBuyCategories());
        buyCategoriesCB.setValue(EMPTY_CATEGORY);
        buyCategoriesCB.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                            currentBuyCategory = BuyCategories.getBuyCategoriesByName(newValue);
                            handleComboBoxCategories();
                });

        displayAllDays();
        displayCostsPerLimit();

        dayList.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> handleDayList(newValue));

        buyList.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> displayBuyDescription(newValue));

        limitedBuysRB.setOnAction(event -> handleLimitedBuysRB());
        nonLimitedBuysRB.setOnAction(event -> handleNonLimitedBuysRB());
        closeBtn.setOnAction(event -> handleCloseBtn());
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
        displayCostsPerDay();
        displayBuyList();
    }

    private void handleComboBoxCategories(){
        displayCostsPerDay();
        displayBuyList();
        displayCostsPerLimit();
    }

    private void handleLimitedBuysRB() {
        if (limitedBuysRB.isSelected()) {
            nonLimitedBuysRB.setSelected(false);
        }
        displayCostsPerDay();
        displayBuyList();
        displayCostsPerLimit();
    }

    private void handleNonLimitedBuysRB() {
        if (nonLimitedBuysRB.isSelected()) {
            limitedBuysRB.setSelected(false);
        }
        displayCostsPerDay();
        displayBuyList();
        displayCostsPerLimit();
    }

    private void handleCloseBtn(){
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

    private void displayCostsPerDay(){
        if(currentDay == null){
            return;
        }
        displayCosts(costsPerDay, Collections.singletonList(currentDay));
    }

    private void displayBuyList(){
        if(currentDay == null){
            return;
        }
        buyList.getItems().clear();

        if(!limitedBuysRB.isSelected() && !nonLimitedBuysRB.isSelected()){
            buyList.getItems().addAll(buyService.getAllBuysByDay(currentDay,
                    new BuyFilter(currentBuyCategory, BuyFilter.Limit.ALL)));

            return;
        }
        buyList.getItems().addAll(buyService.getAllBuysByDay(currentDay,
                new BuyFilter(currentBuyCategory,
                        (limitedBuysRB.isSelected() ? BuyFilter.Limit.YES : BuyFilter.Limit.NO)
                )
        ));
    }

    private void displayBuyDescription(Buy buy){
        if(buy == null){
            buyDescription.setText("");
            return;
        }
        buyDescription.setText(buy.getDescriptionBuy());
    }

    private void displayCostsPerLimit(){
        List<Day> allDays = dayService.getAllDays(currentSession);
        displayCosts(costsPerLimit, allDays);
    }

    private void displayCosts(Label label, List<Day> days){
        if(!limitedBuysRB.isSelected() && !nonLimitedBuysRB.isSelected()){
            label.setText(
                    buyService.getCostsBuys(
                            days, new BuyFilter(currentBuyCategory, BuyFilter.Limit.ALL)
                    ) + UIUtils.RUB
            );

            return;
        }

        label.setText(buyService.getCostsBuys(
                days, new BuyFilter(currentBuyCategory,
                        (limitedBuysRB.isSelected() ? BuyFilter.Limit.YES : BuyFilter.Limit.NO))
        ) + UIUtils.RUB);
    }
}
