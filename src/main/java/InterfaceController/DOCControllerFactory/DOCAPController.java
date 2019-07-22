package InterfaceController.DOCControllerFactory;

import Component.DOCComponent;
import Component.LoginComponent;
import Entities.Patient;
import Entities.Prescription;
import Entities.Recovery;
import State.State;
import State.StateEvent;
import State.Store;
import System.Sistema;
import State.StringCommand;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.Subject;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

import java.util.Calendar;
import java.util.Optional;
import java.util.stream.Collectors;

public class DOCAPController implements DOCController {
    private Sistema sys = Sistema.getInstance();
    private Store<StringCommand> store;
    private Subject<StateEvent> stream;
    private Disposable dis;
    private Optional<Patient> selectedPatient = Optional.empty();

    @FXML private ComboBox<Patient> patientComboBox;
    @FXML private Label patientName;
    @FXML private Label patientSurname;
    @FXML private Label patientPlaceofBirth;
    @FXML private Label patientDateofBirth;
    @FXML private Label prescriptionDate;
    @FXML private TextField prescriptionDrug;
    @FXML private TextField prescriptionDuration;
    @FXML private TextField prescriptionTotalNumberofDoses;
    @FXML private TextField prescriptionDose;
    @FXML private Label nameLabel;

    public DOCAPController(Store store, Subject<StateEvent> stream) {
        this.store = store;
        this.stream = stream;
        try {
            dis.dispose();
        } catch (NullPointerException e) {

        }
        dis = stream.subscribe(se -> {
            Platform.runLater(() -> nameLabel.setText("Dr. " + se.state().getUser().toString()));
            fillPatientsMantainSelection(se.state());
        });
    }

    @FXML public void initialize() {
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

        prescriptionDate.setText(Calendar.getInstance().getTime().toString());
        fillPatients(store.poll());
        try {
            setSelectedPatient(this.patientComboBox.getItems().get(0));
        } catch(IndexOutOfBoundsException er) {
            setSelectedPatient(null);
        }
    }

    @FXML protected void fillPatients(State state) {
        ObservableList<Patient> data = patientComboBox.getItems();
        data.removeAll(data);
        data.addAll(state.getPatients().stream().filter(Patient::isRecovered).collect(Collectors.toList()));
    }

    @FXML protected void fillPatientsMantainSelection(State state) {
        fillPatients(state);
        if (selectedPatient.isPresent() && patientComboBox.getItems().contains(selectedPatient.get())) {
            setSelectedPatient(selectedPatient.get());
        } else {
            setSelectedPatient(null);
        }

    }

    @FXML protected void setLabels() {
        if (selectedPatient.isPresent()) {
            Patient pat = selectedPatient.get();
            patientName.setText(pat.getName());
            patientSurname.setText(pat.getSurname());
            patientPlaceofBirth.setText(pat.getPlaceOfBirth());
            patientDateofBirth.setText(pat.getDateofBirth().toString());
        } else {
            patientName.setText("");
            patientSurname.setText("");
            patientPlaceofBirth.setText("");
            patientDateofBirth.setText("");
        }
    }

    @FXML protected void setSelectedPatient(Patient p) {
        if (p != null) {
            patientComboBox.getSelectionModel().select(p);
            this.selectedPatient = Optional.of(p);
        } else {
            patientComboBox.getSelectionModel().selectFirst();
            try {
                this.selectedPatient = Optional.of(patientComboBox.getItems().get(0));
            } catch (IndexOutOfBoundsException err) {
                this.selectedPatient = Optional.empty();
            }
        }
        setLabels();
    }

    @FXML protected void selectedItemFromCombobox() {
        this.setSelectedPatient(patientComboBox.getSelectionModel().getSelectedItem());
    }

    @FXML protected void tryAddPrescription() {
        String drug = prescriptionDrug.getText();
        String dose = prescriptionDose.getText();
        String duration = prescriptionDuration.getText();
        String quantity = prescriptionTotalNumberofDoses.getText();

        if (!drug.isEmpty() && !dose.isEmpty() && !duration.isEmpty() && !quantity.isEmpty()) {
            try {
                Recovery rec = selectedPatient.get().getActiveRecoveries().stream().findFirst().get();
                Prescription pre = new Prescription(drug, Integer.valueOf(duration), Integer.valueOf(dose), Integer.valueOf(quantity),
                        store.poll().getUser().getName(), rec);
                        store.update(new StringCommand("ADD_PRESCRIPTION", pre));
                prescriptionTotalNumberofDoses.clear();
                prescriptionDrug.clear();
                prescriptionDose.clear();
                prescriptionDuration.clear();
            } catch (Exception e) {

            }
        }

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
