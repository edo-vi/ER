package Main;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class MutableItem<T> {
    private T item;
    private List<T> history = new ArrayList<>();

    private Subject<T> life$ = PublishSubject.create();
    private int bufferSize = 10;
    private int cursor = -1;

    public MutableItem(T item) {
        this.item = item;
    }

    public MutableItem buffer(int s) {
        if (s < 0 || s > 20) bufferSize = 10;
        else bufferSize = s;

        return this;
    }

    public Disposable subscribe(Consumer<T> function) {
        return this.life$.subscribe(function::accept);
    }

    public Disposable subscribeWithLast(BiConsumer<T, Optional<T>> function) {
        try {
            return this.life$.subscribe(t -> function.accept(t, Optional.of(history.get(0))));
        } catch (ArrayIndexOutOfBoundsException err) {
            return this.life$.subscribe(t -> function.accept(t, Optional.empty()));
        }

    }

    public T get() {
        return item;
    }
    public Disposable subscribeWithLast(int size, BiConsumer<T, List<Optional<T>>> function) {

        return this.life$.subscribe(t -> {
            List<Optional<T>> arr = new ArrayList<>();
            for (int i = 0; i < size && i < bufferSize; i++) {
                try {
                    arr.add(Optional.of(history.get(i)));
                } catch (IndexOutOfBoundsException err) {
                    arr.add(Optional.empty());
                }
            }
            function.accept(t, arr);
        });
    }

    public Disposable subscribeAll(Consumer<T> function) {
        List<T> arr = history;
        arr.add(0, item);
        return ((Observable<T>) Observable.fromArray(arr.toArray())).subscribe(function::accept);
    }

    public Observable<T> getAll() {
        List<T> arr = history;
        arr.add(0, item);
        return ((Observable<T>) Observable.fromArray(arr.toArray()));
    }

    public static MutableItem of(Object item) {
        return new MutableItem<>(item);
    }

    public void change(T item) {
        this.history.add(0, this.item);
        this.item = item;

        if (cursor++ >= bufferSize) {
            history.remove(cursor--);
        }
        this.life$.onNext(item);
    }

    public Optional<T> last() {
        if (history.size() > 0) return Optional.of(history.get(0));
        else return Optional.empty();
    }

    /**
     *
     * @param size number of elements to get. Must be greater than 1.
     * @return
     */
    public List<Optional<T>> last(int size) {
        List<Optional<T>> arr = new ArrayList<>();
        if (size > 0) {
            for (int i = 0; i < size && i < bufferSize; i++) {
                try {
                    arr.add(Optional.of(history.get(i)));
                } catch (IndexOutOfBoundsException err) {
                    arr.add(Optional.empty());
                }
            }
            return arr;
        }
        else {
            arr.add(Optional.empty());
        }
        return arr;
    }

}
