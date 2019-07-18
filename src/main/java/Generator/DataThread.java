package Generator;

import java.util.*;

import State.Store;
import State.StringCommand;

public class DataThread extends Thread {
    private volatile boolean run = true;
    private final Store<StringCommand> store;
    private final int numTask = 3;
    private final Long[] times = {2000L, 3000L, 5000L}; //2-2-3-5 (s)
    private final Value[] taskJob = {Value.BP, Value.HEART_RATE, Value.TEMPERATURE};
    private ArrayList<TimerTask> tasks = new ArrayList<>();
    private ArrayList<Timer> timers = new ArrayList<>();

    public DataThread(Store<StringCommand> store) {
        this.store = store;
        for(int i=0; i<numTask; i++) {
            this.tasks.add(new DataTask(taskJob[i]));
            this.timers.add(new Timer());
        }
    }

    public void start() {
        for(int i=0; i<numTask; i++)
            this.timers.get(i).scheduleAtFixedRate(this.tasks.get(i), new Date(), times[i]);
    }

    public void interrupt() {
        this.run = false;
    }

    public void restart() {
        if (!run) run = true;
    }

    private class DataTask extends TimerTask {
        private Value value;

        public DataTask(Value v) {
            this.value = v;
        }
        public void run() {
            if (run) {
                if (value == Value.BP) {
                    store.update(new StringCommand("GENERATE_BP"));
                }
                else if (value == Value.HEART_RATE) {
                    store.update(new StringCommand("GENERATE_HEART_RATE"));
                }
                else {
                    store.update(new StringCommand("GENERATE_TEMPERATURE"));
                }
            }

        }
    }
}
