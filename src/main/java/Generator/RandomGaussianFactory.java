package Generator;

/**
 Generate pseudo-random floating point values, with an
 approximately Gaussian (normal) distribution.
 */
public class RandomGaussianFactory {
    public GeneratorInterface getDataGenerator(Value value) {
        switch(value) {
            case DBP: return new DBPGenerator();
            case SBP: return new SBPGenerator();
            case HEART_RATE: return new HeartRateGenerator();
            case TEMPERATURE: return new TemperatureGenerator();
            default: return null;
        }
    }
}