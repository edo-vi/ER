package Entities;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Administration implements Entry {

    @Id
    @GeneratedValue
    @Column(name = "administration_id")
    private int id;

    @Temporal(TemporalType.DATE)
    private Date date;
    private int hour;
    private int dose;
    private String notes;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "prescription_id")
    private Prescription prescription;

    public Administration() { }

    public Administration(Date date, int hour, int dose, String notes) {
        if (hour < 0 || hour > 23 || dose <= 0) throw new IllegalArgumentException("Administration: invalid arguments");
        this.date = new java.sql.Date(date.getTime());
        this.hour = hour;
        this.dose = dose;
        this.notes = notes;
    }

    public Administration(Date date, int hour, int dose, String notes, Patient patient, Prescription prescription) {
        this(date, hour, dose, notes);
        this.patient = patient;
        this.prescription = prescription;
    }

    public Administration(Date date, int hour, int dose, String notes, Patient patient) {
        this(date, hour, dose, notes);
        this.patient = patient;
    }

    public Administration(Date date, int hour, int dose, String notes, Prescription prescription) {
        this(date, hour, dose, notes);
        this.prescription = prescription;
    }

    public String toString() {
        String s = String.format("{%s, %d, %d, %s, ", date, hour, dose, notes);
        try {
            String patientId = String.valueOf(patient.getId());
            s += String.valueOf(patientId);
        } catch (NullPointerException e) {
            s += "null";
        }
        try {
            String prescriptionId = String.valueOf(prescription.getId());
            s+= ", " + String.valueOf(prescriptionId) + "}";
        } catch (NullPointerException e) {
            s+= ", null}";
        }
        return s;

    }

    /**
     * GETTERS AND SETTERS
     */

    public int getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = new java.sql.Date(date.getTime());
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getDose() {
        return dose;
    }

    public void setDose(int dose) {
        this.dose = dose;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Prescription getPrescription() {
        return prescription;
    }

    public void setPrescription(Prescription prescription) {
        this.prescription = prescription;
    }
}
