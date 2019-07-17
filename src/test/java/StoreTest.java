package test.java;

import Entities.Role;
import Main.Tuple;
import Entities.Patient;
import Entities.User;
import State.Store;
import State.StringCommand;
import State.State;
import State.Reducer;
import State.Middleware;
import State.MiddlewareString;
import State.DatabaseService;
import System.Sistema;

import State.ReducerString;
import io.reactivex.disposables.Disposable;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class StoreTest {
    private State state = new State();


    private Reducer<StringCommand> reducer = new ReducerString()
                .with("LOGIN")
                .with("LOGOUT", (c, s) -> {
                    s.setUser(new User());
                    return s;
                })
                .with("LOAD")
                .with("ADD_PATIENT");
    private Middleware<StringCommand> middleware = new MiddlewareString(Sistema.monitoringStage)
                .with("LOGIN", (c, s, m) -> {
                    User u = (User) c.getArg();
                    if (s.getUserCheck().equals(u)) {
                        s.setUser(s.getUserCheck());
                        return new Tuple<>(new StringCommand("LOGIN_SUCCESS", UUID.randomUUID()), s);
                    }
                    else {
                        return new Tuple<>(new StringCommand("LOGIN_FAILURE", UUID.randomUUID()), s);
                    }
                })
                .with("LOAD", (c, s, m) -> {
                    List<Patient> ps = DatabaseService.getEntries("Patient").stream()
                            .map(e -> (Patient) e)
                            .collect(Collectors.toList());
                    s.setPatients(ps);
                    return new Tuple<>(new StringCommand("LOADED", UUID.randomUUID()), s);
                }).with("ADD_PATIENT" , (c, s, m) -> {
                    Patient patient = (Patient) c.getArg();
                    s.addPatient(patient);
                    //DatabaseService.addEntry(patient);
                    return new Tuple<>(new StringCommand("ADDED_PATIENT", UUID.randomUUID()), s);
                });

        private Store<StringCommand> store = new Store<StringCommand>(new State(), reducer, middleware);


    @Test
    public void update() {
        Patient p =  new Patient("Roberto", "Posenato", "PSNRBR71G208281JA",
                "Verona", new GregorianCalendar(1971, Calendar.JULY, 20).getTime());
        assertArrayEquals(store.poll().getPatients().toArray(), new Patient[] {});

        store.update(new StringCommand("ADD_PATIENT", UUID.randomUUID(), p));
        assertArrayEquals(store.poll().getPatients().toArray(), new Patient[] {p});


        Disposable dis = store.getCommandStream().subscribe(s -> {
            assertEquals(s, "LOGIN_SUCCESS");
        });
        store.update(new StringCommand("LOGIN", UUID.randomUUID(),
                new User("eme", "pw", Role.HEAD_PHYSICIAN, false)));
        assertEquals(store.poll().getUser(), store.poll().getUserCheck());
        dis.dispose();


        dis = store.getCommandStream().subscribe(s -> {
            assertEquals(s, "LOGIN_FAILURE");
        });
        store.update(new StringCommand("LOGIN", UUID.randomUUID(),
                new User("", "", Role.HEAD_PHYSICIAN, true)));
        dis.dispose();


        State st = store.poll();
        dis = store.getCommandStream().subscribe(s -> {
            assertEquals(s, "NON_EXISTENT_COMMAND");
        });
        store.update(new StringCommand("v", UUID.randomUUID()));
        assertEquals(st, store.poll());
        dis.dispose();
    }


}