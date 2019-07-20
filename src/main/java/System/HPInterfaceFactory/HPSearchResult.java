package System.HPInterfaceFactory;

public class HPSearchResult implements HPInterface{
    private String file;

    public HPSearchResult() {
        this.file = "/HP_Search2.fxml";
    }

    public String getFile() {
        return this.file;
    }
}
