package System;

import Component.*;
import Entities.Patient;
import Entities.Recovery;
import Entities.User;
import Generator.DataThread;
import Generator.Sickness;
import Generator.Value;
import InterfaceController.DOCControllerFactory.DOCControllerFactory;
import InterfaceController.DOCControllerFactory.DOCDController;
import InterfaceController.HPControllerFactory.HPControllerFactory;
import InterfaceController.NURControllerFactory.NURControllerFactory;
import State.Reducer;
import State.ReducerString;
import State.StringCommand;
import State.State;
import State.Store;
import State.DatabaseService;
import State.MiddlewareString;
import State.Middleware;

import System.DOCInterfaceFactory.DOCFactory;
import System.HPInterfaceFactory.HPFactory;
import System.NURInterfaceFactory.NURFactory;
import System.Session.DoctorSessionThread;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import Main.Tuple;
import javafx.stage.StageStyle;

import java.util.*;
import java.util.stream.Collectors;

public class Sistema {
    private static Sistema s;
    private Store<StringCommand> store;
    private InterfacesController controller;
    private Stage monitoringStage = null;
    private Stage alarmStage = null;
    private Stage alarmControlStage = null;
    private final Random random = new Random();
    private int selectedPatient = -1;
    private boolean alarmCtlIsShown = false;

    public static Sistema getInstance() {
        if (s == null)
            s = new Sistema();
        return s;
    }

