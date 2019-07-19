package InterfaceController;


import Entities.Patient;
import Entities.Recovery;
import State.StateEvent;
import State.Store;
import State.StringCommand;
import Component.HPComponent;
import Component.LoginComponent;
import System.Sistema;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.Subject;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import java.util.List;
import java.util.stream.Collectors;

public class HPDController {
    private final Store<StringCommand> store;
    private final Sistema sys = Sistema.getInstance();
    @FXML private Label patientName;
    @FXML private Label patientSurname;
    @FXML private Label patientDateofBirth;
    @FXML private Label patientPlaceofBirth;
    @FXML private Label patientRecoveryStartDate;
    @FXML private Label patientRecoveryEndDate;
    @FXML private Label patientRecoveryReasons;
    @FXML private ComboBox<PatientRecovery> patientsChoice= new ComboBox<PatientRecovery>();
    private Disposable dis;

    public HPDController(Store<StringCommand> store, Subject<StateEvent> stream) {
        this.store = store;

        try {
            dis.dispose();
        } catch (NullPointerException e) {}

        dis = stream.subscribe(se ->
        {

        });
    }

    @FXML public void initialize() {
        List<Recovery> nonActiveRecoveries = store.poll().getNonActiveRecoveries();
        if (nonActiveRecoveries.size() > 0) {
            Recovery r = nonActiveRecoveries.get(0);
            setLabels(r);
            ObservableList<PatientRecovery> data = patientsChoice.getItems();
            data.setAll(nonActiveRecoveries.stream().map(re -> new PatientRecovery(re)).collect(Collectors.toList()));
            patientsChoice.setPromptText(data.get(0).toString());
        }

    }
    @FXML protected void showMonitoring() {
        store.update(new StringCommand("SHOW_MONITORING"));
        store.update(new StringCommand("START_MONITORING"));
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

    @FXML protected void setLabels(Recovery r) {
        Patient p = r.getPatient();
        patientName.setText(p.getName());
        patientSurname.setText(p.getSurname());
        patientPlaceofBirth.setText(p.getPlaceOfBirth());
        patientDateofBirth.setText(p.getDateofBirth().toString());
        patientRecoveryStartDate.setText(r.getStartDate().toString());
        patientRecoveryEndDate.setText(r.getEndDate().toString());
        patientRecoveryReasons.setText(r.getDiagnosis());

        patientsChoice.setPromptText(new PatientRecovery(r).toString());
    }

    @FXML protected void clickComboBox(Event e) {
        setLabels(((ComboBox<PatientRecovery>) e.getSource()).getValue().getRecovery());
    }
    private class PatientRecovery {
        private Patient p;

        private Recovery r;
        protected PatientRecovery(Recovery r) {
            this.r = r;
            this.p = r.getPatient();
        }

        public String toString() {
            return this.p.getName() + " " + this.p.getSurname() + ", "
                    + r.getStartDate().toString() + ", " + r.getDiagnosis();
        }

        public Patient getPatient() {
            return p;
        }

        public Recovery getRecovery() {
            return r;
        }

        /*public void setRecovery(Recovery r) {
            this.r = r;
        }

        public void setPatient(Patient p) {
            this.p = p;
        }*/

    }
}
