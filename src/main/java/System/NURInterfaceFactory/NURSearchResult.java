package System.NURInterfaceFactory;

public class NURSearchResult implements NURInterface {
    private String file;

    public NURSearchResult() {
        this.file = "/N_Search2.fxml";
    }

    public String getFile() {
        return this.file;
    }
}
