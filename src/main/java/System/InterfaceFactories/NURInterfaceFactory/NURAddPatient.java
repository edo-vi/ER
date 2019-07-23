package System.InterfaceFactories.NURInterfaceFactory;

public class NURAddPatient implements NURInterface {
    private String file;

    public NURAddPatient() {
        this.file = "/N_AddPatient.fxml";
    }

    public String getFile() {
        return this.file;
    }
}
