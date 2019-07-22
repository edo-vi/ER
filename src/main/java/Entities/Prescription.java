package Entities;

import Main.Tuple;
import javax.persistence.*;
import java.util.*;

@Entity
public class Prescription implements Entry{
    @Id
    @GeneratedValue
    @Column(name = "prescription_id")
    private int id;

    private String drug;

    @Temporal(TemporalType.DATE)
    private Date date;

    private int duration;
    private int dailyDose;
    private int totalNumberofDoses;
    private String doctor;

    @OneToMany(mappedBy = "prescription")
    private List<Administration> administrations = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "recovery_id")
    private Recovery recovery;

    @Transient
    private HashMap<Tuple<String, String>, Integer> dailyAdministrationCounter = new HashMap<>();

    public Prescription(String drug, Date date, int duration, int dailyDose, int totalNumberofDoses, String doctor)
            throws Exception {
        if(duration <= 0 || dailyDose <= 0 || totalNumberofDoses <= 0) throw new IllegalArgumentException("Prescription: invalid arguments");
        this.date = new java.sql.Date(date.getTime());
        this.drug = drug;
        this.duration = duration;
        this.dailyDose = dailyDose;
        this.totalNumberofDoses = totalNumberofDoses;
        this.doctor = doctor;
    }

    public Prescription(String drug, int duration, int dailyDose, int totalNumberofDoses, String doctor,
                        Recovery recovery) throws Exception{
        this(drug, Calendar.getInstance().getTime(), duration, dailyDose, totalNumberofDoses, doctor);
        this.recovery = recovery;
    }

    public Prescription(String drug, Date date, int duration, int dailyDose, int totalNumberofDoses, String doctor,
                        Recovery recovery) throws Exception{
        this(drug, date, duration, dailyDose, totalNumberofDoses, doctor);
        this.recovery = recovery;
    }

    public Prescription() {}

    public String toString() {
        return String.format("{%s, %s, %d, %d, %d, %s, rec_id: %s, adms_is: %s}", drug, date, duration, dailyDose,
                totalNumberofDoses, dailyDose, recovery, administrations);
    }
    /**
     * GETTERS AND SETTERS
     */
    public int getId() {
        return this.id;
    }
    public String getDrug() {
        return drug;
    }

    public void setDrug(String drug) {
        this.drug = drug;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = new java.sql.Date(date.getTime());
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getDailyDose() {
        return dailyDose;
    }

    public void setDailyDose(int dailyDose) {
        this.dailyDose = dailyDose;
    }

    public int getTotalNumberofDoses() {
        return totalNumberofDoses;
    }

    public void setTotalNumberofDoses(int numberOfDoses) {
        this.totalNumberofDoses = numberOfDoses;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public Recovery getRecovery() {
        return recovery;
    }

    public void setRecovery(Recovery recovery) {
        this.recovery = recovery;
    }

    public List<Administration> getAdministrations() {
        return administrations;
    }

    public void setAdministrations(List<Administration> administrations) {
        this.administrations = administrations;
    }

    public void addToAdministrations(Administration administration) {
        this.administrations.add(administration);
    }

    public Integer getAdministrationNumber(Tuple<String, String> drug) {
        return dailyAdministrationCounter.get(drug) + 1;
    }

    public boolean isAdministrable(Tuple<String, String> drug) {
        if(dailyAdministrationCounter.size() == 0 && dailyAdministrationCounter.get(drug) == null) {
            dailyAdministrationCounter.put(drug, 0);
        }
        return dailyAdministrationCounter.get(drug) < totalNumberofDoses && isBefore(drug.fst(), new java.sql.Date(date.getTime() + duration*24*60*60*1000).toString());
    }

    public void addAdministration(Tuple<String, String> drug) {
        dailyAdministrationCounter.replace(drug, dailyAdministrationCounter.get(drug) + 1);
    }

    private boolean isBefore(String date1, String date2) {
        int data1 = new Integer(date1.substring(0, 4));
        int data2 = new Integer(date2.substring(0, 4));
        if(data1 < data2)
            return true;
        else if (data1 > data2)
            return false;
        else {
            data1 = new Integer(date1.substring(5, 7));
            data2 = new Integer(date2.substring(5, 7));
            if(data1 < data2)
                return true;
            else if (data1 > data2)
                return false;
            else {
                data1 = new Integer(date1.substring(8));
                data2 = new Integer(date2.substring(8));
                if(data1 < data2)
                    return true;
                else if (data1 > data2)
                    return false;
                else return false;
            }
        }
    }
}
