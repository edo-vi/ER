package System.Session;

import javafx.application.Platform;
import State.StringCommand;
import State.Store;

import java.util.Timer;
import java.util.TimerTask;

public class AlarmTimer extends Thread {
    private Store<StringCommand> store;
    private long alarmLifeTime;
    private volatile boolean run = true;
    private Timer timerTask;

    public AlarmTimer(int level, Store<StringCommand> store) {
        this.store = store;
        timerTask = new Timer();

        if(level == 3)
            this.alarmLifeTime = 30*1000;
        else if(level == 2)
            this.alarmLifeTime = 60*1000;
        else this.alarmLifeTime = 90*1000;
    }

    public void start() {
        this.timerTask.scheduleAtFixedRate(new AlarmTimerThread(), alarmLifeTime, alarmLifeTime);
    }

    public void alarmDeactivated() {
        run = false;
    }

    public void restart() {
        if (!run)
            run = true;
    }

    private class AlarmTimerThread extends TimerTask {
        public AlarmTimerThread() {}

        public void run() {
            Platform.runLater(() -> store.update(new StringCommand("ERROR", "F in the chat\nRIP")));
        }
    }
}