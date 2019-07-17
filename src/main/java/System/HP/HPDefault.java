package System.HP;

import State.Command;

public class HPDefault<C extends Command> implements HPInterface{
    private String file;

    public HPDefault() {
        this.file = "/A_HP_Default.fxml";
    }

    public String getFile() {
        return this.file;
    }
}
