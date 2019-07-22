package Entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class Patient implements Entry {
    @Id
    @GeneratedValue
    @Column(name = "patient_id")
    private int id;
    private String name;
    private String surname;
    private String fiscalCode;
    private String placeOfBirth;

    @Temporal(TemporalType.DATE)
    private Date birthDay;

    @OneToMany(mappedBy = "patient")
    private List<Administration> administrations = new ArrayList<>();

    @OneToMany(mappedBy = "patient")
    private List<Recovery> recoveries = new ArrayList<>();


    private PatientState patientState;

    public Patient() { }

    public Patient(String name, String surname, String fiscalCode, String placeOfBirth, Date birthDay) {
        this.name = name;
        this.surname = surname;
        this.fiscalCode = fiscalCode;
        this.placeOfBirth = placeOfBirth;
        this.birthDay = new java.sql.Date(birthDay.getTime());
        this.patientState = PatientState.WAITING;
    }

    /*public Patient(String name, String surname, String fiscalCode, String placeOfBirth, Date birthDay,
                   List<Administration> administrations, List<Recovery> recoveries) {
        this(name, surname, fiscalCode, placeOfBirth, birthDay);
        this.administrations.addAll(administrations);
        this.recoveries.addAll(recoveries);
    }*/

    public void admit(Recovery recovery) throws MoreThanOneActiveRecoveryException {
        if (getActiveRecoveries().size() > 0) throw new MoreThanOneActiveRecoveryException();
        this.patientState = PatientState.RECOVERED;

        this.addToRecoveries(recovery);
        try {
            recovery.setPatient(this);
        } catch (Recovery.PatientNotAdmittedException err) {
            System.out.print("Should never happen!!! :" + err);
        }
    }

    public void discharge(Recovery recovery, String dischargeSummary) {
        this.patientState = PatientState.DISCHARGED;
        recovery.discharge(dischargeSummary);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(String.format("{%s, %s, %s, %s, %s, ", name, surname, fiscalCode, placeOfBirth, birthDay));
        if (this.administrations != null && !this.administrations.isEmpty() ) {
            for (Administration adm : this.administrations) {
                sb.append(String.valueOf(adm.getId()));
                sb.append(", ");
            }
        } else {
            sb.append("null, ");
        }
        if (this.recoveries != null && !this.recoveries.isEmpty()) {
            for (Recovery rec : this.recoveries) {
                sb.append(String.valueOf(rec.getId()));
                sb.append(", ");
            }
        } else {
            sb.append("null, ");
        }
        int lg = sb.toString().length()-2;
        return sb.substring(0,lg);
    }



    /**
     * GETTERS AND SETTERS
     */

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getFiscalCode() {
        return fiscalCode;
    }

    public void setFiscalCode(String fiscalCode) {
        this.fiscalCode = fiscalCode;
    }

    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }

    public Date getDateofBirth() {
        return birthDay;
    }

    public void setDateofBirth(Date birthDay) {
        this.birthDay = new java.sql.Date(birthDay.getTime());
    }

    public List<Administration> getAdministrations() {
        return administrations;
    }

    public void setAdministrations(List<Administration> administrations) {
        this.administrations = administrations;
    }

    public void addToAddministrations(Administration administration) {
        this.administrations.add(administration);
    }

    public List<Recovery> getAllRecoveries() {
        return recoveries;
    }

    public void setRecoveries(List<Recovery> recoveries) {
        this.recoveries = recoveries;
    }

    private void addToRecoveries(Recovery recovery) {
        this.recoveries.add(recovery);
    }

    public boolean isRecovered() {
        return this.patientState.equals(PatientState.RECOVERED);
    }

    public boolean isWaiting() {
        return this.patientState.equals(PatientState.WAITING);
    }
    public boolean isDischarged() {
        return this.patientState.equals(PatientState.DISCHARGED);
    }

    public List<Recovery> getActiveRecoveries() {
        return this.getAllRecoveries().stream().filter(Recovery::isActive).collect(Collectors.toList());
    }

    public List<Recovery> getDischargedRecovery() {
        return this.getAllRecoveries().stream().filter(Recovery::isDischarged).collect(Collectors.toList());
    }

    public List<Monitoring> getActiveMonitorings() {
        return this.getActiveRecoveries().stream().flatMap(re -> re.getMonitorings().stream())
                .collect(Collectors.toList());
    }

    public List<Monitoring> getAllMonitorings() {
        return this.getAllRecoveries().stream().flatMap(re -> re.getMonitorings().stream())
                .collect(Collectors.toList());
    }

    public class MoreThanOneActiveRecoveryException extends Exception {

    }

}
