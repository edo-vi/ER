package System.InterfaceFactories.NURInterfaceFactory;

public class NURDefault implements NURInterface {
    private String file;

    public NURDefault() {
        this.file = "/N_Default.fxml";
    }

    public String getFile() {
        return this.file;
    }
}
