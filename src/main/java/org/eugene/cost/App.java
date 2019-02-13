package org.eugene.cost;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.eugene.cost.logic.model.limit.Day;
import org.eugene.cost.logic.model.payment.bank.Bank;
import org.eugene.cost.logic.model.payment.bank.BankRepository;
import org.eugene.cost.logic.model.limit.Buy;
import org.eugene.cost.logic.model.limit.SessionRepository;
import org.eugene.cost.ui.payment.BankFXController;
import org.eugene.cost.ui.payment.FinanceFXController;
import org.eugene.cost.ui.payment.OperationFXController;
import org.eugene.cost.ui.limit.LimitFXController;
import org.eugene.cost.ui.limit.MoreFXController;
import org.eugene.cost.ui.limit.SettingsFXController;

import java.io.IOException;
import java.util.Set;

public class App extends Application {
    private Stage parent;

    public void start(Stage primaryStage) throws Exception {
        parent = primaryStage;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("ui/payment-control-window.fxml"));
        AnchorPane panel = loader.load();
        BankFXController controller = loader.getController();
        controller.setApp(this);
        Scene scene = new Scene(panel);
        primaryStage.setTitle("Управление финансами");
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void openOperation(Set<Bank> banks, BankFXController bankFXController){
        try {
            Stage primaryStage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("ui/operation-window.fxml"));
            AnchorPane panel = loader.load();
            OperationFXController controller = loader.getController();
            controller.initialize(banks);
            controller.setStage(primaryStage);
            controller.setBankFXController(bankFXController);
            Scene scene = new Scene(panel);
            primaryStage.initOwner(parent);
            primaryStage.initModality(Modality.APPLICATION_MODAL);
            primaryStage.setTitle("Операции");
            primaryStage.setResizable(false);
            primaryStage.setScene(scene);
            primaryStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openFinance(BankRepository bankRepository, BankFXController bankFXController){
        try {
            Stage primaryStage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("ui/finance-window.fxml"));
            AnchorPane panel = loader.load();
            FinanceFXController controller = loader.getController();
            controller.initialize(bankRepository);
            controller.setBankFXController(bankFXController);
            Scene scene = new Scene(panel);
            primaryStage.initOwner(parent);
            primaryStage.initModality(Modality.APPLICATION_MODAL);
            primaryStage.setTitle("Управление Финансами");
            primaryStage.setResizable(false);
            primaryStage.setScene(scene);
            primaryStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openLimit(Set<Bank> banks, BankFXController bankFXController){
        try {
            Stage primaryStage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("ui/limit-window.fxml"));
            AnchorPane panel = loader.load();
            LimitFXController controller = loader.getController();
            controller.setApp(this);
            controller.setBanks(banks);
            controller.setBankFXController(bankFXController);
            Scene scene = new Scene(panel);
            primaryStage.initOwner(parent);
            primaryStage.initModality(Modality.APPLICATION_MODAL);
            primaryStage.setTitle("Управление лимитами");
            primaryStage.setResizable(false);
            primaryStage.setScene(scene);
            primaryStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openMore(LimitFXController limitFXController, Buy buy, Set<Bank> banks, Day currentDay, BankFXController bankFXController) {
        try {
            Stage primaryStage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("ui/more.fxml"));
            AnchorPane panel = loader.load();
            MoreFXController controller = loader.getController();
            controller.setLimitFXController(limitFXController);
            controller.setStage(primaryStage);
            controller.setCurrentBuy(buy);
            controller.setCurrentDay(currentDay);
            controller.setBanks(banks);
            controller.setBankFXController(bankFXController);
            controller.init();
            Scene scene = new Scene(panel);
            primaryStage.initOwner(parent);
            primaryStage.initModality(Modality.APPLICATION_MODAL);
            primaryStage.setTitle("Добавить покупку");
            primaryStage.setResizable(false);
            primaryStage.setScene(scene);
            primaryStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openSetting(LimitFXController limitFXController, SessionRepository sessionRepository, boolean allowRemove){
        try {
            Stage primaryStage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("ui/sessions-window.fxml"));
            AnchorPane panel = loader.load();
            SettingsFXController controller = loader.getController();
            controller.setLimitFXController(limitFXController);
            controller.setStage(primaryStage);
            controller.setSessionRepository(sessionRepository);
            controller.init(allowRemove);
            Scene scene = new Scene(panel);
            primaryStage.initOwner(parent);
            primaryStage.initModality(Modality.APPLICATION_MODAL);
            primaryStage.setTitle("Сессии");
            primaryStage.setResizable(false);
            primaryStage.setScene(scene);
            primaryStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Stage getParent() {
        return parent;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
