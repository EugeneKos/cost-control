package org.eugene.cost.ui.chart;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;

import org.eugene.cost.config.SpringContext;
import org.eugene.cost.data.Session;
import org.eugene.cost.service.IBuyService;

import java.util.Map;

public class SessionChartFXController {
    @FXML
    private BarChart<String, Number> barChart;

    private Session currentSession;

    public void init(){
        IBuyService buyService = SpringContext.getBean(IBuyService.class);

        barChart.setTitle("График покупок");

        XYChart.Series<String, Number> dataSeriesLimitedBuys = new XYChart.Series<>();
        dataSeriesLimitedBuys.setName("Лимитные покупки");

        Map<String, Double> limitBuyCostsByCategories = buyService.getLimitBuyCostsByCategories(currentSession);
        fillChart(limitBuyCostsByCategories, dataSeriesLimitedBuys);

        XYChart.Series<String, Number> dataSeriesNonLimitedBuys = new XYChart.Series<>();
        dataSeriesNonLimitedBuys.setName("Не лимитные покупки");

        Map<String, Double> nonLimitBuyCostsByCategories = buyService.getNonLimitBuyCostsByCategories(currentSession);
        fillChart(nonLimitBuyCostsByCategories, dataSeriesNonLimitedBuys);

        barChart.getData().add(dataSeriesLimitedBuys);
        barChart.getData().add(dataSeriesNonLimitedBuys);
    }

    public void setCurrentSession(Session currentSession) {
        this.currentSession = currentSession;
    }

    private void fillChart(Map<String, Double> costsByCategories,
                           XYChart.Series<String, Number> dataSeries){

        for (Map.Entry<String, Double> entry : costsByCategories.entrySet()){
            dataSeries.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }
    }
}
