package System.HPInterfaceFactory;

public class HPSearch implements HPInterface{
    private String file;

    public HPSearch() {
        this.file = "/HP_Search.fxml";
    }

    public String getFile() {
        return this.file;
    }
}
