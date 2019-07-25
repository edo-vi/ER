package InterfaceController.NURControllerFactory;

import Component.LoginComponent;
import Component.NURComponent;
import Entities.Administration;
import Entities.Patient;
import Entities.Prescription;
import Entities.Recovery;
import Main.Tuple;
import State.State;
import State.StateEvent;
import State.Store;
import State.StringCommand;
import io.reactivex.disposables.Disposable;
import System.Sistema;
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

import java.util.Date;
import java.util.List;

public class NURDController implements NURController {
    private Store<StringCommand> store;
    private Sistema sys = Sistema.getInstance();
    private Disposable dis;
    @FXML private Label nurseNameLabel;
    @FXML private ComboBox<Patient> patientComboBox = new ComboBox<>();
    @FXML private ComboBox<String> drugComboBox = new ComboBox<>();
    @FXML private Label surnameLabel;
    @FXML private Label nameLabel;
    @FXML private Label bDayLabel;
    @FXML private Label surname;
    @FXML private Label name;
    @FXML private Label bDay;
    @FXML private Label drug;
    @FXML private Label drugLabel;
    @FXML private Label dose;
    @FXML private Label doseLabel;
    @FXML private Label quantity;
    @FXML private Label quantityLabel;
    @FXML private Label note;
    @FXML private Label admDate;
    @FXML private Label admDateLabel;
    @FXML private TextArea noteTextArea;
    @FXML private Button confirm;
    private Prescription prescriptionOfThisAdm = null;
    private Date admDateValue;


    public NURDController(Store<StringCommand> store, Subject<StateEvent> stream) {
        this.store = store;

        try {
            dis.dispose();
        } catch (NullPointerException e) {}

        dis = stream.subscribe(se -> {
            String command = se.command().name();
            if(command.equals("COULD_NOT_ADD_ADMINISTRATION"))
                store.update(new StringCommand("ERROR", "System Error.\nUnlucky"));
            Platform.runLater(() -> nurseNameLabel.setText(se.state().getUser().toString()));
            if(!command.equals("GENERATE_BP") && !command.equals("GENERATE_HEART_RATE") && !command.equals("GENERATE_TEMPERATURE")) {
                updatePatient(se.state());
                setPatientLabel(patientComboBox.getValue());
                updatePrescription();
                setAdministrationLabel(drugComboBox.getValue());
            }
        });
    }

    @FXML protected void initialize() {
        patientComboBox.setConverter(new StringConverter<Patient>() {
            @Override
            public String toString(Patient patient) {
                return patient.getName() + " " + patient.getSurname();
            }
            @Override
            public Patient fromString(String string) {
                ObservableList<Patient> patients = patientComboBox.getItems();
                for(Patient p : patients) {
                    String[] elem = string.split(" ");
                    if(elem[0].equals(p.getName()) && elem[1].equals(p.getSurname()))
                        return p;
                }
                return null;
            }
        });
        drugComboBox.setConverter(new StringConverter<String>() {
            @Override
            public String toString(String object) {
                return object;
            }

            @Override
            public String fromString(String string) {
                return string;
            }
        });

        updatePatient(store.poll());
        patientComboBox.getSelectionModel().selectFirst();
        setPatientLabel(patientComboBox.getValue());
        updatePrescription();
        setAdministrationLabel(drugComboBox.getValue());
    }

    @FXML protected void updatePatient(State state) {
        ObservableList<Patient> data = this.patientComboBox.getItems();
        List<Recovery> recoveries = state.getActiveRecoveries();
        int index = patientComboBox.getSelectionModel().getSelectedIndex();
        data.removeAll(data);
        recoveries.forEach(r -> data.add(r.getPatient()));
        if(recoveries.size() > 0) {
            patientComboBox.getSelectionModel().select(index);
        } else {
            //todo it doesn't fucking work
            drugComboBox.setValue(null);
        }
    }

    @FXML protected void updatePrescription() {
        ObservableList<String> data = this.drugComboBox.getItems();
        int index = drugComboBox.getSelectionModel().getSelectedIndex();
        data.removeAll(data);
        Patient pat = patientComboBox.getSelectionModel().getSelectedItem();
        if (pat != null) {
            pat.getActiveRecoveries().forEach(r -> r.getPrescriptions().forEach(p -> {
                Tuple<String, String> drugToBeAdministrated = new Tuple<>(new java.sql.Date(new Date().getTime()).toString(), p.getDrug());
                if(p.isAdministrable(drugToBeAdministrated))
                    data.add(p.getDrug());
            }));
            if (data.size() > 0) {
                drugComboBox.getSelectionModel().select(index);
            } else {
                //todo it doesn't fucking work
                drugComboBox.setValue(null);
            }
        }

    }

