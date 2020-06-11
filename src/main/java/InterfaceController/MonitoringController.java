package InterfaceController;

import Entities.Monitoring;
import Entities.Patient;
import Entities.Recovery;
import Main.Tuple;
import State.State;
import State.StateEvent;
import State.Store;
import State.StringCommand;
import System.Sistema;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;

import java.util.ArrayList;
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
    @FXML private TableColumn<Monitoring, String> temperatureColumn;
    @FXML private Button bed0;
    @FXML private Button bed1;
    @FXML private Button bed2;
    @FXML private Button bed3;
    @FXML private Button bed4;
    @FXML private Button bed5;
    @FXML private Button bed6;
    @FXML private Button bed7;
    @FXML private Button bed8;
    @FXML private Button bed9;

    private Subject<StateEvent> stream;
    private Disposable dis;
    private int counterHR = 1;
    private int counterBP = 1;
    private XYChart.Series seriesHR = new XYChart.Series();
    private XYChart.Series seriesS = new XYChart.Series();
    private XYChart.Series seriesD = new XYChart.Series();
    private int room;
    private int recoveryId;
    private ArrayList<Button> bedRooms;

    public MonitoringController(Store store, Subject<StateEvent> stream) {
        this.store = store;
        this.stream = stream;
        try {
            dis.dispose();
        } catch (NullPointerException e) {

        }
        dis = stream.subscribe(se -> {
            String name = se.command().name();
            List<Recovery> activeRecoveries = se.state().getActiveRecoveries();
            updateBed(activeRecoveries);
            if (activeRecoveries.size() <= room) {
                this.setInfo(null);
                this.setRoomLabel(0);
            } else if (activeRecoveries.get(room).getId() != recoveryId) {
                setInfo(activeRecoveries.get(room));
            }
            if (name.equals("GENERATE_BP") || name.equals("GENERATE_HEART_RATE") || name.equals("GENERATE_TEMPERATURE")) {
                ObservableList<Monitoring> data = table.getItems();
                if (activeRecoveries.size() <= room) {
                    setInfo(null);
                    setRoomLabel(0);
                } else {
                    Monitoring lastMonitoring = se.state().getActiveRecoveries().get(room).getLastMonitoring();
                    data.add(0, lastMonitoring);
                    if (table.getItems().size() > 11)
                        table.getItems().remove(11);

                    Platform.runLater(() -> hrLabel.setText(String.valueOf(lastMonitoring.getHeartRate())));
                    Platform.runLater(() -> tempLabel.setText(String.valueOf(lastMonitoring.getTemperature()).substring(0, 4)));
                    Platform.runLater(() -> dbpLabel.setText(String.valueOf(lastMonitoring.getDiastolicPressure())));
                    Platform.runLater(() -> sbpLabel.setText(String.valueOf(lastMonitoring.getSystolicPressure())));

                    if (name.equals("GENERATE_HEART_RATE")) {
                        Platform.runLater(() -> yhrAxis.setLabel("bpm"));
                        Platform.runLater(() -> seriesHR.setName("HR"));
                        Platform.runLater(() -> {
                            if (seriesHR.getData().size() > 9) {
                                xhrAxis.setLowerBound((counterHR - 11 > 0) ? counterHR - 11 : 0);
                            } else
                                xhrAxis.setLowerBound((counterHR - seriesHR.getData().size() > 0) ? counterHR - seriesHR.getData().size() - 1 : 0);

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
                    } else if (name.equals("GENERATE_BP")) {
                        Platform.runLater(() -> ybpAxis.setLabel("mmHg"));
                        Platform.runLater(() -> {
                            seriesS.setName("SBP");
                            seriesD.setName("DBP");
                        });
                        Platform.runLater(() -> {
                            if (seriesD.getData().size() > 9) {
                                xbpAxis.setLowerBound((counterBP - 11 > 0) ? counterBP - 11 : 0);
                            } else
                                xbpAxis.setLowerBound((counterBP - seriesD.getData().size() > 0) ? counterBP - seriesD.getData().size() - 1 : 0);

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
            }
        });
    }

    @FXML public void initialize() {

        temperatureColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Monitoring , String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Monitoring , String> param) {
                return new SimpleObjectProperty<>(String.format("%.2f", param.getValue().getTemperature()));

            }
        });

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

        setRoomLabel(0);
        if (store.poll().getActiveRecoveries().size() > 0)
            setInfo(store.poll().getActiveRecoveries().get(0));
        else
            setInfo(null);

        bedRooms = new ArrayList<>();
        bedRooms.add(bed0);
        bedRooms.add(bed1);
        bedRooms.add(bed2);
        bedRooms.add(bed3);
        bedRooms.add(bed4);
        bedRooms.add(bed5);
        bedRooms.add(bed6);
        bedRooms.add(bed7);
        bedRooms.add(bed8);
        bedRooms.add(bed9);
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

    @FXML protected void setRoomLabel(int a) {
        this.room = a;
        roomLabel.setText("Letto " + String.valueOf(a+1));
    }

    @FXML protected void setInfo(Recovery rec) {

        if (rec != null && rec.isActive()) {
            try {
                recoveryId = rec.getId();
                Monitoring lastMonitoring = rec.getLastMonitoring();
                Patient p = rec.getPatient();
                nameLabel.setText(p.getName());
                surnameLabel.setText(p.getSurname());

                ObservableList<Monitoring> data  = table.getItems();
                data.removeAll(data);
                data.addAll(p.getActiveMonitorings().stream().limit(11).collect(Collectors.toList()));

                ObservableList<XYChart.Data> data1 = seriesHR.getData();
                data1.removeAll(data1);
                data1= seriesD.getData();
                data1.removeAll(data1);
                data1= seriesS.getData();
                data1.removeAll(data1);

                hrLabel.setText(String.valueOf(lastMonitoring.getHeartRate()));
                tempLabel.setText(String.valueOf(lastMonitoring.getTemperature()).substring(0, 4));
                dbpLabel.setText(String.valueOf(lastMonitoring.getDiastolicPressure()));
                sbpLabel.setText(String.valueOf(lastMonitoring.getSystolicPressure()));

                xhrAxis.setLowerBound(counterHR - 1);
                xhrAxis.setUpperBound(counterHR + 1);
                xbpAxis.setLowerBound(counterBP - 1);
                xbpAxis.setUpperBound(counterBP + 1);

            } catch (Exception e) {
                clearInfo();
            }
        } else {
            clearInfo();
        }
    }

    @FXML protected void clearInfo() {
        nameLabel.setText("");
        surnameLabel.setText("");
        recoveryId = -1;
        hrLabel.setText("");
        tempLabel.setText("");
        dbpLabel.setText("");
        sbpLabel.setText("");

        xhrAxis.setLowerBound(counterHR - 1);
        xhrAxis.setUpperBound(counterHR + 1);
        xbpAxis.setLowerBound(counterBP - 1);
        xbpAxis.setUpperBound(counterBP + 1);

        setRoomLabel(0);

        ObservableList<Monitoring> data  = table.getItems();
        data.removeAll(data);
        ObservableList<XYChart.Data> data1 = seriesHR.getData();
        data1.removeAll(data1);
        data1= seriesD.getData();
        data1.removeAll(data1);
        data1= seriesS.getData();
        data1.removeAll(data1);
    }

    @FXML protected void buttonPressed(Event e)
    {
        String s = ((Button) e.getSource()).getText();
        Integer roomNumber = Integer.valueOf(s.split(" ")[1])-1;

        List<Recovery> activerRecoveries = store.poll().getActiveRecoveries();

        if (activerRecoveries.size() > roomNumber) {
            setRoomLabel(roomNumber);
            setInfo(activerRecoveries.get(roomNumber));
        }
    }

    @FXML protected void updateBed(List<Recovery> activeRecoveries) {
        int activeRec = activeRecoveries.size();
        if(activeRec <= 10)
            for(int i=0; i<10; i++)
                bedRooms.get(i).setDisable(!(i < activeRec));
    }

    @FXML protected void showSupport() {
        store.update(new StringCommand("ERROR", "Per supporto contattare i Main Developers\nPiccoli Elia, Marian Statache & Edoardo Zorzi." +
                "\nJava is the best programming language."));
    }
}
