package InterfaceController;

import DataGenerator.Sickness;
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

public class AlarmLoggedControlController {
    private final Sistema sys = Sistema.getInstance();
    private Store<StringCommand> store;
    @FXML private Label messageLabel;
    private Subject<StateEvent> stream;
    private Disposable dis;

    public AlarmLoggedControlController(Store store, Subject<StateEvent> stream) {
        this.store = store;
        this.stream = stream;

        try {
            dis.dispose();
        } catch (NullPointerException e) {}

        dis = this.stream.subscribe(se -> {
           String command = se.command().name();
            if(command.equals("ACTIVE_ALARM")) {
                Platform.runLater(() -> {
                    Tuple<Integer, Sickness> elem = (Tuple) se.command().getArg();
                    messageLabel.setText("Attenzione! Allarme di livello " + elem.fst() + "\n" +
                            elem.snd() + " paziente " +
                            ((elem.snd().equals(Sickness.FLUTTER)) ? se.state().getActiveRecoveries().get(0).getPatient().getName() : sys.getSickPatient().getName()) +
                            " " +
                            ((elem.snd().equals(Sickness.FLUTTER)) ? se.state().getActiveRecoveries().get(0).getPatient().getSurname() : sys.getSickPatient().getSurname()) +
                            "\n" +
                            "Richiesto l'intervento del dottor " + se.state().getDocAlarm().getName());
                });
                store.update(new StringCommand("ACTIVATE_COUNTDOWN", ((Tuple) se.command().getArg()).fst()));
            }
        });
    }

    @FXML protected void login() {
        store.update(new StringCommand("RESET_ALARMS"));
        store.update(new StringCommand("STOP_COUNTDOWN"));
    }
}