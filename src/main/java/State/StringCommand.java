package State;

public class StringCommand implements Command {
    private final String s;
    private final Object obj;
    private static final StringCommand err = new StringCommand("NON_EXISTENT_COMMAND");

    public StringCommand(String s, Object obj) {
        this.s = s;
        this.obj = obj;
    }

    public StringCommand(String s) {
        this(s, null);
    }

    @Override
    public String name() {
        return s;
    }

    @Override
    public Object getArg() {
        return obj;
    }

    @Override
    public StringCommand errorCommand() {
        return err;
    }

    public String toString() {
        return this.name();
    }
}