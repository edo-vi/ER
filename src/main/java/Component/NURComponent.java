package Component;

import InterfaceController.NURControllerFactory.NURController;
import State.Command;
import State.StateEvent;
import System.InterfaceFactories.NURInterfaceFactory.NURInterface;
import javafx.fxml.FXMLLoader;
import System.Sistema;

public class NURComponent<C extends Command> extends Component {
    private FXMLLoader loader;
    private final Sistema sys = Sistema.getInstance();
    public static final String NURTitle = "ER - Nurse";

    public NURComponent(NURInterface ui, NURController ctl) {
        this.loader = new FXMLLoader(getClass().getResource(ui.getFile()));
        loader.setController(ctl);
    }


    protected void eventHook(StateEvent se) {

    }

    protected final void draw() {
        sys.setInterface("NURD", NURTitle);
    }

    public final FXMLLoader getLoader() {
        return this.loader;
    }
}
