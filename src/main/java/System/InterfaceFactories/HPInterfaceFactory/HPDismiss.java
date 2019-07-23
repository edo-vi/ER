package System.InterfaceFactories.HPInterfaceFactory;

public class HPDismiss implements HPInterface{
    private String file;

    public HPDismiss() {
        this.file = "/HP_Default.fxml";
    }

    public String getFile() {
        return this.file;
    }
}
