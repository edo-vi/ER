package System.InterfaceFactories.DOCInterfaceFactory;

public class DOCSearchResult implements DOCInterface {
    private String file;

    public DOCSearchResult() {
        this.file = "/D_Search2.fxml";
    }

    public String getFile() {
        return this.file;
    }
}
