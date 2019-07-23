package InterfaceController;

import Generator.Sickness;
import Main.Tuple;
import State.StateEvent;
import State.Store;
import State.StringCommand;
import System.Sistema;
import io.reactivex.subjects.Subject;
import javafx.fxml.FXML;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

public class AlarmsController {
    private final Sistema sys = Sistema.getInstance();
    private Store<StringCommand> store;

    public AlarmsController(Store store, Subject<StateEvent> stream) {
        this.store = store;
    }


    @FXML protected void aritmia(){
        store.update(new StringCommand("EVOLVE_GENERATOR", Sickness.ARITMIA));

    }

    @FXML protected void tachicardia(){
        store.update(new StringCommand("EVOLVE_GENERATOR", Sickness.TACHICARDIA));

    }

    @FXML protected void ipotermia(){
        store.update(new StringCommand("EVOLVE_GENERATOR", Sickness.IPOTERMIA));

    }

    @FXML protected void ipertermia(){
        store.update(new StringCommand("EVOLVE_GENERATOR", Sickness.IPERTERMIA));

    }

    @FXML protected void ipotensione(){
        store.update(new StringCommand("EVOLVE_GENERATOR", Sickness.IPOTENSIONE));
    }

    @FXML protected void ipertensione(){
        store.update(new StringCommand("EVOLVE_GENERATOR", Sickness.IPERTENSIONE));
    }

    @FXML protected void flutter(){
        store.update(new StringCommand("ALARM_ACTIVATED", new Tuple<>(3, Sickness.FLUTTER)));

    }
}
