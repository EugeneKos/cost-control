package org.eugene.cost.ui.chart;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import org.eugene.cost.logic.model.limit.Buy;
import org.eugene.cost.logic.model.limit.BuyCategories;
import org.eugene.cost.logic.model.limit.Day;
import org.eugene.cost.logic.model.limit.Session;
import org.eugene.cost.logic.util.Calculate;

import java.util.List;

public class BarChartFXController {
    @FXML
    private BarChart<String, Number> barChart;


    public void initialize(Session session){
        XYChart.Series<String, Number> xx = new XYChart.Series<>();
        for (BuyCategories buyCategory : BuyCategories.values()){
            xx.getData().add(new XYChart.Data<>(buyCategory.getName(),getSumRateOnCategory(buyCategory, session.getDayList())));
        }
        barChart.getData().add(xx);
    }

    private int getSumRateOnCategory(BuyCategories buyCategories, List<Day> days){
        String sumRate = "0";
        for (Day day : days){
            for (Buy buy : day.getBuyList()){
                if(buy.getBuyCategories() == buyCategories){
                    sumRate = Calculate.plus(sumRate, buy.getPrice());
                }
            }
        }
        return Integer.parseInt(sumRate);
    }
}
