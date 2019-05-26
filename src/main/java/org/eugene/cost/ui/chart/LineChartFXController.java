package org.eugene.cost.ui.chart;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import org.eugene.cost.data.model.Bank;
import org.eugene.cost.service.op.Debit;
import org.eugene.cost.service.op.Enrollment;
import org.eugene.cost.service.op.Operation;
import org.eugene.cost.data.Operations;
import org.eugene.cost.util.Calculate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

public class LineChartFXController {
    @FXML
    private LineChart<String, Number> lineChart;
    @FXML
    private BarChart<String, Number> barChartRate;
    @FXML
    private BarChart<String, Number> barChartArrival;

    public void initialize(Bank bank, LocalDate beginDate, LocalDate finalDate){
        XYChart.Series<String, Number> lineChartSeries = new XYChart.Series<>();
        lineChartSeries.setName("Баланс");
        for (Map.Entry<LocalDate, Double> entry : getBalanceOnPeriod(bank, checkBeginDate(bank, beginDate), checkFinalDate(finalDate)).entrySet()){
            lineChartSeries.getData().add(new XYChart.Data<>(entry.getKey().format(DateTimeFormatter.ofPattern("dd.MM")), entry.getValue()));
        }
        lineChart.getData().add(lineChartSeries);

        XYChart.Series<String, Number> barChartSeriesRate = new XYChart.Series<>();
        barChartSeriesRate.setName("Расходы");
        for (Map.Entry<LocalDate, Double> entry : getRatesOnPeriod(bank, checkBeginDate(bank, beginDate), checkFinalDate(finalDate), Operations.DEBIT).entrySet()){
            barChartSeriesRate.getData().add(new XYChart.Data<>(entry.getKey().format(DateTimeFormatter.ofPattern("dd.MM")), entry.getValue()));
        }
        barChartRate.getData().add(barChartSeriesRate);

        XYChart.Series<String, Number> barChartSeriesArrival = new XYChart.Series<>();
        barChartSeriesArrival.setName("Поступления");
        for (Map.Entry<LocalDate, Double> entry : getRatesOnPeriod(bank, checkBeginDate(bank, beginDate), checkFinalDate(finalDate), Operations.ENROLLMENT).entrySet()){
            barChartSeriesArrival.getData().add(new XYChart.Data<>(entry.getKey().format(DateTimeFormatter.ofPattern("dd.MM")), entry.getValue()));
        }
        barChartArrival.getData().add(barChartSeriesArrival);
    }

    private LocalDate checkBeginDate(Bank bank, LocalDate beginDate){
        return beginDate.isBefore(bank.getDate()) ? bank.getDate() : beginDate;
    }

    private LocalDate checkFinalDate(LocalDate finalDate){
        return finalDate.isAfter(LocalDate.now()) ? LocalDate.now() : finalDate;
    }

    private Map<LocalDate, Double> getBalanceOnPeriod(Bank bank, LocalDate beginDate, LocalDate finalDate){
        Map<LocalDate, Double> balances = new LinkedHashMap<>();
        LocalDate current = beginDate;
        while (!current.isAfter(finalDate)){
            balances.put(current, Double.parseDouble(calculateBalanceOnDate(bank, current)));
            current = current.plusDays(1);
        }
        return balances;
    }

    private String calculateBalanceOnDate(Bank bank, LocalDate date){
        String balance = bank.getBalance();
        if(date.isEqual(LocalDate.now()) | date.isAfter(LocalDate.now())){
            return balance;
        }
        LocalDate current = date.plusDays(1);
        while (current.isBefore(LocalDate.now()) | current.isEqual(LocalDate.now())){
            for (Operation operation : bank.getOperationHistory()){
                if(operation.getDate().isEqual(current)){
                    if(operation instanceof Enrollment){
                        balance = Calculate.minus(balance, operation.getSum());
                    } else if (operation instanceof Debit){
                        balance = Calculate.plus(balance, operation.getSum());
                    }
                }
            }
            current = current.plusDays(1);
        }
        return balance;
    }

    private Map<LocalDate, Double> getRatesOnPeriod(Bank bank, LocalDate beginDate, LocalDate finalDate, Operations operations){
        Map<LocalDate, Double> rates = new LinkedHashMap<>();
        LocalDate current = beginDate;
        while (!current.isAfter(finalDate)){
            String rateOnDay = "0";
            for (Operation operation : bank.getOperationHistory()){
                if(operation.getDate().isEqual(current)){
                    switch (operations){
                        case ENROLLMENT:
                            if(operation instanceof Enrollment){
                                rateOnDay = Calculate.plus(rateOnDay,operation.getSum());
                            }
                            break;
                        case DEBIT:
                            if(operation instanceof Debit){
                                rateOnDay = Calculate.plus(rateOnDay,operation.getSum());
                            }
                            break;
                    }
                }
            }
            rates.put(current, Double.parseDouble(rateOnDay));
            current = current.plusDays(1);
        }
        return rates;
    }
}
