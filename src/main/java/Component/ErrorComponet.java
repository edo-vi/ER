package Component;

import InterfaceController.ErrorController;
import State.Command;
import State.StateEvent;
import javafx.fxml.FXMLLoader;
import System.Sistema;

public class ErrorComponet<C extends Command> extends Component {
    private FXMLLoader loader;
    private final Sistema sys = Sistema.getInstance();
    public static final String ErrorTitle = "SICURA - Error";

    public ErrorComponet() {
        ErrorController errctl = new ErrorController(sys.getStore(), sys.getStore().getEventStream());
        this.loader = new FXMLLoader(getClass().getResource("/Error_PopUp.fxml"));
        loader.setController(errctl);
    }

    protected void eventHook(StateEvent se) {

    }

    protected final void draw() {
        sys.setInterface("ERR", ErrorTitle);
    }

    public final FXMLLoader getLoader() {
        return this.loader;
    }

}
