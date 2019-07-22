package Component;

import State.State;
import State.StateEvent;
import State.Store;
import State.Command;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;import javafx.application.Application;
import javafx.fxml.FXMLLoader;

public abstract class Component<C extends Command> {

    private Store <C> store;
    private Disposable subscription;

    public Component() {}

    public Component(Store <C> store) {
        this.store = store;
        this.subscription = this.store.getEventStream()
                .onErrorResumeNext(Observable.empty())
                .subscribe(
                        s -> eventHook(s) ,
                        throwable -> System.out.println(throwable)  );
        initialization(this.store.poll());

    }

    protected State getState() {
        return this.store.poll();
    }

    protected abstract void draw() throws Exception;

    private void clean() {
        this.subscription.dispose();
    }
    protected final void sendCommand(C c) {
        this.store.update(c);
    }

    protected abstract void eventHook(StateEvent se);
    protected void initialization(State state) {
        try {
            draw();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Something went wrong drawing the scene");
        }
    }

    /**
     * GETTERS AND SETTERS
     */
    public Store<C> getStore() {
        return store;
    }

}
