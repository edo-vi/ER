package DataGenerator;

import Main.Tuple;
import State.Store;
import System.Sistema;
import State.StringCommand;

import java.util.Random;

public class HeartRateGenerator implements GeneratorInterface {
    private boolean canGenerateAlarm = true;
    private Sistema sys = null;
    private final Random random = new Random();
    private double mean = 80;
    private double variance = 4.4;

    public HeartRateGenerator() {
    }

    public void evolve(Sickness sick) {
        if(sick == Sickness.ARITMIA) {
            mean = 50;
            variance = 3.0;
        } else if(sick == Sickness.TACHICARDIA) {
            mean = 130;
            variance = 5.4;
        } else if(sick == Sickness.FLUTTER) {
            //TODO cambiare scheduling del task
        }
    }

    public void reset() {
        canGenerateAlarm = true;
        mean = 80;
        variance = 4.4;
    }

    public Integer getValue() {
        if(sys == null)
            sys = Sistema.getInstance();

        int x =  (int) (mean + random.nextGaussian()*variance);
        if(canGenerateAlarm && sys.getSickPatient() != null) {
            Store<StringCommand> store = sys.getStore();
            if(x < 60) {
                canGenerateAlarm = false;
                store.update(new StringCommand("ALARM_ACTIVATED", new Tuple<>(1, Sickness.ARITMIA)));
            } else if(x > 100) {
                canGenerateAlarm = false;
                store.update(new StringCommand("ALARM_ACTIVATED", new Tuple<>(1, Sickness.TACHICARDIA)));
            }
        }

        return x;
    }
}
