package InterfaceController.HPControllerFactory;


import Component.NURComponent;
import Entities.Administration;
import Entities.Patient;
import Entities.Prescription;
import Entities.Recovery;
import State.StateEvent;
import State.Store;
import State.StringCommand;
import Component.HPComponent;
import Component.LoginComponent;
import System.Sistema;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.Subject;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.Callback;

import java.util.UUID;
import java.util.stream.Collectors;

public class HPSRController implements HPController {
    private final Store<StringCommand> store;
    private final Sistema sys = Sistema.getInstance();
    @FXML private TableView<Administration> administrations = new TableView<>();
    @FXML private TableView<Prescription> prescriptions = new TableView<>();

    @FXML private Label patientName;
    @FXML private Label patientSurname;
    @FXML private Label patientDateofBirth;
    @FXML private Label patientFiscalCode;
    @FXML private Label patientPlaceofBirth;
    @FXML private Label patientRecoveryStartDate;
    @FXML private Label patientRecoveryEndDate;
    @FXML private Label patientRecoveryReasons;
    @FXML private Label patientRecoveryDischarge;
    @FXML private TextField patientText;
    @FXML private Label nameLabel;

    @FXML private TableColumn<Administration, String> drugColumn;
    @FXML private TableColumn<Prescription, String> quantityColumn;

    private Disposable dis;

    public HPSRController(Store<StringCommand> store, Subject<StateEvent> stream) {
        this.store = store;

        try {
            dis.dispose();
        } catch (NullPointerException e) {}

        dis = stream.subscribe(se ->
        {
            Platform.runLater(() -> nameLabel.setText("Primario Dr. " + se.state().getUser().toString()));
            if(se.command().name().equals("CHOSEN_RECOVERY_TO_SHOW")) {
                setData(se.state().getChosenRecovery());
            }
        });
    }

    @FXML protected void initialize() {
        drugColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Administration , String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Administration , String> param) {
                return new SimpleObjectProperty<>(param.getValue().getPrescription().getDrug());
            }
        });

        quantityColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Prescription , String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Prescription, String> param) {
                int a = param.getValue().getTotalNumberofDoses() * param.getValue().getDailyDose();
                String s = String.format("%d mg/mL", a);
                return new SimpleObjectProperty<String>(s);
            }
        });
    }

    @FXML protected void setData(Recovery r) {
        if (r != null) {
            Patient p = r.getPatient();
            patientName.setText(p.getName());
            patientSurname.setText(p.getSurname());
            patientFiscalCode.setText(p.getFiscalCode());
            patientPlaceofBirth.setText(p.getPlaceOfBirth());
            patientDateofBirth.setText(p.getDateofBirth().toString());
            patientRecoveryStartDate.setText(r.getStartDate().toString());
            try {
                patientRecoveryEndDate.setText(r.getEndDate().toString());
            } catch(Recovery.RecoveryNullFieldException e) {
                patientRecoveryEndDate.setText("");
            }
            try {
                patientRecoveryDischarge.setText(r.getDischargeSummary());
            } catch(Recovery.RecoveryNullFieldException e) {
                patientRecoveryDischarge.setText("");
            }
            patientRecoveryReasons.setText(r.getDiagnosis());


            ObservableList<Prescription> data = prescriptions.getItems();
            data.removeAll(data);
            data.addAll(r.getPrescriptions());

            ObservableList<Administration> data1 = administrations.getItems();
            data1.removeAll(data1);
            data1.addAll(r.getPrescriptions().stream().flatMap(pr -> pr.getAdministrations().stream())
                    .collect(Collectors.toList()));
        } else {
            patientName.setText("");
            patientSurname.setText("");
            patientFiscalCode.setText("");
            patientPlaceofBirth.setText("");
            patientDateofBirth.setText("");
            patientRecoveryStartDate.setText("");
            patientRecoveryEndDate.setText("");
            patientRecoveryReasons.setText("");
            patientRecoveryDischarge.setText("");

            ObservableList<Prescription> data = prescriptions.getItems();
            data.removeAll(data);

            ObservableList<Administration> data1 = administrations.getItems();
            data.removeAll(data1);
        }

    }

    @FXML protected void searchPatient() {
        try {
            String[] arr = patientText.getText().split(" ");
            String name = arr[0];
            String surname = arr[1];
            Patient p = store.poll().getPatients().stream()
                    .filter(pa -> pa.getName().toLowerCase().equals(name.toLowerCase())
                            && pa.getSurname().toLowerCase().equals(surname.toLowerCase())).findFirst().orElse(null);

            if (p != null) {
                store.update(new StringCommand("SEARCH_PATIENT", patientText.getText()));
                sys.setInterface("HPS", HPComponent.HPTitle);
            } else {
                store.update(new StringCommand("ERROR", "La ricerca non ha prodotto nessun risultato.\nRiprovare."));
                patientText.clear();
            }
        } catch (ArrayIndexOutOfBoundsException err) {
            store.update(new StringCommand("ERROR", "La stringa di ricerca deve rispettare il formato:\nNome Cognome"));
            patientText.clear();
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
        store.update(new StringCommand("CLEAR_CHOSEN_RECOVERY"));
        sys.endSystem();
    }
}
