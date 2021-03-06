package DataGenerator;

public interface GeneratorInterface {
    void reset();
    void evolve(Sickness sick);
    Object getValue();
}
