package Component;

import InterfaceController.MonitoringController;
import State.Command;
import State.StateEvent;
import State.Store;
import javafx.fxml.FXMLLoader;
import System.Sistema;

public class MonitoringComponent<C extends Command> extends Component {
    private FXMLLoader loader;
    private final Sistema sys = Sistema.getInstance();
    public static final String monitoringTitle = "SICURA - Monitoring";

    public MonitoringComponent() {
        MonitoringController monctl = new MonitoringController(sys.getStore(), sys.getStore().getEventStream());
        loader = new FXMLLoader(getClass().getResource("/Monitoring.fxml"));
        loader.setController(monctl);
    }

    public MonitoringComponent(Store<C> store) {
        super(store);
    }

    protected void eventHook(StateEvent se) {

    }

    protected void draw() {
        sys.setInterface("MON", monitoringTitle);
    }

    public FXMLLoader getLoader() {
        return this.loader;
    }
}
