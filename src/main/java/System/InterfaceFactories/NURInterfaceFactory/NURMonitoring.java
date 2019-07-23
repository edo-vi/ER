package System.InterfaceFactories.NURInterfaceFactory;

public class NURMonitoring implements NURInterface {
    private String file;

    public NURMonitoring() {
        this.file = "/N_Monitoring.fxml";
    }

    public String getFile() {
        return this.file;
    }
}
