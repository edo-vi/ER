package InterfaceController;

import Entities.User;
import Generator.Sickness;
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
            String command = se.command().name();
            if (command.equals("ALM_LOGIN_SUCCESS") || command.equals("ALM_LOGIN_FAILURE")) {
                userField.clear();
                passField.clear();
                if (se.command().name().equals("ALM_LOGIN_SUCCESS")) {
                    store.update(new StringCommand("RESET_ALARMS"));
                    store.update(new StringCommand("STOP_COUNTDOWN"));
                    store.update(new StringCommand("SESSION_START"));
                } else {
                    System.out.println("Invalid username and/or password");
                }
            } else if(command.equals("ACTIVE_ALARM")) {
                Platform.runLater(() -> {
                    Tuple<Integer, Sickness> elem = (Tuple) se.command().getArg();
                    messageLabel.setText("Attenzione! Allarme di livello " + elem.fst() + "\n" +
                            elem.snd() + " paziente " +
                            ((elem.snd().equals(Sickness.FLUTTER)) ? se.state().getActiveRecoveries().get(0).getPatient().getName() : sys.getSickPatient().getName()) +
                            " " +
                            ((elem.snd().equals(Sickness.FLUTTER)) ? se.state().getActiveRecoveries().get(0).getPatient().getSurname() : sys.getSickPatient().getSurname()) +
                            "\n" +
                            "Richiesto l'intervento di un dottore");
                });
                store.update(new StringCommand("ACTIVATE_COUNTDOWN", ((Tuple) se.command().getArg()).fst()));
            }
        });
    }

    @FXML protected void login() {
        store.update(new StringCommand("ALARM_LOGIN", new User(userField.getText(), passField.getText())));
    }
}
