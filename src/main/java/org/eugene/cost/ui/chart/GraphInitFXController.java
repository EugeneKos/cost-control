package org.eugene.cost.ui.chart;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.eugene.cost.logic.model.payment.bank.Bank;
import org.eugene.cost.logic.model.payment.bank.Card;
import org.eugene.cost.logic.model.payment.bank.Cash;

import javax.swing.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Set;

public class GraphInitFXController {
    @FXML
    private ListView<Bank> bankListView;

    @FXML
    private DatePicker beginDate;
    @FXML
    private DatePicker finalDate;

    @FXML
    private Button buildGraph;

    private Bank currentBank;

    public void initialize(Set<Bank> banks){
        initBankList(banks);
        bankListView.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> currentBank = newValue));
        buildGraph.setOnAction(this::handleBuildGraphBtn);
    }

    private void initBankList(Set<Bank> banks){
        for (Bank bank : banks){
            bankListView.getItems().add(bank);
        }
    }

    private void handleBuildGraphBtn(ActionEvent event){
        if(currentBank == null){
            JOptionPane.showMessageDialog(null,
                    "Платежная система не выбрана!", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (beginDate.getValue() == null || finalDate.getValue() == null) {
            JOptionPane.showMessageDialog(null,
                    "Начальная и конечная даты не установлены!",
                    "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }
        openGraphs(currentBank, beginDate.getValue(), finalDate.getValue());
    }

    private void openGraphs(Bank bank, LocalDate beginDate, LocalDate finalDate){
        try {
            Stage primaryStage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("ui/line-chart.fxml"));
            AnchorPane panel = loader.load();
            LineChartFXController controller = loader.getController();
            controller.initialize(bank, beginDate, finalDate);
            Scene scene = new Scene(panel);
            if(bank instanceof Card){
                primaryStage.setTitle("Графики "+((Card) bank).getNumber());
            } else {
                primaryStage.setTitle("Графики "+((Cash) bank).getDescription());
            }
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
