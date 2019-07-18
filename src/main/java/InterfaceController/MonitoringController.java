package InterfaceController;

import Component.LoginComponent;
import Entities.Monitoring;
import State.StateEvent;
import State.Store;
import State.StringCommand;
import System.Sistema;
import io.reactivex.subjects.Subject;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;

public class MonitoringController {
    private Sistema sys = Sistema.getInstance();
    private Store<StringCommand> store;
    @FXML private Label nameLabel;
    @FXML private Label surnameLabel;
    @FXML private TableView<Monitoring> dataTable;
    @FXML private Label hrLabel;
    @FXML private Label tempLabel;
    @FXML private Label dbpLabel;
    @FXML private Label sbpLabel;
    @FXML private LineChart hrGraphic;
    @FXML private LineChart bpGraphic;

    public MonitoringController(Store store, Subject<StateEvent> stream) {
        this.store = store;

        // iscrizione
    }

    @FXML protected void login() {
        store.update(new StringCommand("GET_LOGIN"));
    }

    @FXML protected void close() {
        store.update(new StringCommand("CLOSE_MONITORING"));
        store.update(new StringCommand("STOP_MONITORING"));
    }

    @FXML protected void showAlarmController() {
        store.update(new StringCommand("SHOW_ALARMS"));
    }


}
