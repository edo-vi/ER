package Component;

import State.Command;
import State.StateEvent;
import javafx.fxml.FXMLLoader;
import System.Sistema;

public class ErrorComponet<C extends Command> extends Component {
    private FXMLLoader loader;
    private final Sistema sys = Sistema.getInstance();
    public static final String AlarmControlTitle = "ER - Error";

    public ErrorComponet() {
        this.loader = new FXMLLoader(getClass().getResource("/Error_PopUp.fxml"));
    }

    protected void eventHook(StateEvent se) {

    }

    protected final void draw() {
        sys.setInterface("ERR", AlarmControlTitle);
    }

    public final FXMLLoader getLoader() {
        return this.loader;
    }

}
