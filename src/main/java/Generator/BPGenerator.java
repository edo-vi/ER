package Generator;

import Main.Tuple;

import java.util.Random;

public class BPGenerator implements GeneratorInterface {
    private final Random randomS = new Random();
    private double meanS = 120.0;
    private double varianceS = 9.25;

    private final Random randomD = new Random();
    private double meanD = 77.5;
    private double varianceD = 4.0;

    public BPGenerator() {
    }

    public void evolve(Sickness sick) {
        if(sick == Sickness.IPERTENSIONE) {
            meanS = 185.5;
            varianceS = 5.9;
            meanD = 107.5;
            varianceD = 3.5;
        }else if(sick == Sickness.IPOTENSIONE) {
            meanS = 75;
            varianceS = 3.8;
            meanD = 50;
            varianceD = 3;
        }
    }

    public void reset() {
        meanS = 120;
        varianceS = 9.5;
        meanD = 77.5;
        varianceD = 4.0;
    }

    public Tuple<Integer, Integer> getValue() {
        return new Tuple<>((int) (meanD + randomD.nextGaussian()*varianceD), (int) (meanS + randomS.nextGaussian()*varianceS));
    }
}
