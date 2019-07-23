package System.Session;

import State.StringCommand;
import State.Store;
import javafx.application.Platform;
import org.hibernate.Session;

import java.util.Timer;
import java.util.TimerTask;

public class DoctorSessionThread extends Thread {
    private Store<StringCommand> store;
    private volatile boolean run = true;
    private Timer timerTask;
    private final int sessionLength = 30000; //30s
    private SessionTimer sessionThread;

    public DoctorSessionThread(Store<StringCommand> store) {
        this.store = store;
        timerTask = new Timer();
    }

    public void start() {
        sessionThread = new SessionTimer();
        this.timerTask.scheduleAtFixedRate(sessionThread, sessionLength, sessionLength);
    }

    public void interrupt() {
        sessionThread.cancel();
        run = false;
    }

    public void restart() {
        if (!run)
            run = true;
        start();
    }

    private class SessionTimer extends TimerTask {

        public SessionTimer() {}

        public void run() {
            Platform.runLater(() -> {
                if(run) {
                    store.update(new StringCommand("SESSION_TERMINATED"));
                }
            });
        }
    }
}
