package org.eugene.cost.ui.chart;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import org.eugene.cost.data.model.Buy;
import org.eugene.cost.data.BuyCategories;
import org.eugene.cost.data.model.Day;
import org.eugene.cost.data.model.Session;
import org.eugene.cost.util.Calculate;

import java.util.List;

public class BarChartFXController {
    @FXML
    private BarChart<String, Number> barChart;

    public void initialize(Session session){
        XYChart.Series<String, Number> barChartSeries = new XYChart.Series<>();
        for (BuyCategories buyCategory : BuyCategories.values()){
            barChartSeries.getData().add(new XYChart.Data<>(buyCategory.getName(),getSumRateOnCategory(buyCategory, session.getDayList())));
        }
        barChart.getData().add(barChartSeries);
    }

    private double getSumRateOnCategory(BuyCategories buyCategories, List<Day> days){
        String sumRate = "0";
        for (Day day : days){
            for (Buy buy : day.getBuyList()){
                if(buy.getBuyCategories() == buyCategories){
                    sumRate = Calculate.plus(sumRate, buy.getPrice());
                }
            }
        }
        return Double.parseDouble(sumRate);
    }
}
