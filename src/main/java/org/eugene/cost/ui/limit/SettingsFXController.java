package org.eugene.cost.ui.limit;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.eugene.cost.config.SpringContext;
import org.eugene.cost.data.Session;
import org.eugene.cost.service.ISessionService;

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

    private Stage primaryStage;

    private LimitFXController limitFXController;

    private ISessionService sessionService;

    private Session currentSession;

    void init(){
        sessionService = SpringContext.getBean(ISessionService.class);

        sessionList.getItems().addAll(sessionService.getAll());
        sessionList.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> currentSession = newValue);

        applySession.setOnAction(event -> handleBtnApplySession());
    }

    void setLimitFXController(LimitFXController limitFXController) {
        this.limitFXController = limitFXController;
    }

    void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    private void handleBtnApplySession(){
        if(currentSession == null){
            return;
        }
        limitFXController.setCurrentSession(currentSession);
        limitFXController.afterApplySession(true);
        primaryStage.close();
    }
}
