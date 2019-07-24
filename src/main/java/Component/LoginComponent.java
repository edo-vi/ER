package Component;

import InterfaceController.LoginController;
import State.Command;
import State.StateEvent;
import javafx.fxml.FXMLLoader;
import State.Store;
import System.Sistema;

public class LoginComponent<C extends Command> extends Component {
    private FXMLLoader loader;
    private final Sistema sys = Sistema.getInstance();
    public static final String loginTitle = "SICURA - Login";

    public LoginComponent() {
        LoginController logctl = new LoginController(sys.getStore(), sys.getStore().getEventStream());
        loader = new FXMLLoader(getClass().getResource("/Login.fxml"));
        loader.setController(logctl);
    }

    public LoginComponent(Store<C> store) {
        super(store);
    }

    protected void eventHook(StateEvent se) {

    }

    protected void draw() {
        sys.setInterface("login", loginTitle);
    }

    public FXMLLoader getLoader() {
        return this.loader;
    }
}


