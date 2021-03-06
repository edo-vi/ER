package InterfaceController.HPControllerFactory;


import Entities.Patient;
import Entities.Recovery;
import Main.Tuple;
import State.State;
import State.StateEvent;
import State.Store;
import State.StringCommand;
import Component.HPComponent;
import Component.LoginComponent;
import System.Sistema;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.Subject;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.util.StringConverter;

import java.util.Calendar;
import java.util.List;

public class HPDController implements HPController{
    private final Store<StringCommand> store;
    private final Sistema sys = Sistema.getInstance();
    @FXML private Label patientName;
    @FXML private Label patientSurname;
    @FXML private Label patientDateofBirth;
    @FXML private Label patientPlaceofBirth;
    @FXML private Label patientRecoveryStartDate;
    @FXML private Label patientRecoveryEndDate;
    @FXML private Label patientRecoveryReasons;
    @FXML private TextArea dischargeText;
    @FXML private ComboBox<Recovery> patientsChoice = new ComboBox<Recovery>();
    @FXML private Label nameLabel;
    @FXML private Button dischargeButton;
    private Disposable dis;

    public HPDController(Store<StringCommand> store, Subject<StateEvent> stream) {
        this.store = store;

        try {
            dis.dispose();
        } catch (NullPointerException e) {}

        dis = stream.subscribe(se ->
        {
            Platform.runLater(() -> nameLabel.setText("Primario Dr. " + se.state().getUser().toString()));
            updateRecoveries(se.state());
            setLabels(patientsChoice.getValue());

        });
    }

    @FXML protected void initialize() {
        patientsChoice.setConverter(new StringConverter<Recovery>() {
            @Override
            public String toString(Recovery recovery) {
                try {
                    return recovery.getPatient().getName() + " " + recovery.getPatient().getSurname()
                            + ", " + recovery.getStartDate().toString() + ", " + recovery.getDiagnosis();
                } catch (Exception err) {
                    return "";
                }
            }

            @Override
            public Recovery fromString(String s) {
                return patientsChoice.getValue();
            }
        });
        patientRecoveryEndDate.setText(new java.sql.Date(Calendar.getInstance().getTime().getTime()).toString());
        initialize(store.poll());
    }

    @FXML protected void show() {
        patientsChoice.getSelectionModel().selectFirst();
    }
    @FXML protected void initialize(State state) {
        updateRecoveries(state);
        patientsChoice.getSelectionModel().selectFirst();
        setLabels(patientsChoice.getValue());

    }

    @FXML protected void updateRecoveries(State state) {
        List<Recovery> recoveries = state.getActiveRecoveries();
        ObservableList<Recovery> data = this.patientsChoice.getItems();
        int index = patientsChoice.getSelectionModel().getSelectedIndex();
        data.removeAll(data);
        data.addAll(recoveries);
        if (recoveries.size() > index) {
            patientsChoice.getSelectionModel().select(index);
        } else {
            patientsChoice.getSelectionModel().select(0);
        }
    }

    @FXML protected void showMonitoring() {
        store.update(new StringCommand("SHOW_MONITORING"));
        store.update(new StringCommand("START_MONITORING"));
    }

    @FXML protected void setLabels(Recovery r) {
        if(r != null) {
            Patient p = r.getPatient();
            patientName.setText(p.getName());
            patientSurname.setText(p.getSurname());
            patientPlaceofBirth.setText(p.getPlaceOfBirth());
            patientDateofBirth.setText(p.getDateofBirth().toString());
            patientRecoveryStartDate.setText(r.getStartDate().toString());
            patientRecoveryEndDate.setText(new java.sql.Date(Calendar.getInstance().getTime().getTime()).toString());
            patientRecoveryReasons.setText(r.getDiagnosis());
            dischargeButton.setDisable(false);
        } else {
            patientName.setText("");
            patientSurname.setText("");
            patientPlaceofBirth.setText("");
            patientDateofBirth.setText("");
            patientRecoveryStartDate.setText("");
            patientRecoveryEndDate.setText("");
            patientRecoveryReasons.setText("");
            dischargeButton.setDisable(true);
        }


    }

    @FXML protected void selectedItemFromCombobox(Event e) {
        try {
            setLabels(((ComboBox<Recovery>) e.getSource()).getValue());
        } catch(Exception er) {}
    }

    @FXML protected void discharge() {
        String dt = dischargeText.getText();
        dischargeText.clear();
        if (dt != null && !dt.equals("") && patientsChoice.getValue() != null) {
            store.update(new StringCommand("DISCHARGE_PATIENT", new Tuple<>(this.patientsChoice.getValue(), dt)));
            store.update(new StringCommand("ERROR", "Paziente dimesso."));
        } else store.update(new StringCommand("ERROR", "Il campo: 'Diagnosi di dimissione' è obbligatorio."));
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

    @FXML protected void showSupport() {
        store.update(new StringCommand("ERROR", "Per supporto contattare i Main Developers\nPiccoli Elia, Marian Statache & Edoardo Zorzi." +
                "\nJava is the best programming language."));
    }
}
