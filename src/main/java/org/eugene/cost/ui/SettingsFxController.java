package org.eugene.cost.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.eugene.cost.logic.model.Session;
import org.eugene.cost.logic.model.Sessions;
import org.eugene.cost.logic.util.FileManager;

import javax.swing.*;
import java.io.IOException;

public class SettingsFxController {
    @FXML
    private ListView<String> sessionList;

    @FXML
    private Button applySession;
    @FXML
    private Button moreAboutSession;
    @FXML
    private Button removeSession;

    private MainFxController mainFxController;

    private Stage stage;

    private Sessions sessions;

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
            MoreSessionFxController controller = loader.getController();
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

    public void setMainFxController(MainFxController mainFxController) {
        this.mainFxController = mainFxController;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setSessions(Sessions sessions) {
        this.sessions = sessions;
    }

    private void handleApplySessionBtn(ActionEvent event){
        if (currentSessionIntoSessionList == -1) {
            JOptionPane.showMessageDialog(null, "Сессия не выбрана!", "Информация", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        mainFxController.applySession(sessions.getSession(currentSessionIntoSessionList));
        stage.close();
    }

    private void handleMoreAboutSessionBtn(ActionEvent event){
        if (currentSessionIntoSessionList == -1) {
            JOptionPane.showMessageDialog(null, "Сессия не выбрана!", "Информация", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        openMoreSession(sessions.getSession(currentSessionIntoSessionList));
    }

    private void handleRemoveSessionBtn(ActionEvent event){
        if (currentSessionIntoSessionList == -1) {
            JOptionPane.showMessageDialog(null, "Сессия не выбрана!", "Информация", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        sessions.removeSession(currentSessionIntoSessionList);
        sessionList.getItems().remove(currentSessionIntoSessionList);
        updateSessionList();
        FileManager.save(sessions);
    }

    private void updateSessionList() {
        sessionList.getItems().clear();
        int num = 1;
        for (Session session : sessions.getSessions()) {
            sessionList.getItems().add("Session number " + num + "               Begin date: "+session.getBeginDate() + "                 Final date: "+session.getFinalDate() + "            is Active: "+session.isActiveSession());
            num++;
        }
    }
}
