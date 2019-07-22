package InterfaceController.NURControllerFactory;


import Component.LoginComponent;
import Component.NURComponent;
import Entities.Patient;
import Entities.Recovery;
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

import java.util.Date;

public class NURSController implements NURController {
    private final Store<StringCommand> store;
    private final Sistema sys = Sistema.getInstance();
    @FXML private TableView<Recovery> recoveryTable = new TableView<>();
    @FXML private TextField searchPatient;
    @FXML private Label nameLabel;
    private Disposable dis;

    @FXML private TableColumn<Recovery, String> endDateColumn;
    @FXML private TableColumn<Recovery, String> dischargeSummary;

    public NURSController(Store<StringCommand> store, Subject<StateEvent> stream) {
        this.store = store;

        try {
            dis.dispose();
        } catch (NullPointerException e) {}

        dis = stream.subscribe(se -> {
            Platform.runLater(() -> nameLabel.setText(se.state().getUser().toString()));
            if(se.command().name().equals("SEARCH_PATIENT")) {
                String search = (String) se.command().getArg();
                searchPatient.setText(search);
                updateRecoveries(search);
            }
        });
    }

    @FXML protected void initialize() {

        recoveryTable.setRowFactory( tv -> {
            TableRow<Recovery> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    Recovery rowData = row.getItem();
                    store.update(new StringCommand("CHOSEN_RECOVERY_TO_SHOW", rowData));
                    searchResult();
                }
            });
            return row ;
        });

        dischargeSummary.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Recovery , String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Recovery, String> param) {
                try {
                    String dis = param.getValue().getDischargeSummary();
                    return new SimpleObjectProperty<>(dis);
                } catch (Recovery.RecoveryNullFieldException e) {
                    return new SimpleObjectProperty<>("-----");
                }
            }
        });

        endDateColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Recovery , String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Recovery, String> param) {
                try {
                    Date d = param.getValue().getEndDate();
                    return new SimpleObjectProperty<>(d.toString());
                } catch (Recovery.RecoveryNullFieldException e) {
                    return new SimpleObjectProperty<>("Ricovero attivo");
                }
            }
        });
    }


    @FXML protected void updateRecoveries(String nameandsurname) {
        try {
            String[] arr = nameandsurname.split(" ");
            String name = arr[0];
            String surname = arr[1];

            Patient p = store.poll().getPatients().stream()
                    .filter(pa -> pa.getName().toLowerCase().equals(name.toLowerCase())
                            && pa.getSurname().toLowerCase().equals(surname.toLowerCase())).findFirst().orElse(null);

            if (p != null) {
                //searchPatient.setText(name + " " + surname);
                ObservableList<Recovery> data = recoveryTable.getItems();
                data.removeAll(data);
                //todo check
                p.getAllRecoveries().forEach(r -> {
                    Date extreme = new Date(r.getStartDate().getTime() - 7*24*60*60*1000);
                    if(r.getStartDate().after(extreme))
                        data.add(r);
                });
            } else {
                searchPatient.clear();
            }
        }  catch (ArrayIndexOutOfBoundsException err) {
            searchPatient.clear();
        }
    }

    @FXML protected void searchPatient() {
        this.updateRecoveries(searchPatient.getText());
        //this.searchPatient.clear();
    }

//--------------------------------------------------------
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

    @FXML protected void searchResult() {
        sys.setInterface("NURSR", NURComponent.NURTitle);
    }

    @FXML protected void logout() {
        store.update(new StringCommand("LOGOUT"));
        sys.setInterface("login", LoginComponent.loginTitle);
    }

    @FXML protected void close() {
        sys.endSystem();
    }
}
