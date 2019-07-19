package Main;

public class Triple<T,W,S> {
    private T first;
    private W second;
    private S third;
    public Triple (T fst, W snd ,S trd) {
        first=fst;
        second=snd;
        third=trd;
    }

    public T fst() {
        return this.first;
    }
    public W snd() {
        return this.second;
    }
    public S trd() {
        return this.third;
    }
    public String toString() {
        return "<" + first + ", " + second + ", " + third + ">";
    }
}
