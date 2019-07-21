package InterfaceController.NURControllerFactory;

import Component.LoginComponent;
import Component.NURComponent;
import State.StateEvent;
import State.Store;
import State.StringCommand;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.Subject;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import System.Sistema;
import javafx.scene.control.TextField;

import java.util.Date;

public class NURAPController implements NURController {
    private Store<StringCommand> store;
    private Sistema sys = Sistema.getInstance();
    private Disposable dis;
    @FXML private Label nurseNameLabel;
    @FXML private TextField surnameLabel;
    @FXML private TextField nameLabel;
    @FXML private RadioButton male;
    @FXML private RadioButton female;
    @FXML private DatePicker bDayDate;
    @FXML private TextField placeLabel;
    @FXML private TextField cfLabel;

    public NURAPController(Store<StringCommand> store, Subject<StateEvent> stream) {
        this.store = store;

        try {
            dis.dispose();
        } catch (NullPointerException e) {}

        dis = stream.subscribe(se -> {
            Platform.runLater(() -> nurseNameLabel.setText(se.state().getUser().toString()));
        });
    }

    @FXML protected void submit() {
        try {
            if(surnameLabel.getText() != null &&
                    nameLabel.getText() != null &&
                    (male.isSelected() || female.isSelected()) &&
                    new java.sql.Date(new Date().getTime()).before(new java.sql.Date(bDayDate.getValue().toEpochDay())) &&
                    placeLabel.getText() != null &&
                    cfLabel.getText() != null)
                System.out.println("ALL DATA IS CORRECT");
            else throw new IllegalArgumentException();
        } catch (Exception e) {
            nameLabel.clear();
            surnameLabel.clear();
            if(male.isSelected())
                male.setSelected(false);
            if(female.isSelected())
                female.setSelected(false);
            bDayDate.setValue(null);
            placeLabel.clear();
            cfLabel.clear();
            System.out.println("NOT ALL DATA IS CORRECT OR INSERTED");
        }
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
}
