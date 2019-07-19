package InterfaceController;


import Entities.Administration;
import Entities.Monitoring;
import Entities.Patient;
import State.State;
import State.StateEvent;
import State.Store;
import State.StringCommand;
import Component.HPComponent;
import Component.LoginComponent;
import System.Sistema;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.Subject;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.util.StringConverter;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class HPMController {
    private final Store<StringCommand> store;
    private final Sistema sys = Sistema.getInstance();
    @FXML private TableView<Monitoring> tableMonitorings = new TableView<>();
    @FXML private TableView<Administration> tableAdministrations = new TableView<>();
    @FXML private ComboBox<Patient> patientComboBox;
    private Disposable dis;

    public HPMController(Store<StringCommand> store, Subject<StateEvent> stream) {
        this.store = store;

        try {
            dis.dispose();
        } catch (NullPointerException e) {}

        dis = stream.subscribe(se ->
        {
            updatePatients(se.state());
            setData(patientComboBox.getValue());
        });
    }

    @FXML protected void initialize() {
        patientComboBox.setConverter(new StringConverter<Patient>() {
            @Override
            public String toString(Patient patient) {
                try {
                    return patient.getName() + " " + patient.getSurname();
                } catch (Exception err) {
                    return "";
                }
            }

            @Override
            public Patient fromString(String s) {
                return patientComboBox.getValue();
            }
        });
        initialize(store.poll());
    }

    @FXML protected void initialize(State state) {
        updatePatients(state);
        patientComboBox.getSelectionModel().selectFirst();
        setData(patientComboBox.getValue());
    }

    @FXML protected void updatePatients(State state) {
        List<Patient> patients = state.getActiveRecoveries().stream().map(r -> r.getPatient()).collect(Collectors.toList());
        ObservableList<Patient> data = this.patientComboBox.getItems();
        int index = patientComboBox.getSelectionModel().getSelectedIndex();
        data.removeAll(data);
        data.addAll(patients);
        patientComboBox.getSelectionModel().select(index);
    }
    @FXML protected void showMonitoring() {
        store.update(new StringCommand("SHOW_MONITORING"));
        store.update(new StringCommand("START_MONITORING"));
    }
    @FXML protected void selectedPatient() {
        this.setData(patientComboBox.getValue());
    }
    @FXML protected void setData(Patient p) {
        if (p != null) {
            List<Monitoring> monitorings = p.getRecoveries().stream().flatMap(re -> re.getMonitorings().stream()).collect(Collectors.toList());
            List<Administration> administrations = p.getAdministrations();
            ObservableList<Monitoring> data1 = tableMonitorings.getItems();
            data1.removeAll(data1);
            data1.addAll(monitorings);

            ObservableList<Administration> data2 = tableAdministrations.getItems();
            data2.removeAll(data2);
            data2.addAll(administrations);
        }
    }
    @FXML protected void search() {
        sys.setInterface("HPS", HPComponent.HPTitle);
    }
    @FXML protected void dismissPatient() {
        sys.setInterface("HPD", HPComponent.HPTitle);
    }
    @FXML protected void showLast2H() {
        sys.setInterface("HPM", HPComponent.HPTitle);
    }

    @FXML protected void logout() {
        store.update(new StringCommand("LOGOUT"));
        sys.setInterface("login", LoginComponent.loginTitle);
    }

    @FXML protected void close() {
        sys.endSystem();
    }
}
