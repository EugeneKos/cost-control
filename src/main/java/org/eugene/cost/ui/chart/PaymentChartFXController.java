package org.eugene.cost.ui.chart;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

import javafx.scene.control.TextField;
import org.eugene.cost.config.SpringContext;
import org.eugene.cost.data.OperationType;
import org.eugene.cost.data.Payment;
import org.eugene.cost.service.IOperationService;
import org.eugene.cost.service.util.DateUtils;

import java.time.LocalDate;
import java.util.Map;

public class PaymentChartFXController {
    @FXML
    private LineChart<String, Number> balanceSchedule;
    @FXML
    private BarChart<String, Number> expenseSchedule;
    @FXML
    private BarChart<String, Number> arrivalSchedule;

    @FXML
    private TextField pointValue;

    private Payment currentPayment;

    private LocalDate beginDate;
    private LocalDate finalDate;

    void init(){
        IOperationService operationService = SpringContext.getBean(IOperationService.class);

        XYChart.Series<String, Number> expenseDataSeries = new XYChart.Series<>();
        expenseDataSeries.setName("Расходы");
        expenseSchedule.setTitle("График расходов");
        expenseSchedule.getData().add(expenseDataSeries);

        Map<LocalDate, Double> transactionAmountsByDebit = operationService
                .getTransactionsAmountsByTypeAndDates(currentPayment, OperationType.DEBIT, beginDate, finalDate);

        fillChart(transactionAmountsByDebit, expenseDataSeries, beginDate, finalDate);

        XYChart.Series<String, Number> arrivalDataSeries = new XYChart.Series<>();
        arrivalDataSeries.setName("Поступления");
        arrivalSchedule.setTitle("График поступлений");
        arrivalSchedule.getData().add(arrivalDataSeries);

        Map<LocalDate, Double> transactionAmountsByEnrollment = operationService
                .getTransactionsAmountsByTypeAndDates(currentPayment, OperationType.ENROLLMENT, beginDate, finalDate);

        fillChart(transactionAmountsByEnrollment, arrivalDataSeries, beginDate, finalDate);

        XYChart.Series<String, Number> balancesDataSeries = new XYChart.Series<>();
        balancesDataSeries.setName("Балансы по датам");
        balanceSchedule.setTitle("График балансов");
        balanceSchedule.getData().add(balancesDataSeries);

        Map<LocalDate, Double> balancesByDates = operationService.getBalancesByDates(currentPayment, beginDate, finalDate);

        fillChart(balancesByDates, balancesDataSeries, beginDate, finalDate);
    }

    void setCurrentPayment(Payment currentPayment) {
        this.currentPayment = currentPayment;
    }

    void setBeginDate(LocalDate beginDate) {
        this.beginDate = beginDate;
    }

    void setFinalDate(LocalDate finalDate) {
        this.finalDate = finalDate;
    }

    private void fillChart(Map<LocalDate, Double> dateDoubleMap,
                           XYChart.Series<String, Number> dataSeries,
                           LocalDate beginDate, LocalDate finalDate){

        LocalDate currentDate = beginDate;
        while (currentDate.isBefore(finalDate) || currentDate.isEqual(finalDate)){
            Double value = dateDoubleMap.get(currentDate);
            if(value != null){
                dataSeries.getData().add(new XYChart.Data<>(DateUtils.dateToString(currentDate), value));
                for (XYChart.Data data : dataSeries.getData()){
                    data.getNode().setOnMouseClicked(event ->
                            pointValue.setText("Текущее значение: " + data.getYValue()));
                }
            }
            currentDate = currentDate.plusDays(1);
        }
    }
}
