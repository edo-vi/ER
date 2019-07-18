package Generator;

import java.util.Random;

public class TemperatureGenerator implements GeneratorInterface {
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
        mean = 36.75;
        variance = 0.0625;
    }

    public Double getValue() {
        return mean + random.nextGaussian()*variance;
    }
}
