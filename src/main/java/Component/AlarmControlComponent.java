package Component;

import InterfaceController.AlarmControlController;
import InterfaceController.AlarmLoggedControlController;
import State.Command;
import State.StateEvent;
import System.Sistema;
import javafx.fxml.FXMLLoader;

public class AlarmControlComponent<C extends Command> extends Component {
    private FXMLLoader loader;
    private final Sistema sys = Sistema.getInstance();
    public static final String AlarmControlTitle = "SICURA - Alarm Control";

    public AlarmControlComponent(boolean logged) {
        if(logged) {
            AlarmLoggedControlController almctl = new AlarmLoggedControlController(sys.getStore(), sys.getStore().getEventStream());
            this.loader = new FXMLLoader(getClass().getResource("/Alarm_PopUp.fxml"));
            loader.setController(almctl);
        } else {
            AlarmControlController almctl = new AlarmControlController(sys.getStore(), sys.getStore().getEventStream());
            this.loader = new FXMLLoader(getClass().getResource("/Login_PopUp.fxml"));
            loader.setController(almctl);
        }
    }


    protected void eventHook(StateEvent se) {

    }

    protected final void draw() {
        sys.setInterface("ALMCTL", AlarmControlTitle);
    }

    public final FXMLLoader getLoader() {
        return this.loader;
    }
}
