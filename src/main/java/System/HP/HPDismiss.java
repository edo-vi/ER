package System.HP;

import State.Command;

public class HPDismiss<C extends Command> implements HPInterface{
    private String file;

    public HPDismiss() {
        this.file = "/HP_Default.fxml";
    }

    public String getFile() {
        return this.file;
    }
}
