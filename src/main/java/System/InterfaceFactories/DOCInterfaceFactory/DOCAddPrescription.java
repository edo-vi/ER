package System.InterfaceFactories.DOCInterfaceFactory;

public class DOCAddPrescription implements DOCInterface {
    private String file;

    public DOCAddPrescription() {
        this.file = "/D_AddPrescription.fxml";
    }

    public String getFile() {
        return this.file;
    }
}
