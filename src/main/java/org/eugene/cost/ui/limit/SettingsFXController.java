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

import javax.swing.*;
import java.io.IOException;

public class SettingsFXController {
    @FXML
    private ListView<String> sessionList;

    @FXML
    private Button applySession;
    @FXML
    private Button moreAboutSession;
    @FXML
    private Button removeSession;

    private LimitFXController limitFXController;

    private Stage stage;

    private SessionRepository sessionRepository;

    private int currentSessionIntoSessionList = -1;

    public void init(boolean allowRemove){
        applySession.setOnAction(this::handleApplySessionBtn);
        moreAboutSession.setOnAction(this::handleMoreAboutSessionBtn);
        removeSession.setOnAction(this::handleRemoveSessionBtn);
        removeSession.setDisable(!allowRemove);
        sessionList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            currentSessionIntoSessionList = sessionList.getSelectionModel().getSelectedIndex();
        });
        updateSessionList();
    }

    public void openMoreSession(Session session){
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
            primaryStage.setTitle(session.getBeginDate()+" - "+session.getFinalDate());
            primaryStage.setResizable(false);
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
        if (currentSessionIntoSessionList == -1) {
            JOptionPane.showMessageDialog(null, "Сессия не выбрана!", "Информация", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        limitFXController.applySession(sessionRepository.getSession(currentSessionIntoSessionList));
        stage.close();
    }

    private void handleMoreAboutSessionBtn(ActionEvent event){
        if (currentSessionIntoSessionList == -1) {
            JOptionPane.showMessageDialog(null, "Сессия не выбрана!", "Информация", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        openMoreSession(sessionRepository.getSession(currentSessionIntoSessionList));
    }

    private void handleRemoveSessionBtn(ActionEvent event){
        if (currentSessionIntoSessionList == -1) {
            JOptionPane.showMessageDialog(null, "Сессия не выбрана!", "Информация", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        sessionRepository.removeSession(currentSessionIntoSessionList);
        sessionList.getItems().remove(currentSessionIntoSessionList);
        updateSessionList();
        FileManager.save(sessionRepository);
    }

    private void updateSessionList() {
        sessionList.getItems().clear();
        int num = 1;
        for (Session session : sessionRepository.getSessions()) {
            sessionList.getItems().add("Session number " + num + "               Begin date: "+session.getBeginDate() + "                 Final date: "+session.getFinalDate() + "            is Active: "+session.isActiveSession());
            num++;
        }
    }
}
