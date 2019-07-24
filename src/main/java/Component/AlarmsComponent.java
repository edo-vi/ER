package Component;

import InterfaceController.AlarmsController;
import State.Command;
import State.StateEvent;
import System.Sistema;
import javafx.fxml.FXMLLoader;


public class AlarmsComponent<C extends Command> extends Component {
    private FXMLLoader loader;
    private final Sistema sys = Sistema.getInstance();
    public static final String AlarmsTitle = "SICURA - Alarms";

    public AlarmsComponent() {
        AlarmsController almctl = new AlarmsController(sys.getStore(), sys.getStore().getEventStream());
        this.loader = new FXMLLoader(getClass().getResource("/Alarms.fxml"));
        loader.setController(almctl);
    }


    protected void eventHook(StateEvent se) {

    }

    protected final void draw() {
        sys.setInterface("ALM", AlarmsTitle);
    }

    public final FXMLLoader getLoader() {
        return this.loader;
    }
}