    //will have all the information into the state and iterate over them
    //NB. This is just demo environment
    private Sistema() {
        Reducer<StringCommand> reducer = new ReducerString()
                .with("LOGIN")
                .with("LOGOUT", (c, s) -> {
                    s.setUser(new User("default"));
                    return s;
                })
                .with("GENERATE_BP", (c, s) -> {
                    s.getActiveRecoveries().forEach(r -> r.generateMonitoring(Value.BP));
                    return s;
                })
                .with("GENERATE_HEART_RATE", (c, s) -> {
                    s.getActiveRecoveries().forEach(r -> r.generateMonitoring(Value.HEART_RATE));
                    return s;
                })
                .with("GENERATE_TEMPERATURE", (c, s) -> {
                    s.getActiveRecoveries().forEach(r -> r.generateMonitoring(Value.TEMPERATURE));
                    return s;
                })
                .with("LOAD")
                .with("ADD_PATIENT")
                .with("SHOW_MONITORING")
                .with("CLOSE_MONITORING")
                .with("START_MONITORING")
                .with("STOP_MONITORING")
                .with("SHOW_ALARMS")
                .with("GET_LOGIN", (c, s) -> {
                    if(s.getUser().isValid())
                        controller.toFront();
                    else controller.activate("login", LoginComponent.loginTitle);
                    return s;
                })
                .with("EVOLVE_GENERATOR", (c, s) -> {
                    Sickness sick = (Sickness) c.getArg();
                    if (selectedPatient == -1) {
                        selectedPatient = random.nextInt(s.getActiveRecoveries().size());
                    }
                    s.getActiveRecoveries().get(selectedPatient).evolveGenerator(sick);
                    return s;
                })
                .with("CHOSEN_RECOVERY_TO_SHOW", (c,s) -> {
                    Recovery r = (Recovery) c.getArg();
                    if (r != null) {
                        s.setChosenRecovery(r);
                    }
                    return s;
                })
                .with("CLEAR_CHOSEN_RECOVERY", (c,s) -> {
                    s.setChosenRecovery(null);
                    return s;
                })
                .with("ALARM_ACTIVATED")
                .with("RESET_ALARMS")
                .with("ALARM_LOGIN")
                .with("DISCHARGE_PATIENT")
                .with("ALARM_LOGIN")
                .with("SESSION_TERMINATED")
                .with("SESSION_START")
                .with("SEARCH_PATIENT_HP");
        Middleware<StringCommand> middleware = new MiddlewareString(monitoringStage)
                .with("LOGIN", (c, s, m) -> {
                    User x = (User) c.getArg();
                    for(User u : s.getSystemUsers()) {
                        if (u.equals(x)) {
                            s.setUser(u);
                            return new Tuple<>(new StringCommand("LOGIN_SUCCESS"), s);
                        }
                    }
                    return new Tuple<>(new StringCommand("LOGIN_FAILURE"), s);
                })
                .with("LOAD", (c, s, m) -> {
                    List<Patient> ps = DatabaseService.getEntries("Patient").stream()
                            .map(e -> (Patient) e)
                            .collect(Collectors.toList());
                    s.setPatients(ps);
                    List<Recovery> rec = ps.stream().flatMap(p -> p.getAllRecoveries().stream()).filter(Recovery::isActive)
                            .collect(Collectors.toList());
                    //TODO add
                    /*if (rec.size() == 0) {

                    }*/
                    s.setMainRecoveryIndex(0);
                    return new Tuple<>(new StringCommand("LOADED"), s);
                })
                .with("ADD_PATIENT" , (c, s, m) -> {
                    Patient patient = (Patient) c.getArg();
                    s.addPatient(patient);
                    DatabaseService.addEntry(patient);
                    return new Tuple<>(new StringCommand("ADDED_PATIENT"), s);
                    })
                .with("SHOW_MONITORING", (c,s,m) -> {
                    if(monitoringStage == null) {
                        monitoringStage = createUI("MON", MonitoringComponent.monitoringTitle);
                        monitoringStage.setOnCloseRequest(e -> store.update(new StringCommand("STOP_MONITORING")));
                    }
                    monitoringStage.sizeToScene();
                    monitoringStage.show();
                    monitoringStage.toFront();
                    return new Tuple<>((new StringCommand("SHOW_MONITORING")), s);
                })
                .with("START_MONITORING", (c,s,m) -> {
                    MiddlewareString x = ((MiddlewareString) m);
                    if(x.getMonitoring() != null) {
                        x.getMonitoring().restart();
                    } else {
                        DataThread t = new DataThread(store);
                        x.setMonitoring(t);
                        t.start();
                    }
                    return new Tuple<>(new StringCommand("MONITORING_HAS_STARTED"), s);
                })
                .with("STOP_MONITORING", (c,s,m) -> {
                    ((MiddlewareString) m).getMonitoring().interrupt();
                    return new Tuple<>(new StringCommand("STOPPED_MONITORING"), s);
                })
                .with("CLOSE_MONITORING", (c,s,m) -> {
                    monitoringStage.close();
                    return new Tuple<>(new StringCommand("CLOSE_MONITORING"), s);
                })
                .with("SHOW_ALARMS", (c,s,m) -> {
                    if(alarmStage == null)
                        alarmStage = createUI("ALM", AlarmsComponent.AlarmsTitle);
                    alarmStage.sizeToScene();
                    alarmStage.show();
                    alarmStage.toFront();
                    return new Tuple<>(new StringCommand("SHOW_ALARMS"), s);
                })
                .with("ALARM_ACTIVATED", (c,s,m) -> {
                    if(!alarmCtlIsShown) {
                        boolean docAlreadyLog = s.getDocAlarm().isValid() && s.getDocAlarm().equals(s.getDocAlarmCheck());
                        String filename = (docAlreadyLog) ? "ALMCTL" : "ALMCTLLOG";
                        if (alarmControlStage == null) {
                            alarmControlStage = createUI(filename, AlarmControlComponent.AlarmControlTitle);
                            alarmControlStage.initModality(Modality.APPLICATION_MODAL);
                            alarmControlStage.initStyle(StageStyle.UNDECORATED);
                        }
                        String cssName = "";
                        switch (((Tuple<Integer, Sickness>) c.getArg()).fst()) {
                            case 1:
                                cssName = "/ButtonAlarm1.css";
                                break;
                            case 2:
                                cssName = "/ButtonAlarm2.css";
                                break;
                            case 3:
                                cssName = "/ButtonAlarm3.css";
                                break;
                        }
                        alarmControlStage.getScene().getStylesheets().add(getClass().getResource(cssName).toExternalForm());
                        alarmControlStage.getScene().setRoot(getInterface(filename));
                        alarmControlStage.sizeToScene();
                        alarmControlStage.toFront();
                        alarmControlStage.show();
                        alarmCtlIsShown = true;
                    }
                    return new Tuple<>(new StringCommand("ACTIVE_ALARM", c.getArg()), s);
                })
                .with("RESET_ALARMS", (c,s,m) -> {
                    if(selectedPatient != -1) {
                        s.getActiveRecoveries().get(selectedPatient).resetGenerator();
                        selectedPatient = -1;
                    }
                    alarmControlStage.close();
                    alarmCtlIsShown = false;
                    return new Tuple<>(new StringCommand("CLOSED_ALARMS"), s);
                })
                .with("ALARM_LOGIN", (c,s,m) -> {
                    User u = (User) c.getArg();
                    if (s.getDocAlarmCheck().equals(u)) {
                        s.setDocAlarm(u);
                        s.getDocAlarm().setValid(true);
                        return new Tuple<>(new StringCommand("ALM_LOGIN_SUCCESS"), s);
                    }
                    else {
                        return new Tuple<>(new StringCommand("ALM_LOGIN_FAILURE"), s);
                    }
                })
                .with("DISCHARGE_PATIENT", (c,s,m) -> {
                    Tuple<Integer, String> val = (Tuple<Integer, String>) c.getArg();
                    try {
                        Recovery re = s.getAllRecoveries().stream()
                                .filter(r -> r.getId()==val.fst()).findFirst().orElse(null);
                        re.discharge(val.snd());
                        DatabaseService.addEntry(re);
                        return new Tuple<>(new StringCommand("DISCHARGED_A_PATIENT"), s);
                    } catch (Exception e) {
                        System.out.println("Sistema, Discharge summary: " + e);
                        return new Tuple<>(new StringCommand("COULD NOT_DISCHARGE_A_PATIENT"), s);
                    }
                })
                .with("SESSION_TERMINATED", (c,s,m) -> {
                    ((MiddlewareString) m).getDocSession().interrupt();
                    s.setDocAlarm(new User("default"));

                    return new Tuple<>(new StringCommand("DOC_SESSION_TERMINATED"), s);
                })
                .with("SESSION_START", (c,s,m) -> {
                    MiddlewareString x = ((MiddlewareString) m);
                    if(x.getDocSession() != null) {
                        x.getDocSession().restart();
                    } else {
                        DoctorSessionThread t = new DoctorSessionThread(store);
                        x.setDocSession(t);
                        t.start();
                    }

                    return new Tuple<>(new StringCommand("DOC_SESSION_STARTED"), s);
                });

        store = new Store<StringCommand>(new State(), reducer, middleware);
        store.update(new StringCommand("LOAD"));
        /*
        store.update(new StringCommand("START_MONITORING"));
        store.update(new StringCommand("ADD_PATIENT", new Patient("Roberto", "Posenato", "PSNRBRA373UUS88I",
                "Verona", new GregorianCalendar(1981, Calendar.FEBRUARY, 11).getTime())));
        */
    }

