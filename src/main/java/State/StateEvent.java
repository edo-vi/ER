package State;

public class StateEvent<C extends Command> {
    private final C sc;
    private final State s;

    StateEvent(C sc, State s) {
        this.sc = sc;
        this.s = s;
    }
    public C command() {
        return this.sc;
    }
    public State state() {
        return this.s;
    }
}
