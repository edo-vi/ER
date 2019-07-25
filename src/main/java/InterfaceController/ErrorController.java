package InterfaceController;

import State.StateEvent;
import State.Store;
import State.StringCommand;
import System.Sistema;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.Subject;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ErrorController {
    private Sistema sys = Sistema.getInstance();
    private Store<StringCommand> store;
    private Disposable dis;
    @FXML private Label errorText;
    private boolean areAlarmsActive = false;

    public ErrorController(Store<StringCommand> store, Subject<StateEvent> stream) {
        this.store = store;

        try {
            dis.dispose();
        } catch (NullPointerException e) {}

        dis = stream.subscribe(se -> {
            String command = se.command().name();
            if(command.equals("ERROR_WINDOW_CREATED"))
                Platform.runLater(() -> errorText.setText(((String) se.command().getArg())));
            if(command.equals("ACTIVE_ALARM"))
                areAlarmsActive = true;
            if(command.equals("ALM_LOGIN_FAILURE"))
                areAlarmsActive = false;
        });
    }

    @FXML protected void close() {
        store.update(new StringCommand("CLOSE_ERROR_WINDOW"));
        if(areAlarmsActive) {
            areAlarmsActive = false;
            store.update(new StringCommand("RESET_ALARMS"));
            store.update(new StringCommand("STOP_COUNTDOWN"));
        }
    }
}