    @FXML protected void setPatientLabel(Patient p) {
        if(p != null) {
            surname.setVisible(true);
            surnameLabel.setVisible(true);
            name.setVisible(true);
            nameLabel.setVisible(true);
            bDay.setVisible(true);
            bDayLabel.setVisible(true);
            surnameLabel.setText(p.getSurname());
            nameLabel.setText(p.getName());
            bDayLabel.setText(p.getDateofBirth().toString());
        } else {
            surname.setVisible(false);
            surnameLabel.setVisible(false);
            name.setVisible(false);
            nameLabel.setVisible(false);
            bDay.setVisible(false);
            bDayLabel.setVisible(false);
            drug.setVisible(false);
            drugLabel.setVisible(false);
            dose.setVisible(false);
            doseLabel.setVisible(false);
            quantity.setVisible(false);
            quantityLabel.setVisible(false);
            admDate.setVisible(false);
            admDateLabel.setVisible(false);
            note.setVisible(false);
            noteTextArea.setVisible(false);
            confirm.setVisible(false);
        }
    }

    @FXML protected void setAdministrationLabel(String d) {
        if(d != null) {
            drug.setVisible(true);
            drugLabel.setText(d);
            drugLabel.setVisible(true);
            dose.setVisible(true);
            patientComboBox.getSelectionModel().getSelectedItem().getActiveRecoveries().forEach(r -> r.getPrescriptions().forEach(p -> {
                if(p.getDrug().equals(d)) {
                    Tuple<String, String> drugToBeAdministrated = new Tuple<>(new java.sql.Date(new Date().getTime()).toString(), d);
                    if(p.isAdministrable(drugToBeAdministrated)) {
                        doseLabel.setText(String.valueOf(p.getAdministrationNumber(drugToBeAdministrated)));
                    }
                    quantityLabel.setText(p.getDailyDose() + " mg/mL");
                }
            }));
            doseLabel.setVisible(true);
            quantity.setVisible(true);
            quantityLabel.setVisible(true);
            admDate.setVisible(true);
            admDateValue = new Date();
            admDateLabel.setText(new java.sql.Date(admDateValue.getTime()).toString());
            admDateLabel.setVisible(true);
            note.setVisible(true);
            noteTextArea.setVisible(true);
            confirm.setVisible(true);

        } else {
            drug.setVisible(false);
            drugLabel.setVisible(false);
            dose.setVisible(false);
            doseLabel.setVisible(false);
            quantity.setVisible(false);
            quantityLabel.setVisible(false);
            admDate.setVisible(false);
            admDateLabel.setVisible(false);
            note.setVisible(false);
            noteTextArea.setVisible(false);
            confirm.setVisible(false);
        }
    }

    @FXML protected void selectedPatientFromCombobox(Event e) {
        try {
            setPatientLabel(((ComboBox<Patient>) e.getSource()).getValue());
            updatePrescription();
            setAdministrationLabel(drugComboBox.getSelectionModel().getSelectedItem());
        } catch(Exception er) {}
    }

    @FXML protected void selectedDrugFromCombobox(Event e) {
        try {
            setAdministrationLabel(((ComboBox<String>) e.getSource()).getValue());
        } catch(Exception er) {}
    }

    @FXML protected void administrate() {
        String notes = (noteTextArea.getText().equals("")) ? "Nessuna" : noteTextArea.getText();
        noteTextArea.clear();
        Integer hour = new Integer(admDateValue.toString().substring(11, 13));

        patientComboBox.getValue().getActiveRecoveries().forEach(r -> {
            r.getPrescriptions().forEach(p-> {
                if(p.getDrug().equals(drugComboBox.getValue())) {
                    prescriptionOfThisAdm = p;
                }
            });
        });

        Administration adm = new Administration(admDateValue, hour, prescriptionOfThisAdm.getDailyDose(), notes, patientComboBox.getValue(), prescriptionOfThisAdm);
        prescriptionOfThisAdm.addAdministration(new Tuple<>(new java.sql.Date(admDateValue.getTime()).toString(), drugComboBox.getValue()));
        store.update(new StringCommand("ADD_ADMINISTRATION", adm));
        store.update(new StringCommand("ERROR", "Somministrazione avvenuta con successo."));
    }

    @FXML protected void showMonitoring() {
        store.update(new StringCommand("SHOW_MONITORING"));
        store.update(new StringCommand("START_MONITORING"));
    }

    @FXML protected void search() {
        sys.setInterface("NURS", NURComponent.NURTitle);
    }

    @FXML protected void addAdministration() {
        sys.setInterface("NURD", NURComponent.NURTitle);
    }

    @FXML protected void addPatient() {
        sys.setInterface("NURAP", NURComponent.NURTitle);
    }

    @FXML protected void showLast2H() {
        sys.setInterface("NURM", NURComponent.NURTitle);
    }

    @FXML
    protected void logout() {
        store.update(new StringCommand("LOGOUT"));
        sys.setInterface("login", LoginComponent.loginTitle);
    }

    @FXML protected void close() {
        sys.endSystem();
    }

    @FXML protected void showSupport() {
        store.update(new StringCommand("ERROR", "Per supporto contattare i Main Developers\nPiccoli Elia, Marian Statache & ***REMOVED***." +
                "\nJava is the best programming language."));
    }
}