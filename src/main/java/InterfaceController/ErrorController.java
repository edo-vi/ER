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

    public ErrorController(Store<StringCommand> store, Subject<StateEvent> stream) {
        this.store = store;

        try {
            dis.dispose();
        } catch (NullPointerException e) {}

        dis = stream.subscribe(se -> {
            String command = se.command().name();
            if(command.equals("ERROR_WINDOW_CREATED"))
                Platform.runLater(() -> errorText.setText(((String) se.command().getArg())));
        });
    }

    @FXML protected void close() {
        store.update(new StringCommand("CLOSE_ERROR_WINDOW"));
    }
}
