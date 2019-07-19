package InterfaceController;

import Entities.Monitoring;
import Entities.Patient;
import Entities.Recovery;
import State.StateEvent;
import State.Store;
import State.StringCommand;
import System.Sistema;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.Subject;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;

import java.util.List;
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
    @FXML private Label roomLabel;
    @FXML private LineChart hrGraphic;
    @FXML private LineChart bpGraphic;
    @FXML private NumberAxis xhrAxis;
    @FXML private NumberAxis xbpAxis;
    @FXML private NumberAxis yhrAxis;
    @FXML private NumberAxis ybpAxis;
    private Recovery activeRecovery;
    private Subject<StateEvent> stream;
    private Disposable dis;
    private int counterHR = 1;
    private int counterBP = 1;
    private XYChart.Series seriesHR = new XYChart.Series();
    private XYChart.Series seriesS = new XYChart.Series();
    private XYChart.Series seriesD = new XYChart.Series();

    public MonitoringController(Store store, Subject<StateEvent> stream) {
        this.store = store;
        this.stream = stream;

        try {
            dis.dispose();
        } catch (NullPointerException e) {
        }
        dis = this.stream.subscribe(se -> {
            String name = se.command().name();
            if (name.equals("GENERATE_BP") || name.equals("GENERATE_HEART_RATE") || name.equals("GENERATE_TEMPERATURE")) {
                ObservableList<Monitoring> data = table.getItems();
                Monitoring lastMonitoring = activeRecovery.getLastMonitoring();
                data.add(0, lastMonitoring);
                if(table.getItems().size() > 11)
                    table.getItems().remove(11);

                Platform.runLater(() -> hrLabel.setText(String.valueOf(lastMonitoring.getHeartRate())));
                Platform.runLater(() -> tempLabel.setText(String.valueOf(lastMonitoring.getTemperature()).substring(0, 4)));
                Platform.runLater(() -> dbpLabel.setText(String.valueOf(lastMonitoring.getDiastolicPressure())));
                Platform.runLater(() -> sbpLabel.setText(String.valueOf(lastMonitoring.getSystolicPressure())));

                if (name.equals("GENERATE_HEART_RATE")) {
                    Platform.runLater(() -> yhrAxis.setLabel("bpm"));
                    Platform.runLater(() -> seriesHR.setName("HR"));
                    Platform.runLater(() -> {
                        xhrAxis.setLowerBound((counterHR - 11 > 0) ? counterHR - 11 : 0);
                        xhrAxis.setUpperBound(counterHR + 1);
                    });
                    Platform.runLater(() -> {
                        XYChart.Data elem = new XYChart.Data(counterHR, lastMonitoring.getHeartRate());
                        ObservableList<XYChart.Data> s = seriesHR.getData();
                        if (s.size() > 10)
                            s.remove(0);
                        s.add(elem);
                        seriesHR.setData(s);
                        hrGraphic.getData().add(seriesHR);
                        counterHR++;
                    });
                } else if(name.equals("GENERATE_BP")) {
                    Platform.runLater(() -> ybpAxis.setLabel("mmHg"));
                    Platform.runLater(() -> {
                        seriesS.setName("SBP");
                        seriesD.setName("DBP");
                    });
                    Platform.runLater(() -> {
                        xbpAxis.setLowerBound((counterBP - 11 > 0) ? counterBP - 11 : 0);
                        xbpAxis.setUpperBound(counterBP + 1);
                    });
                    Platform.runLater(() -> {
                        XYChart.Data elem = new XYChart.Data(counterBP, lastMonitoring.getSystolicPressure());
                        ObservableList<XYChart.Data> s = seriesS.getData();
                        if (s.size() > 10)
                            s.remove(0);
                        s.add(elem);
                        seriesS.setData(s);

                        elem = new XYChart.Data(counterBP, lastMonitoring.getDiastolicPressure());
                        s = seriesD.getData();
                        if (s.size() > 10)
                            s.remove(0);
                        s.add(elem);
                        seriesD.setData(s);

                        bpGraphic.getData().addAll(seriesS, seriesD);

                        counterBP++;
                    });
                }
            }
        });
    }

    @FXML public void initialize() {
        setInfo(0);
        xhrAxis.setAutoRanging(false);
        xhrAxis.setLowerBound(0);
        xhrAxis.setUpperBound(2);
        xhrAxis.setTickUnit(1.0);
        yhrAxis.setAutoRanging(false);
        yhrAxis.setLowerBound(40.0);
        yhrAxis.setUpperBound(160);
        yhrAxis.setTickUnit(40.0);

        xbpAxis.setAutoRanging(false);
        xbpAxis.setLowerBound(0);
        xbpAxis.setUpperBound(2);
        xbpAxis.setTickUnit(1.0);
        ybpAxis.setAutoRanging(false);
        ybpAxis.setLowerBound(40.0);
        ybpAxis.setUpperBound(220);
        ybpAxis.setTickUnit(40.0);
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

    @FXML private void setInfo(int index) {
        List<Recovery> allActiveRecoveries = store.poll().getAllRecoveries();
        if (index < allActiveRecoveries.size()) {
            try {
                this.activeRecovery = allActiveRecoveries.get(index);
                Monitoring lastMonitoring = activeRecovery.getLastMonitoring();
                Patient p = activeRecovery.getPatient();
                nameLabel.setText(p.getName());
                surnameLabel.setText(p.getSurname());
                ObservableList<Monitoring> data  = table.getItems();
                data.removeAll(data);
                ObservableList<XYChart.Data> data1 = seriesHR.getData();
                data1.removeAll(data1);
                data1= seriesD.getData();
                data1.removeAll(data1);
                data1= seriesS.getData();
                data1.removeAll(data1);

                Platform.runLater(() -> hrLabel.setText(String.valueOf(lastMonitoring.getHeartRate())));
                Platform.runLater(() -> tempLabel.setText(String.valueOf(lastMonitoring.getTemperature()).substring(0, 4)));
                Platform.runLater(() -> dbpLabel.setText(String.valueOf(lastMonitoring.getDiastolicPressure())));
                Platform.runLater(() -> sbpLabel.setText(String.valueOf(lastMonitoring.getSystolicPressure())));
                Platform.runLater(() -> roomLabel.setText("Room " + String.valueOf(index+1)));

                data.addAll(p.getRecoveries().stream().flatMap(r -> r.getMonitorings().stream()).collect(Collectors.toList()));

            } catch (Exception e) {
                System.out.println("Some error occured");
            }
        }
    }

    @FXML protected void buttonPressed(Event e)
    {
        String s = ((Button) e.getSource()).getText();
        setInfo(Integer.valueOf(s.substring(5))-1);
    }
}
