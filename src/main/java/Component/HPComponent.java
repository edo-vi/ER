package Component;

import InterfaceController.HPControllerFactory.*;
import State.Command;
import State.StateEvent;
import System.HPInterfaceFactory.HPInterface;
import javafx.fxml.FXMLLoader;
import System.Sistema;

public class HPComponent<C extends Command> extends Component {
    private FXMLLoader loader;
    private final Sistema sys = Sistema.getInstance();
    public static final String HPTitle = "ER - HeadPhysician";

    public HPComponent(HPInterface ui, HPController ctl) {
        this.loader = new FXMLLoader(getClass().getResource(ui.getFile()));
        loader.setController(ctl);
    }


    protected void eventHook(StateEvent se) {

    }

    protected final void draw() {
        sys.setInterface("HPDF", HPTitle);
    }

    public final FXMLLoader getLoader() {
        return this.loader;
    }
}
