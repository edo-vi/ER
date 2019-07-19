package System.Session;

import State.StringCommand;
import State.Store;
import javafx.application.Platform;

import java.util.Timer;
import java.util.TimerTask;

public class DoctorSessionThread extends Thread {
    private Store<StringCommand> store;
    private volatile boolean run = true;
    private Timer timerTask;
    private final int sessionLength = 30000; //30s

    public DoctorSessionThread(Store<StringCommand> store) {
        this.store = store;
        timerTask = new Timer();
    }

    public void start() {
        this.timerTask.scheduleAtFixedRate(new SessionTimer(), sessionLength, sessionLength);
    }

    public void interrupt() {
        run = false;
    }

    public void restart() {
        if (!run)
            run = true;
    }

    private class SessionTimer extends TimerTask {

        public SessionTimer() {};

        public void run() {
            Platform.runLater(() -> {
                if(run) {
                    store.update(new StringCommand("SESSION_TERMINATED"));
                }
            });
        }
    }
}
