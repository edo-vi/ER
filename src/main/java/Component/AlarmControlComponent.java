package Component;

import State.Command;
import State.StateEvent;
import System.Sistema;
import javafx.fxml.FXMLLoader;

public class AlarmControlComponent<C extends Command> extends Component {
    private FXMLLoader loader;
    private final Sistema sys = Sistema.getInstance();
    public static final String AlarmControlTitle = "ER - Alarm Control";

    public AlarmControlComponent(boolean logged) {
        //AlarmsController almctl = new AlarmsController(sys.getStore(), sys.getStore().getEventStream());
        this.loader = new FXMLLoader(getClass().getResource((logged) ? "/Alarm_PopUp.fxml" : "/Login_PopUp.fxml"));
        //loader.setController(almctl);
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