    public void setupUI(Stage stage){
        try {
            stage.getIcons().add(new Image("/logo.png"));
            System.out.println("---- System Interfaces ----");
            this.controller = new InterfacesController(stage);
            this.controller.addInterface("login", new LoginComponent<StringCommand>().getLoader().load());
            this.controller.addInterface("MON", new MonitoringComponent<StringCommand>().getLoader().load());
            this.controller.addInterface("ALM", new AlarmsComponent<StringCommand>().getLoader().load());
            this.controller.addInterface("ALMCTLLOG", new AlarmControlComponent<StringCommand>(false).getLoader().load());
            this.controller.addInterface("ALMCTL", new AlarmControlComponent<StringCommand>(true).getLoader().load());
            System.out.println("---- HP Interfaces ----");
            this.controller.addInterface("HPD", new HPComponent<StringCommand>(new HPFactory().getHPInterface("default"), new HPControllerFactory().getController("default")).getLoader().load());
            this.controller.addInterface("HPS", new HPComponent<StringCommand>(new HPFactory().getHPInterface("search"), new HPControllerFactory().getController("search")).getLoader().load());
            this.controller.addInterface("HPSR", new HPComponent<StringCommand>(new HPFactory().getHPInterface("searchResult"), new HPControllerFactory().getController("searchResult")).getLoader().load());
            this.controller.addInterface("HPM", new HPComponent<StringCommand>(new HPFactory().getHPInterface("monitoring"), new HPControllerFactory().getController("monitoring")).getLoader().load());
            System.out.println("---- DOC Interfaces ----");
            this.controller.addInterface("DOCD", new DOCComponent<StringCommand>(new DOCFactory().getInterface("default"), new DOCControllerFactory().getController("default")).getLoader().load());
            this.controller.addInterface("DOCAP", new DOCComponent<StringCommand>(new DOCFactory().getInterface("addPrescription"), new DOCControllerFactory().getController("addPrescription")).getLoader().load());
            this.controller.addInterface("DOCM", new DOCComponent<StringCommand>(new DOCFactory().getInterface("monitoring"), new DOCControllerFactory().getController("monitoring")).getLoader().load());
            this.controller.addInterface("DOCS", new DOCComponent<StringCommand>(new DOCFactory().getInterface("search"), new DOCControllerFactory().getController("search")).getLoader().load());
            this.controller.addInterface("DOCSR", new DOCComponent<StringCommand>(new DOCFactory().getInterface("searchResult"), new DOCControllerFactory().getController("searchResult")).getLoader().load());
            System.out.println("---- NUR Interfaces ----");
            this.controller.addInterface("NURD", new NURComponent<StringCommand>(new NURFactory().getInterface("default"), new NURControllerFactory().getController("default")).getLoader().load());
            this.controller.addInterface("NURAP", new NURComponent<StringCommand>(new NURFactory().getInterface("addPatient"), new NURControllerFactory().getController("addPatient")).getLoader().load());
            this.controller.addInterface("NURM", new NURComponent<StringCommand>(new NURFactory().getInterface("monitoring"), new NURControllerFactory().getController("monitoring")).getLoader().load());
            this.controller.addInterface("NURS", new NURComponent<StringCommand>(new NURFactory().getInterface("search"), new NURControllerFactory().getController("search")).getLoader().load());
            this.controller.addInterface("NURSR", new NURComponent<StringCommand>(new NURFactory().getInterface("searchResult"), new NURControllerFactory().getController("searchResult")).getLoader().load());
            System.out.println("---- Interfaces Loaded Successfully----");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error during interfaces setup");
        }
    }

    public Store getStore() {
        return store;
    }

    public void setInterface(String component, String title) {
        controller.activate(component, title);
    }

    public void endSystem() {
        this.controller.deactivate();
    }

    public Pane getInterface(String s) {
        return this.controller.getInterface(s);
    }

    public Stage getMonitoringStage() {
        return monitoringStage;
    }

    private Stage createUI(String filename, String title) {
        Stage stage = new Stage();
        stage.getIcons().add(new Image("/logo.png"));
        stage.setScene(new Scene(getInterface(filename)));
        stage.setTitle(title);
        stage.setResizable(false);

        return stage;
    }

    public Patient getSickPatient() {
        if(selectedPatient == -1)
            return null;
        else return store.getState().getActiveRecoveries().get(selectedPatient).getPatient();
    }
}
