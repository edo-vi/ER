package InterfaceController;

import Component.DOCComponent;
import Component.NURComponent;
import Entities.Role;
import Entities.User;
import State.StateEvent;
import State.StringCommand;
import Component.HPComponent;
import System.Sistema;
import io.reactivex.subjects.Subject;
import javafx.fxml.FXML;
import State.Store;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;


public class LoginController {
    private Sistema sys = Sistema.getInstance();
    private Store<StringCommand> store;
    @FXML private TextField userField;
    @FXML private PasswordField passField;

    public LoginController(Store store, Subject<StateEvent> stream) {
        this.store = store;
        stream.subscribe(se -> {
            String command = se.command().name();
            if (command.equals("LOGIN_SUCCESS") || command.equals("LOGIN_FAILURE")) {
                userField.clear();
                passField.clear();
                if (command.equals("LOGIN_SUCCESS")) {
                    Role role = se.state().getUser().getRole();
                    if(role == Role.HEAD_PHYSICIAN)
                        sys.setInterface("HPD", HPComponent.HPTitle);
                    else if(role == Role.DOCTOR)
                        sys.setInterface("DOCD", DOCComponent.DOCTitle);
                    else sys.setInterface("NURD", NURComponent.NURTitle);
                } else {
                    store.update(new StringCommand("ERROR", "Username e Password non validi"));
                }
            }
        });
    }

    @FXML protected void login() {
        store.update(new StringCommand("LOGIN", new User(userField.getText(), passField.getText())));
    }

    @FXML protected void close() {
        sys.endSystem();
    }

    @FXML protected void buttonPressed(KeyEvent e)
    {
        if(e.getCode().toString().equals("ENTER"))
            login();
    }

    @FXML protected void startMonitoring() {
        store.update(new StringCommand("SHOW_MONITORING"));
        store.update(new StringCommand("START_MONITORING"));
    }
}
