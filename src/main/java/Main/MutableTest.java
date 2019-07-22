package Main;

import Entities.Patient;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class MutableTest {
    public static void main(String ...args) {
        MutableItem<Integer> a = new MutableItem<Integer>(3);
        a.subscribeWithLast(5, (p, l) -> {
            /*System.out.print(p + " & ");
            System.out.println(l);*/
        });

        for (int i = 1; i < 6; i ++ ) {
            a.change(a.get()+1);
        }
        a.subscribeAll(System.out::print);
    }
}
