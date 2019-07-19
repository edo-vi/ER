package State;

import Main.Tuple;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

import java.io.*;
import java.util.function.BiFunction;

/**
 *
 */
public class Store<C extends Command> {
    private State state;
    private Reducer<C> reducer;
    private int counter = 0;
    private Middleware<C> middleware;
    private Writer writer;
    //streams
    private PublishSubject<StateEvent> state$ = PublishSubject.create();
    private PublishSubject<String> commands$= PublishSubject.create();

    public Store(State state, Reducer<C> reducer, Middleware<C> middleware, Tuple<String, BiFunction<C, State,State>>...args) {
        //TODO remove -- logging
        this.state$.subscribe(s -> {
            String name = s.command().name();
            if (!name.equals("GENERATE_BP") && !name.equals("GENERATE_HEART_RATE") && !name.equals("GENERATE_TEMPERATURE")) {
                System.out.println(String.valueOf(counter++) + " | " + s.command() + "\n" + s.state());
            }
        }


        );

        this.middleware = middleware;
        this.state = state;
        //this.state$.onNext(new StateEvent(new C, state));
        this.reducer = reducer;
        // <Command, Function> tuples can be passed here to the reducer
        if (args.length != 0) this.reducer = this.reducer.with(args);

        //this.reducer.setStore(this);

    }
    /*
    * TODO check this, may not follow reactive paradigm
     */
    public State poll() {
        return this.state;
    }

    /**
     * Upon calling this method the Store updates the state
     * as indicated by command and broadcast the change to all
     * subscribers
     *
     * @param command command that specifies how the state is updated
     * @implNote synchronized because it shall not be called by multiple threads
     *    at the same time as it can produce data inconsistencies because
     *    of the non deterministic order in which this.state is called
     */
    synchronized public void update(C command) {
        State state;
        C comm = command;
        State resultReducer;
        try {
            resultReducer = this.reducer.run(this.state, command);
        } catch (ReducerString.NonExistentCommandException e) {
            this.state$.onNext(new StateEvent(command.errorCommand(), this.state));
            return;
        }
        if (this.middleware.check(command.name())) {
            Tuple<C, State> resultMiddleware = this.middleware.run(resultReducer, command);
            state = resultMiddleware.snd();
            comm = resultMiddleware.fst();
        } else {
            state = resultReducer;
        }

        this.state = state;
        this.state$.onNext(new StateEvent<C>(comm, state));
        // notify subscribers about the state change

    }

    public Subject<StateEvent> getEventStream() {
        return state$;
    }

    public Subject<String> getCommandStream() {
        return commands$;
    }

    public State getState() {
        return this.state;
    }
}
