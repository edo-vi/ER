package InterfaceController;

import Component.LoginComponent;
import Entities.Monitoring;
import Entities.Patient;
import Entities.Recovery;
import State.State;
import State.StateEvent;
import State.Store;
import State.StringCommand;
import System.Sistema;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.Subject;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;

import java.util.stream.Collectors;

public class MonitoringController {
    private Sistema sys = Sistema.getInstance();
    private Store<StringCommand> store;
    @FXML private Label nameLabel;
    @FXML private Label surnameLabel;
    @FXML private TableView<Monitoring> table = new TableView<>();
    @FXML private Label hrLabel;
    @FXML private Label tempLabel;
    @FXML private Label dbpLabel;
    @FXML private Label sbpLabel;
    @FXML private LineChart hrGraphic;
    @FXML private LineChart bpGraphic;
    private Recovery activeRecovery;
    private Subject<StateEvent> stream;
    private Disposable dis;

    public MonitoringController(Store store, Subject<StateEvent> stream) {
        this.store = store;
        this.stream = stream;
        // iscrizione
        try {
            dis.dispose();
        } catch (NullPointerException e) {}
        dis = this.stream.subscribe(se -> {
            String name = se.command().name();
            if (name.equals("GENERATE_BP") || name.equals("GENERATE_HEART_RATE")
                    || name.equals("GENERATE_TEMPERATURE")) {
                ObservableList<Monitoring> data  = table.getItems();
                Monitoring lastMonitoring = activeRecovery.getLastMonitoring();
                data.add(0,lastMonitoring);

                Platform.runLater(() -> hrLabel.setText(String.valueOf(lastMonitoring.getHeartRate())));
                Platform.runLater(() ->tempLabel.setText(String.valueOf(lastMonitoring.getTemperature()).substring(0,4)));
                Platform.runLater(() ->dbpLabel.setText(String.valueOf(lastMonitoring.getDiastolicPressure())));
                Platform.runLater(() ->sbpLabel.setText(String.valueOf(lastMonitoring.getSystolicPressure())));
            }
        });
    }

    @FXML public void initialize() {
        setInfo(0);
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

    @FXML protected void setInfo(int index) {

        try {
            this.activeRecovery = store.poll().getAllRecoveries().get(index);
            Patient p = activeRecovery.getPatient();
            nameLabel.setText(p.getName());
            surnameLabel.setText(p.getSurname());
            ObservableList<Monitoring> data  = table.getItems();
            data.addAll(p.getRecoveries().stream().flatMap(r -> r.getMonitorings().stream()).collect(Collectors.toList()));

        } catch (Exception e) {

        }

     }


}
