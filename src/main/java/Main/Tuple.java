package Main;

public class Tuple<T,W> {
    private T first;
    private W second;

    public Tuple (T fst, W snd) {
        first=fst;
        second=snd;
    }

    public T fst() {
        return this.first;
    }
    public W snd() {
        return this.second;
    }

    public String toString() {
        return "<" + first + ", " + second + ">";
    }

    public boolean equals(Object other) {
        return (other instanceof Tuple) && this.first.equals(((Tuple) other).first) && this.second.equals(((Tuple) other).second);
    }

    @Override
    public int hashCode() {
        return first.hashCode() + second.hashCode();
    }
}
