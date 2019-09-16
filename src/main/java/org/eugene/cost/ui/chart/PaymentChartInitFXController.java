package org.eugene.cost.ui.chart;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import org.eugene.cost.config.SpringContext;
import org.eugene.cost.data.Payment;
import org.eugene.cost.service.IPaymentService;
import org.eugene.cost.ui.common.MessageType;
import org.eugene.cost.ui.common.UIStarter;
import org.eugene.cost.ui.common.UIUtils;

public class PaymentChartInitFXController {
    @FXML
    private ListView<Payment> paymentList;

    @FXML
    private DatePicker beginDate;
    @FXML
    private DatePicker finalDate;

    @FXML
    private Button buildGraphBtn;

    private Payment currentPayment;

    @FXML
    public void initialize(){
        IPaymentService paymentService = SpringContext.getBean(IPaymentService.class);

        paymentList.getItems().addAll(paymentService.getAll());
        paymentList.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> currentPayment = newValue);

        buildGraphBtn.setOnAction(event -> handleBuildGraphBtn());
    }

    private void handleBuildGraphBtn(){
        if(!checkDatesAndPayment()){
            return;
        }

        UIStarter<PaymentChartFXController> paymentChartFXControllerUIStarter = new UIStarter<PaymentChartFXController>() {
            @Override
            protected boolean isResizable() {
                return true;
            }

            @Override
            public void controllerSetting(PaymentChartFXController controller, Stage primaryStage) {
                controller.setCurrentPayment(currentPayment);
                controller.setBeginDate(beginDate.getValue());
                controller.setFinalDate(finalDate.getValue());
                controller.init();
            }
        };
        paymentChartFXControllerUIStarter.start("payment-chart-window.fxml", "Графики платежных систем");
    }

    private boolean checkDatesAndPayment(){
        if(currentPayment == null){
            UIUtils.showOptionPane("Платежная система не выбрана!",
                    "Предупреждение", MessageType.WARNING);
            return false;
        }
        if(beginDate.getValue() == null){
            UIUtils.showOptionPane("Начальная дата не заполнена!",
                    "Предупреждение", MessageType.WARNING);
            return false;
        }
        if(finalDate.getValue() == null){
            UIUtils.showOptionPane("Конечная дата не заполнена!",
                    "Предупреждение", MessageType.WARNING);
            return false;
        }
        if(finalDate.getValue().isBefore(beginDate.getValue())){
            UIUtils.showOptionPane("Конечная дата не может быть ранее начальной даты!",
                    "Ошибка", MessageType.ERROR);
            return false;
        }
        return true;
    }
}
