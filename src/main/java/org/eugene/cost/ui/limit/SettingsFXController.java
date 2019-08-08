package org.eugene.cost.ui.limit;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import org.eugene.cost.data.Session;

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
}
