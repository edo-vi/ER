package Generator;

import Main.Tuple;
import State.Store;
import System.Sistema;
import State.StringCommand;

import java.util.Random;

public class HeartRateGenerator implements GeneratorInterface {
    private boolean canGenerateAlarm = true;
    private final Random random = new Random();
    private double mean = 80;
    private double variance = 4.4;

    public HeartRateGenerator() {
    }

    public void evolve(Sickness sick) {
        if(sick == Sickness.BRACHICARDIA) {
            mean = 50;
            variance = 3.0;
        } else if(sick == Sickness.TACHICARDIA) {
            mean = 130;
            variance = 5.4;
        } else if(sick == Sickness.FLUTTER) {
            //TODO cambiare scheduling del task
        }
        canGenerateAlarm = false;
    }

    public void reset() {
        canGenerateAlarm = true;
        mean = 80;
        variance = 4.4;
    }

    public Integer getValue() {
        int x =  (int) (mean + random.nextGaussian()*variance);
        if(canGenerateAlarm) {
            Store<StringCommand> store = Sistema.getInstance().getStore();
            if(x < 60) {
                canGenerateAlarm = false;
                store.update(new StringCommand("ALARM_ACTIVATED", new Tuple<>(1, Sickness.BRACHICARDIA)));
            } else if(x > 100) {
                canGenerateAlarm = false;
                store.update(new StringCommand("ALARM_ACTIVATED", new Tuple<>(1, Sickness.TACHICARDIA)));
            }
        }

        return x;
    }
}
