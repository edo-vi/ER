package Main;

import Entities.Patient;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class MutableTest {
    public static void main(String ...args) {
        MutableItem<Integer> a = new MutableItem<Integer>(3);
        a.subscribeWithLast(10, (p, l) -> {});

        for (int i = 1; i < 18; i ++ ) {
            a.change(a.last().get()+1);
            System.out.println(a.last(3));
        }

    }
}
