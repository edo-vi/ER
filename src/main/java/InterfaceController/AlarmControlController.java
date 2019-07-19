package InterfaceController;

import Entities.User;
import Generator.Sickness;
import Main.Triple;
import Main.Tuple;
import State.StateEvent;
import State.Store;
import State.StringCommand;
import System.Sistema;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.Subject;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class AlarmControlController {
    private final Sistema sys = Sistema.getInstance();
    private Store<StringCommand> store;
    @FXML private Label messageLabel;
    @FXML private TextField userField;
    @FXML private PasswordField passField;
    private Disposable dis;
    Subject<StateEvent> stream;

    public AlarmControlController(Store store, Subject<StateEvent> stream) {
        this.store = store;
        this.stream = stream;

        try {
            dis.dispose();
        } catch(NullPointerException e) {}

        dis = this.stream.subscribe(se -> {
            if (se.command().name().equals("ALM_LOGIN_SUCCESS") || se.command().name().equals("ALM_LOGIN_FAILURE")) {
                userField.clear();
                passField.clear();
                if (se.command().name().equals("ALM_LOGIN_SUCCESS")) {
                    store.update(new StringCommand("RESET_ALARMS"));
                } else {
                    System.out.println("Invalid username and/or password");
                }
            }
            if(se.command().equals("ALARM_ACTIVATED"))
                Platform.runLater(() -> {
                    Tuple<Integer, Sickness> elem = (Tuple) se.command().getArg();
                    messageLabel.setText("Attenzione! Allarme di livello" + elem.fst() + "\n" +
                            elem.snd() + " paziente " + sys.getSickPatient().getName() + " " + sys.getSickPatient().getSurname() + "\n" +
                            "Richiesto l'intervento del dottor " + se.state().getDocAlarm().getName());
                });
        });
    }

    @FXML protected void login() {
        store.update(new StringCommand("ALARM_LOGIN", new User(userField.getText(), passField.getText())));
    }
}
