package System.DOCInterfaceFactory;

public class DOCSearch implements DOCInterface {
    private String file;

    public DOCSearch() {
        this.file = "/D_Search.fxml";
    }

    public String getFile(){
        return this.file;
    }
}
