package System.HP;

import State.Command;

public class HPSearch<C extends Command> implements HPInterface{
    private String file;

    public HPSearch() {
        this.file = "/HP_Search.fxml";
    }

    public String getFile() {
        return this.file;
    }
}
