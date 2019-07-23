package System.InterfaceFactories.DOCInterfaceFactory;

public class DOCDefault implements DOCInterface {
    private String file;

    public DOCDefault() {
        this.file = "/D_Default.fxml";
    }

    public String getFile() {
        return this.file;
    }
}
