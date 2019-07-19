package Entities;

import Generator.*;
import Main.Tuple;
import System.Sistema;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Recovery implements Entry{
    @Id
    @GeneratedValue
    @Column(name = "recovery_id")
    private int id;

    @Temporal(TemporalType.DATE)
    private Date startDate;

    private String diagnosis;
    private boolean active;
    private Date endDate;
    private String dischargeSummary;

    @Transient
    private final BPGenerator bpGenerator = new BPGenerator();

    @Transient
    private final HeartRateGenerator heartRateGenerator = new HeartRateGenerator();

    @Transient
    private final TemperatureGenerator temperatureGenerator = new TemperatureGenerator();

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @OneToMany(mappedBy = "recovery")
    private List<Monitoring> monitorings = new ArrayList<>();

    @OneToMany(mappedBy = "recovery")
    private List<Prescription> prescriptions = new ArrayList<>();


    /**
    * GETTERS AND SETTERS
    **/

    public Recovery () {}
    public Recovery(Date start, Date end, String diagnosis, boolean active, String dischargeSummary) {
        this.startDate = new java.sql.Date(start.getTime());
        this.endDate = new java.sql.Date(end.getTime());
        this.diagnosis = diagnosis;
        this.active = active;
        this.dischargeSummary = dischargeSummary;
    }

    public Recovery(Date start, Date end, String diagnosis, boolean active, String dischargeSummary, Patient patient) {
        this(start, end, diagnosis, active, dischargeSummary);
        this.patient = patient;
    }

    public Recovery(Date start, Date end, String diagnosis, boolean active, String dischargeSummary, Patient patient,
                    List<Monitoring> monitorings, List<Prescription> prescriptions) {
        this(start, end, diagnosis, active, dischargeSummary, patient);
        this.monitorings.addAll(monitorings);
        this.prescriptions.addAll(prescriptions);
    }


    public Recovery(Date start, Date end, String diagnosis, boolean active, String dischargeSummary,
                    List<Monitoring> monitorings, List<Prescription> prescriptions) {
        this(start, end, diagnosis, active, dischargeSummary);
        this.monitorings.addAll(monitorings);
        this.prescriptions.addAll(prescriptions);
    }

    public Monitoring getLastMonitoring() {
        if (this.monitorings.size() == 0) {
            System.out.println("*** Recovery: trying to get the last monitoring when the array is empty ***");
            return new Monitoring().defaultMonitoring();
        }
        return this.monitorings.get(this.monitorings.size()-1);
    }

    public void evolveGenerator(Sickness s) {
        bpGenerator.evolve(s);
        heartRateGenerator.evolve(s);
        temperatureGenerator.evolve(s);
    }

    public void resetGenerator() {
        bpGenerator.reset();
        heartRateGenerator.reset();
        temperatureGenerator.reset();
    }

    public void generateMonitoring(Value value) {
        if (active) {
            Monitoring last = this.getLastMonitoring();
            Monitoring nm = new Monitoring();
            nm.setDate(new Date());
            if (value == Value.BP) {
                Tuple<Integer, Integer> ints = this.bpGenerator.getValue();
                nm.setDiastolicPressure(ints.fst());
                nm.setSystolicPressure(ints.snd());
                nm.setHeartRate(last.getHeartRate());
                nm.setTemperature(last.getTemperature());
            }
            else if (value == Value.HEART_RATE) {
                nm.setHeartRate(this.heartRateGenerator.getValue());
                nm.setDiastolicPressure(last.getDiastolicPressure());
                nm.setSystolicPressure(last.getSystolicPressure());
                nm.setTemperature(last.getTemperature());
            }
            else {
                nm.setTemperature(this.temperatureGenerator.getValue());
                nm.setDiastolicPressure(last.getDiastolicPressure());
                nm.setSystolicPressure(last.getSystolicPressure());
                nm.setHeartRate(last.getHeartRate());
            }
        this.addToMonitorings(nm);
        }
    }

    /**
     * GETTERS AND SETTERS
     */

    public int getId() {
        return this.id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date date) {
        this.startDate = new java.sql.Date(date.getTime());
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = new java.sql.Date(endDate.getTime());
    }

    public String getDischargeSummary() {
        return dischargeSummary;
    }

    public void setDischargeSummary(String dischargeSummary) {
        this.dischargeSummary = dischargeSummary;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public List<Prescription> getPrescriptions() {
        return prescriptions;
    }

    public void setPrescriptions(List<Prescription> prescriptions) {
        this.prescriptions = prescriptions;
    }

    public void addToPrescriptions(Prescription prescription) {this.prescriptions.add(prescription);}

    public List<Monitoring> getMonitorings() {
        return monitorings;
    }

    public void setMonitorings(List<Monitoring> monitorings) {
        this.monitorings = monitorings;
    }

    public void addToMonitorings(Monitoring monitoring) {this.monitorings.add(monitoring);}
}
