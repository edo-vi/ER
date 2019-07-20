package System.NURInterfaceFactory;

public class NURSearch implements NURInterface {
    private String file;

    public NURSearch() {
        this.file = "/N_Search.fxml";
    }

    public String getFile() {
        return this.file;
    }
}
