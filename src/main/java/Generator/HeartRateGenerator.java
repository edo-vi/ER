package Generator;

import java.util.Random;

public class HeartRateGenerator implements GeneratorInterface {
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
    }

    public void reset() {
        mean = 80;
        variance = 4.4;
    }

    public Integer getValue() {
        return (int) (mean + random.nextGaussian()*variance);
    }
}
