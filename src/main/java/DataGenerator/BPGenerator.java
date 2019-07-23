package DataGenerator;

import Main.Tuple;
import State.Store;
import State.StringCommand;
import System.Sistema;

import java.util.Random;

public class BPGenerator implements GeneratorInterface {
    private boolean canGenerateAlarm = true;
    private Sistema sys = null;
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
        canGenerateAlarm = true;
        meanS = 120.0;
        varianceS = 9.5;
        meanD = 77.5;
        varianceD = 4.0;
    }

    public Tuple<Integer, Integer> getValue() {
        if(sys == null)
            sys = Sistema.getInstance();

        Tuple<Integer, Integer> data = new Tuple<>((int) (meanD + randomD.nextGaussian()*varianceD), (int) (meanS + randomS.nextGaussian()*varianceS));
        if(canGenerateAlarm && sys.getSickPatient() != null) {
            Store<StringCommand> store = sys.getStore();
            if(data.fst() < 60 || data.snd() < 90) {
                canGenerateAlarm = false;
                store.update(new StringCommand("ALARM_ACTIVATED", new Tuple<>(2, Sickness.IPOTENSIONE)));
            } else if(data.fst() > 95 || data.snd() > 150) {
                canGenerateAlarm = false;
                store.update(new StringCommand("ALARM_ACTIVATED", new Tuple<>(2, Sickness.IPERTENSIONE)));
            }
        }

        return data;
    }
}
