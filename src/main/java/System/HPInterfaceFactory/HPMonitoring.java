package System.HPInterfaceFactory;

public class HPMonitoring implements HPInterface{
    private String file;

    public HPMonitoring() {
        this.file = "/HP_Monitoring.fxml";
    }

    public String getFile() {
        return this.file;
    }
}
