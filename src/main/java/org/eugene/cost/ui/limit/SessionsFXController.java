package org.eugene.cost.ui.limit;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import org.eugene.cost.data.SessionDetail;
import org.eugene.cost.config.SpringContext;
import org.eugene.cost.data.Session;
import org.eugene.cost.service.ISessionService;
import org.eugene.cost.ui.chart.BarChartFXController;
import org.eugene.cost.ui.common.MessageType;
import org.eugene.cost.ui.common.UIStarter;
import org.eugene.cost.ui.common.UIUtils;

public class SessionsFXController {
    @FXML
    private ListView<Session> sessionList;

    @FXML
    private Button applySessionBtn;
    @FXML
    private Button moreAboutSessionBtn;
    @FXML
    private Button removeSessionBtn;
    @FXML
    private Button graphBtn;

    private Stage primaryStage;

    private LimitFXController limitFXController;

    private ISessionService sessionService;

    private Session currentSession;

    private SessionDetail sessionDetail;

    void init(){
        sessionService = SpringContext.getBean(ISessionService.class);

        sessionList.getItems().addAll(sessionService.getAll());
        sessionList.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> currentSession = newValue);

        applySessionBtn.setOnAction(event -> handleApplySessionBtn());
        removeSessionBtn.setOnAction(event -> handleRemoveSessionBtn());
        moreAboutSessionBtn.setOnAction(event -> handleMoreAboutSessionBtn());
        graphBtn.setOnAction(event -> handleGraphBtn());
    }

    void setLimitFXController(LimitFXController limitFXController) {
        this.limitFXController = limitFXController;
    }

    void setSessionDetail(SessionDetail sessionDetail) {
        this.sessionDetail = sessionDetail;
    }

    void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    private void handleApplySessionBtn(){
        if(currentSession == null){
            UIUtils.showOptionPane("Сессия не выбрана!",
                    "Информация", MessageType.INFORMATION);
            return;
        }
        limitFXController.setCurrentSession(currentSession);
        limitFXController.afterApplySession(true);
        primaryStage.close();
    }

    private void handleRemoveSessionBtn(){
        if(currentSession == null){
            UIUtils.showOptionPane("Сессия для удаления не выбрана!",
                    "Предупреждение", MessageType.WARNING);
            return;
        }
        SessionDetail currentSessionDetail = new SessionDetail(
                currentSession.getLimit(), currentSession.getBeginDate(), currentSession.getFinalDate()
        );

        if(sessionDetail != null && sessionDetail.equals(currentSessionDetail)){
            UIUtils.showOptionPane("Невозможно удалить открытую сессию!",
                    "Ошибка", MessageType.ERROR);
            return;
        }
        sessionService.delete(currentSession);
        sessionList.getItems().remove(currentSession);
    }

    private void handleMoreAboutSessionBtn(){
        if(currentSession == null){
            UIUtils.showOptionPane("Сессия для просмотра не выбрана!",
                    "Предупреждение", MessageType.WARNING);
            return;
        }
        UIStarter<SessionInfoFXController> sessionInfoFXControllerUIStarter = new UIStarter<SessionInfoFXController>() {
            @Override
            public void controllerSetting(SessionInfoFXController controller, Stage primaryStage) {
                controller.setPrimaryStage(primaryStage);
                controller.setCurrentSession(currentSession);
                controller.init();
            }
        };
        sessionInfoFXControllerUIStarter.start("sessions-info-window.fxml", "Детализация о лимите");
    }

    private void handleGraphBtn(){
        if(currentSession == null){
            UIUtils.showOptionPane("Сессия для построения графика не выбрана!",
                    "Предупреждение", MessageType.WARNING);
            return;
        }
        UIStarter<BarChartFXController> barChartFXControllerUIStarter = new UIStarter<BarChartFXController>() {
            @Override
            protected boolean isResizable() {
                return true;
            }

            @Override
            public void controllerSetting(BarChartFXController controller, Stage primaryStage) {
                controller.setCurrentSession(currentSession);
                controller.init();
            }
        };
        barChartFXControllerUIStarter.start("bar-chart.fxml", "Диаграмма покупок");
    }
}
