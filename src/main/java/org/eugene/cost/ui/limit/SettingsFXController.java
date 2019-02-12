package org.eugene.cost.ui.limit;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.eugene.cost.logic.model.limit.Session;
import org.eugene.cost.logic.model.limit.SessionRepository;
import org.eugene.cost.logic.util.FileManager;
import org.eugene.cost.ui.chart.BarChartFXController;

import javax.swing.*;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class SettingsFXController {
    @FXML
    private ListView<Session> sessionList;

    @FXML
    private Button applySession;
    @FXML
    private Button moreAboutSession;
    @FXML
    private Button removeSession;
    @FXML
    private Button graph;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");

    private LimitFXController limitFXController;

    private Stage stage;

    private SessionRepository sessionRepository;

    private Session currentSession;

    public void init(boolean allowRemove){
        applySession.setOnAction(this::handleApplySessionBtn);
        moreAboutSession.setOnAction(this::handleMoreAboutSessionBtn);
        removeSession.setOnAction(this::handleRemoveSessionBtn);
        graph.setOnAction(this::handleMoreAboutSessionBtn);
        removeSession.setDisable(!allowRemove);
        sessionList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> currentSession = newValue);
        updateSessionList();
    }

    private void openMoreSession(Session session){
        try {
            Stage primaryStage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("ui/more-session.fxml"));
            AnchorPane panel = loader.load();
            MoreSessionFXController controller = loader.getController();
            controller.setStage(primaryStage);
            controller.setSession(session);
            controller.init();
            Scene scene = new Scene(panel);
            primaryStage.setTitle(session.getBeginDate().format(formatter)+" - "+session.getFinalDate().format(formatter));
            primaryStage.setResizable(false);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openBarChart(Session session){
        try {
            Stage primaryStage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("ui/bar-chart.fxml"));
            AnchorPane panel = loader.load();
            BarChartFXController controller = loader.getController();
            controller.initialize(session);
            Scene scene = new Scene(panel);
            primaryStage.setTitle(session.getBeginDate().format(formatter)+" - "+session.getFinalDate().format(formatter));
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setLimitFXController(LimitFXController limitFXController) {
        this.limitFXController = limitFXController;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setSessionRepository(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    private void handleApplySessionBtn(ActionEvent event){
        if (currentSession == null) {
            JOptionPane.showMessageDialog(null, "Сессия не выбрана!",
                    "Информация", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        limitFXController.applySession(currentSession);
        stage.close();
    }

    private void handleMoreAboutSessionBtn(ActionEvent event){
        if (currentSession == null) {
            JOptionPane.showMessageDialog(null, "Сессия не выбрана!",
                    "Информация", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        Button btn = (Button) event.getSource();
        if(btn.getId().equals("graph")){
            openBarChart(currentSession);
        } else {
            openMoreSession(currentSession);
        }
    }

    private void handleRemoveSessionBtn(ActionEvent event){
        if (currentSession == null) {
            JOptionPane.showMessageDialog(null, "Сессия не выбрана!",
                    "Информация", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        sessionRepository.removeSession(currentSession);
        sessionList.getItems().remove(currentSession);
        updateSessionList();
        FileManager.saveRepository(sessionRepository);
    }

    private void updateSessionList() {
        sessionList.getItems().clear();
        for (Session session : sessionRepository.getSessions()) {
            sessionList.getItems().add(session);
        }
    }
}
