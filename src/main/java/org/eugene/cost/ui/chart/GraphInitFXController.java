package org.eugene.cost.ui.chart;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import org.eugene.cost.data.Payment;

public class GraphInitFXController {
    @FXML
    private ListView<Payment> bankListView;

    @FXML
    private DatePicker beginDate;
    @FXML
    private DatePicker finalDate;

    @FXML
    private Button buildGraph;
}
