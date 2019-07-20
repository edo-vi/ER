package System.HPInterfaceFactory;

import State.Command;

public class HPMonitoring<C extends Command> implements HPInterface{
    private String file;

    public HPMonitoring() {
        this.file = "/HP_Monitoring.fxml";
    }

    public String getFile() {
        return this.file;
    }
}
