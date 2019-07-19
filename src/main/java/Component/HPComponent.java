package Component;

import InterfaceController.HPMController;
import InterfaceController.HPSController;
import InterfaceController.HPDController;
import InterfaceController.HPSRController;
import State.Command;
import State.StateEvent;
import System.HP.HPFactory;
import javafx.fxml.FXMLLoader;
import System.Sistema;

public class HPComponent<C extends Command> extends Component {
    private FXMLLoader loader;
    private final Sistema sys = Sistema.getInstance();
    public static final String HPTitle = "ER - HeadPhysician";

    public HPComponent(String type) {
        if (type.equals("default")) {
            HPDController hpctl = new HPDController(sys.getStore(), sys.getStore().getEventStream());
            this.loader = new FXMLLoader(getClass().getResource(new HPFactory().getHPInterface(type)));
            loader.setController(hpctl);
        } else if (type.equals("search")) {
            HPSController hpctl = new HPSController(sys.getStore(), sys.getStore().getEventStream());
            this.loader = new FXMLLoader(getClass().getResource(new HPFactory().getHPInterface(type)));
            loader.setController(hpctl);
        } else if (type.equals("searchResult")) {
            HPSRController hpctl = new HPSRController(sys.getStore(), sys.getStore().getEventStream());
            this.loader = new FXMLLoader(getClass().getResource(new HPFactory().getHPInterface(type)));
            loader.setController(hpctl);
        } else if (type.equals("monitoring")) {
            HPMController hpctl = new HPMController(sys.getStore(), sys.getStore().getEventStream());
            this.loader = new FXMLLoader(getClass().getResource(new HPFactory().getHPInterface(type)));
            loader.setController(hpctl);
        }

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
