package State;

public interface Command {
    String name();
    //UUID issuer();
    Object getArg();
    Command errorCommand();
}

