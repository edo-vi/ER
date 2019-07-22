package State;

import Generator.DataThread;
import Main.Tuple;
import System.Session.AlarmTimer;
import System.Session.DoctorSessionThread;
import javafx.stage.Stage;

import java.util.HashMap;

public class MiddlewareString implements Middleware<StringCommand> {
    private Stage stage;

    private DataThread monitoring;
    private DoctorSessionThread docSession;
    private AlarmTimer alarmTimerThread;

    public MiddlewareString(Stage stage) {
        this.stage = stage;
    }
    private HashMap<String, TriFunction<StringCommand, State, Middleware<StringCommand>, Tuple<StringCommand, State>>> map =
            new HashMap<String, TriFunction<StringCommand, State, Middleware<StringCommand>, Tuple<StringCommand, State>>>();

    @Override
    public Middleware<StringCommand> with(String st, TriFunction<StringCommand, State,
            Middleware<StringCommand>, Tuple<StringCommand, State>> fun) {
        this.map.put(st, fun);
        return this;
    }

    @Override
    public Tuple<StringCommand, State> run(State s, StringCommand stringCommand) {
        TriFunction<StringCommand, State, Middleware<StringCommand>, Tuple<StringCommand, State>> fun =
                this.map.get(stringCommand.name());
        return fun.apply(stringCommand, s, this);
    }

    public Stage getStage() {
        return this.stage;
    }
    public boolean check(String s) {
        return this.map.containsKey(s);
    }

    public DataThread getMonitoring() {
        return monitoring;
    }

    public void setMonitoring(DataThread monitoring) {
        this.monitoring = monitoring;
    }

    public DoctorSessionThread getDocSession() {
        return docSession;
    }

    public void setDocSession(DoctorSessionThread docSession) {
        this.docSession = docSession;
    }

    public AlarmTimer getAlarmTimerThread() {
        return alarmTimerThread;
    }

    public void setAlarmTimerThread(AlarmTimer alarmTimerThread) {
        this.alarmTimerThread = alarmTimerThread;
    }


}
