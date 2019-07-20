package Entities;

import Generator.*;
import Main.Tuple;
import System.Sistema;
import com.sun.istack.NotNull;
import com.sun.istack.Nullable;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Entity
public class Recovery implements Entry {
    @Id
    @GeneratedValue
    @Column(name = "recovery_id")
    private int id;

    @Temporal(TemporalType.DATE)
    private Date startDate;

    private String diagnosis;
    private boolean active;
    @Temporal(TemporalType.DATE)
    private Date endDate;
    private String dischargeSummary;

    @ManyToOne @NotNull
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @OneToMany(mappedBy = "recovery")
    private List<Monitoring> monitorings = new ArrayList<>();

    @OneToMany(mappedBy = "recovery")
    private List<Prescription> prescriptions = new ArrayList<>();

    @Transient
    private final BPGenerator bpGenerator = new BPGenerator();

    @Transient
    private final HeartRateGenerator heartRateGenerator = new HeartRateGenerator();

    @Transient
    private final TemperatureGenerator temperatureGenerator = new TemperatureGenerator();

    /**
    * GETTERS AND SETTERS
    **/

    public Recovery () {}


    public Recovery(Date start, String diagnosis, Patient patient) {
        this.startDate = new java.sql.Date(start.getTime());
        this.diagnosis = diagnosis;
        this.patient = patient;
        this.active = true;
    }

    public Recovery(Date start, String diagnosis, Patient patient, List<Prescription> prescriptions) {
        this(start, diagnosis, patient);
        this.prescriptions.addAll(prescriptions);
    }

    public Recovery(Date start, String diagnosis, Patient patient, List<Monitoring> monitorings, List<Prescription> prescriptions) {
        this(start, diagnosis, patient);
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
        return active && (this.dischargeSummary == null || this.dischargeSummary.equals("")) && this.endDate == null;
    }

    public void discharge(String dischargeSummary) {
        this.discharge(dischargeSummary, Calendar.getInstance().getTime());
    }

    public void discharge(String dischargeSummary, Date endDate) {
        this.dischargeSummary = dischargeSummary;
        this.setEndDate(endDate);
        this.active = false;
    }
    public void setActive(boolean active) {
        this.active = active;
    }

    public Date getEndDate() throws RecoveryNullFieldException {
        if(endDate == null) {
            throw new RecoveryNullFieldException();
        } else {
            return endDate;
        }
    }

    public void setEndDate(Date endDate) {
        this.endDate = new java.sql.Date(endDate.getTime());
    }

    public String getDischargeSummary() throws RecoveryNullFieldException {
        if(dischargeSummary == null) {
            throw new RecoveryNullFieldException();
        } else {
            return dischargeSummary;
        }
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

    public class RecoveryNullFieldException extends Exception {

    }

}
