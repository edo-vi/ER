package InterfaceController.DOCControllerFactory;

import Component.DOCComponent;
import Component.LoginComponent;
import State.StateEvent;
import State.Store;
import System.Sistema;
import State.StringCommand;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.Subject;
import javafx.fxml.FXML;

public class DOCAPController implements DOCController {
    private Sistema sys = Sistema.getInstance();
    private Store<StringCommand> store;
    private Subject<StateEvent> stream;
    private Disposable dis;

    public DOCAPController(Store store, Subject<StateEvent> stream) {
        this.store = store;
        this.stream = stream;
        try {
            dis.dispose();
        } catch (NullPointerException e) {

        }
        dis = stream.subscribe(se -> {

        });
    }

    @FXML
    public void initialize() {

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
