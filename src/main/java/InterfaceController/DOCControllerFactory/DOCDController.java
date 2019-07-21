package InterfaceController.DOCControllerFactory;

import Component.DOCComponent;

import Component.LoginComponent;

import Entities.Patient;
import Main.Tuple;
import State.State;
import State.StateEvent;
import State.Store;
import State.StringCommand;
import System.Sistema;

import io.reactivex.disposables.Disposable;

import io.reactivex.subjects.Subject;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;


import java.util.List;
import java.util.stream.Collectors;

public class DOCDController implements DOCController{
    private Sistema sys = Sistema.getInstance();
    private Store<StringCommand> store;
    private Subject<StateEvent> stream;
    private Disposable dis;
    @FXML private Label patientNameLabel;
    @FXML private Label patientSurnameLabel;
    @FXML private Label patientDateofBirth;
    @FXML private Label patientPlaceofBirth;
    @FXML private TableView<Patient> waitingPatients;
    @FXML private TableColumn<Patient, String> sexColumn;
    @FXML private TextField diagnosisTextField;
    @FXML private Label nameLabel;
    private int selectedRow = 0;

    public DOCDController(Store store, Subject<StateEvent> stream) {
        this.store = store;
        this.stream = stream;
        try {
            dis.dispose();
        } catch (NullPointerException e) {

        }
        dis = stream.subscribe(se -> {
            if (se.command().name().equals("PATIENT_SUCCESSFULLY_ADMITTED")) {
                selectedRow = 0;
            }
            Platform.runLater(() -> nameLabel.setText("Dr. " + se.state().getUser().toString()));
            initialize(se.state());
        });
    }

    @FXML public void initialize() {
        sexColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Patient , String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Patient, String> param) {

                try {
                    int s = Integer.valueOf(param.getValue().getFiscalCode().substring(9, 11));
                    return
                            s <= 0 || s > 71 ?
                                    new SimpleObjectProperty<>("BOTH")
                                    : s > 41 ?
                                    new SimpleObjectProperty<>("F")
                                    : new SimpleObjectProperty<>("M");


                } catch (StringIndexOutOfBoundsException | NumberFormatException err) {
                    return new SimpleObjectProperty<>("BOTH");
                }

            }
        });

        waitingPatients.setRowFactory( tv -> {
            TableRow<Patient> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && (! row.isEmpty()) ) {
                    Patient rowData = row.getItem();
                    setLabels(rowData);
                    selectedRow = row.getIndex();
                }
            });
            return row;
        });

        initialize(store.poll());

    }
    @FXML protected void initialize(State state) {
        List<Patient> nonRecoveredPatients = state.getPatients().stream().filter(pa -> !pa.isRecovered()).collect(Collectors.toList());
        ObservableList<Patient> data = waitingPatients.getItems();
        data.removeAll(data);
        data.addAll(nonRecoveredPatients);

        if(nonRecoveredPatients.size() > selectedRow) {
            waitingPatients.getSelectionModel().select(selectedRow);
            setLabels(nonRecoveredPatients.get(selectedRow));
        }
    }

    @FXML protected void setLabels(Patient p) {
        if (p != null) {
            patientNameLabel.setText(p.getName());
            patientSurnameLabel.setText(p.getSurname());
            patientPlaceofBirth.setText(p.getPlaceOfBirth());
            patientDateofBirth.setText(p.getDateofBirth().toString());
        } else {
            patientNameLabel.setText("");
            patientSurnameLabel.setText("");
            patientPlaceofBirth.setText("");
            patientDateofBirth.setText("");
        }

    }

    @FXML protected void tryAdmission() {
        Patient p = waitingPatients.getSelectionModel().getSelectedItem();
        if (p != null) {
            store.update(new StringCommand("TRY_ADMISSION", new Tuple<>(p, diagnosisTextField.getText())));
        }
        diagnosisTextField.clear();
    }

    @FXML protected void addPrescription() {
        sys.setInterface("DOCAP", DOCComponent.DOCTitle);
    }

    @FXML protected void searchDoc() {
        sys.setInterface("DOCS", DOCComponent.DOCTitle);
    }

    @FXML protected void defaultDoc() {
        sys.setInterface("DOCD", DOCComponent.DOCTitle);
    }

    @FXML protected void monitoringDoc() {
        sys.setInterface("DOCM", DOCComponent.DOCTitle);
    }

    @FXML protected void showMonitoring() {
        store.update(new StringCommand("SHOW_MONITORING"));
        store.update(new StringCommand("START_MONITORING"));
    }

    @FXML protected void logout() {
        store.update(new StringCommand("LOGOUT"));
        sys.setInterface("login", LoginComponent.loginTitle);
    }

    @FXML protected void close() {
        sys.endSystem();
    }
}
