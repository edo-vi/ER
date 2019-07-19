package InterfaceController;

import Component.HPComponent;
import Entities.Role;
import Entities.User;
import State.StateEvent;
import State.Store;
import State.StringCommand;
import System.Sistema;
import io.reactivex.subjects.Subject;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class AlarmControlController {
    private final Sistema sys = Sistema.getInstance();
    private Store<StringCommand> store;
    @FXML private TextField userField;
    @FXML private PasswordField passField;

    public AlarmControlController(Store store, Subject<StateEvent> stream) {
        this.store = store;
        stream.subscribe(se -> {
            if (se.command().name().equals("ALM_LOGIN_SUCCESS") || se.command().name().equals("ALM_LOGIN_FAILURE")) {
                userField.clear();
                passField.clear();
                if (se.command().name().equals("ALM_LOGIN_SUCCESS")) {
                    store.update(new StringCommand("RESET_ALARMS"));
                } else {
                    System.out.println("Invalid username and/or password");
                }
            }
        });
    }

    @FXML protected void login() {
        store.update(new StringCommand("ALARM_LOGIN", new User(userField.getText(), passField.getText())));
    }
}
