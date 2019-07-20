package System.HPInterfaceFactory;

import State.Command;

public class HPSearchResult<C extends Command> implements HPInterface{
    private String file;

    public HPSearchResult() {
        this.file = "/HP_Search2.fxml";
    }

    public String getFile() {
        return this.file;
    }
}
