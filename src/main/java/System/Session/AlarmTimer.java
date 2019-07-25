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
    private AlarmTimerThread alarmThread;
    private int alarmLevel;

    public AlarmTimer(int level, Store<StringCommand> store) {
        this.store = store;
        this.alarmLevel = level;
        timerTask = new Timer();

        if(level == 3)
            this.alarmLifeTime = 30*1000;
        else if(level == 2)
            this.alarmLifeTime = 60*1000;
        else this.alarmLifeTime = 90*1000;
    }

    public void start() {
        alarmThread = new AlarmTimerThread();
        this.timerTask.scheduleAtFixedRate(alarmThread, alarmLifeTime, alarmLifeTime);
    }

    public void changeLevel(int newLevel) {
        alarmLevel = newLevel;
        if(newLevel == 3)
            this.alarmLifeTime = 30*1000;
        else if(newLevel == 2)
            this.alarmLifeTime = 60*1000;
        else this.alarmLifeTime = 90*1000;
    }

    public void alarmDeactivated() {
        alarmThread.cancel();
        run = false;
    }

    public void restart() {
        if (!run)
            run = true;
        alarmThread.cancel();
        start();
    }

    private class AlarmTimerThread extends TimerTask {
        private final String mgsDeath = "Avete perso il paziente!\nIn caso di necessità questo è il\nnumero delle onoranze funebri: +39 348 3965808";
        private final String msgAlert = "Si consiglia di chiamare un medico.\nIl paziente ha i minuti contati!!";
        private String msg;
        public AlarmTimerThread() {
            if(alarmLevel == 3)
                msg = mgsDeath;
            else msg = msgAlert;
        }

        public void run() {
            Platform.runLater(() -> {
                if(run)
                    store.update(new StringCommand("ERROR", msg));
            });
        }
    }
}
