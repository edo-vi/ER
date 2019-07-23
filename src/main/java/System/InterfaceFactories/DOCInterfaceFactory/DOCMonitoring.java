package System.InterfaceFactories.DOCInterfaceFactory;

public class DOCMonitoring implements DOCInterface {
    private String file;

    public DOCMonitoring() {
        this.file = "/D_Monitoring.fxml";
    }

    public String getFile() {
        return this.file;
    }
}
