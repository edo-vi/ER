package Generator;

import State.StringCommand;
import System.Sistema;

import java.util.Random;

public class TemperatureGenerator implements GeneratorInterface {
    private boolean canGenerateAlarm = true;
    private final Random random = new Random();
    private double mean = 36.75;
    private double variance = 0.0625;

    public TemperatureGenerator() {
    }

    public void evolve(Sickness sick) {
        if(sick == Sickness.IPERTERMIA) {
            mean = 38.75;
            variance = 0.0625;
        } else if(sick == Sickness.IPOTERMIA) {
            mean = 35.0;
            variance = 0.0625;
        }
    }

    public void reset() {
        canGenerateAlarm = true;
        mean = 36.75;
        variance = 0.0625;
    }

    public Double getValue() {
        double x =  mean + random.nextGaussian()*variance;
        if((x > 37.5 || x < 36) && canGenerateAlarm) {
            Sistema.getInstance().getStore().update(new StringCommand("ALARM_ACTIVATED", 2));
            canGenerateAlarm = false;
        }

        return x;
    }
}
