package Main;

import java.util.*;

public class MutableList<E> implements Iterable<E> {
    private List<E> list;

    public MutableList() {
        this.list = new ArrayList<>();
    }

    public static MutableList create() {
        return new MutableList<>();
    }

    public void add(E item) {
        this.list.add(item);
    }

    /*public void add(E ...items) {
        Collections.addAll(items, list);

    }
    public void addAll(Collection<? extends E> collection) {
        Collections.addAll()
    }*/

    @Override
    public Iterator<E> iterator() {
        return list.iterator();
    }

    public enum MutableListEvent {
        ADDED, ADDEDMANY, REMOVED, REMOVEDMANY, CHANGED, CHANGEDMANY
    }
}
