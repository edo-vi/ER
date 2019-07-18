package InterfaceController;

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
import javafx.stage.Stage;


public class LoginController {
    private Sistema sys = Sistema.getInstance();
    private Store<StringCommand> store;
    @FXML private TextField userField;
    @FXML private PasswordField passField;

    public LoginController(Store store, Subject<StateEvent> stream) {
        this.store = store;
        stream.subscribe(se -> {
            if (se.command().name().equals("LOGIN_SUCCESS") || se.command().name().equals("LOGIN_FAILURE")) {
                userField.clear();
                passField.clear();
                if (se.state().getUser() == se.state().getUserCheck() && se.state().getUser().getRole() == Role.HEAD_PHYSICIAN) {
                    Sistema.getInstance().setInterface("HPD", HPComponent.HPTitle);
                } else {
                    System.out.println("Invalid username and/or password");
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
        {
            this.login();
        }
    }

    @FXML protected void startMonitoring() {
        store.update(new StringCommand("SHOW_MONITORING"));
        store.update(new StringCommand("START_MONITORING"));
    }
}
