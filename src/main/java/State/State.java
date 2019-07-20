package State;
import Entities.*;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class State  {
    private ArrayList<User> systemUsers;
    private User user;
    private List<Patient> patients = new ArrayList<>();
    private int mainRecoveryIndex;
    private Recovery chosenRecovery;
    private User docAlarm;

    // Must be the initial state
    public State() {
        systemUsers = new ArrayList<>();
        systemUsers.add(new User("nan", "pw", Role.NURSE, true));
        systemUsers.add(new User("warian", "pw", Role.DOCTOR, true));
        systemUsers.add(new User("null", "pw", Role.HEAD_PHYSICIAN, true));

        this.user = new User("default");
        this.docAlarm = new User("default");

        /*
        this.patients.add(new Patient("Carlo", "Combi", "CMBCBMWHATEVER291Z", "Verona",
                new GregorianCalendar(1965, Calendar.APRIL, 11).getTime()));
        this.patients.add(new Patient("Barbara", "Oliboni", "BRBLBIDONTKNOW329I", "Verona",
                new GregorianCalendar(1973, Calendar.OCTOBER, 8).getTime()));
        */
    }

    /*
    ** To keep track of the initial state
    */
    public State initial() {
        return new State();
    }

    public String toString() {
        return "Hello I'm State";
    }

    /*
    *****************************************************
    *               Getters and setters                 *
    *****************************************************
    */

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Patient> getPatients() {
        return this.patients;
    }

    public void setPatients(List<Patient> patients) {
        this.patients = patients;
    }

    public void addPatient(Patient patient) {
        this.patients.add(patient);
    }

    public void deletePatient(int index) {
        this.patients.remove(index);
    }

    public void deletePatient(Patient patient) {
        this.patients.remove(patient);
    }

    public int getMainRecoveryIndex() {
        return this.mainRecoveryIndex;
    }

    public void setMainRecoveryIndex(int mainRecoveryIndex) {
        this.mainRecoveryIndex = mainRecoveryIndex;
    }

    public Recovery getMainRecovery() {
        return this.getActiveRecoveries().get(mainRecoveryIndex);
    }

    public List<Recovery> getAllRecoveries() {
        return patients.stream()
                .flatMap(p -> p.getRecoveries().stream())
                .collect(Collectors.toList());
    }

    public List<Recovery> getActiveRecoveries() {
        return patients.stream()
                .flatMap(p -> p.getRecoveries().stream().filter(Recovery::isActive))
                .collect(Collectors.toList());
    }

    public List<Recovery> getNonActiveRecoveries() {
        return patients.stream()
                .flatMap(p -> p.getRecoveries().stream().filter(a->!a.isActive()))
                .collect(Collectors.toList());
    }

    public List<Monitoring> getActiveMonitorings() {
        return this.getActiveRecoveries().stream()
                .flatMap(r -> r.getMonitorings().stream())
                .collect(Collectors.toList());
    }

    public List<Monitoring> getAllMonitorings() {
        return this.getAllRecoveries().stream()
                .flatMap(r -> r.getMonitorings().stream())
                .collect(Collectors.toList());
    }

    public User getDocAlarm() {
        return docAlarm;
    }

    public void setDocAlarm(User docAlarm) {
        this.docAlarm = docAlarm;
    }

    public Recovery getChosenRecovery() {
        return chosenRecovery;
    }

    public void setChosenRecovery(Recovery chosenRecovery) {
        this.chosenRecovery = chosenRecovery;
    }

    public ArrayList<User> getSystemUsers() {
        return systemUsers;
    }

    public void setSystemUsers(ArrayList<User> systemUsers) {
        this.systemUsers = systemUsers;
    }

    public User getDocAlarmCheck() {
        return systemUsers.get(Role.DOCTOR.ordinal());
    }

    @Override
    public boolean equals(Object oth) {
        return (oth instanceof State) && ((State) oth).getPatients().equals(this.patients)
                && ((State) oth).getUser().equals(this.user)
                && ((State) oth).getMainRecoveryIndex() == this.mainRecoveryIndex
                && ((State) oth).getSystemUsers().equals(this.getSystemUsers());
    }
}
