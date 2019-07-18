package Entities;

import javax.persistence.*;
import java.io.*;
import java.sql.Time;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
public class Monitoring implements Entry {
    @Id
    @GeneratedValue
    @Column(name = "monitoring_id")
    private int id;

    @Temporal(TemporalType.TIME)
    private Date date;

    private int systolicPressure;
    private int diastolicPressure;

    @Column(name ="beatsPerMinute")
    private int heartRate;

    private double temperature;

    @ManyToOne
    @JoinColumn(name = "recovery_id")
    private Recovery recovery;

    public Monitoring(Date date, int diastolicPressure, int systolicPressure, int heartRate, double temperature)
            throws IllegalArgumentException {
        if(temperature <= -273.15) throw new IllegalArgumentException("Monitoring: patient has reached zero entropy; must " +
                "re-establish thermodynamics laws.");
        if(systolicPressure < 0 || diastolicPressure < 0 || heartRate < 0 || temperature < 0) throw
            new IllegalArgumentException("Monitoring: illegal arguments");
        this.date = new java.sql.Date(date.getTime());
        this.systolicPressure = systolicPressure;
        this.diastolicPressure = diastolicPressure;
        this.heartRate = heartRate;
        this.temperature = temperature;
    }

    public Monitoring(Date date, int diastolicPressure, int systolicPressure, int heartRate, double temperature,
                      Recovery recovery)
            throws IllegalArgumentException {
        this(date, diastolicPressure, systolicPressure, heartRate, temperature);
        this.recovery = recovery;
    }


    public Monitoring() {

    }

    public Monitoring defaultMonitoring() {
        this.diastolicPressure = 80;
        this.systolicPressure = 120;
        this.temperature = 37;
        this.heartRate = 75;
        this.date = new java.sql.Date(new Date().getTime());
        return this;
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

    public int getSystolicPressure() {
        return systolicPressure;
    }

    public void setSystolicPressure(int systolicPressure) {
        this.systolicPressure = systolicPressure;
    }

    public int getDiastolicPressure() {
        return diastolicPressure;
    }

    public void setDiastolicPressure(int diastolicPressure) {
        this.diastolicPressure = diastolicPressure;
    }

    public int getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(int heartRate) {
        this.heartRate = heartRate;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public Recovery getRecovery() {
        return recovery;
    }

    public void setRecovery(Recovery recovery) {
        this.recovery = recovery;
    }

    public String toString() {
        String s = String.format("{%s, %d, %d, %d, %fd", this.date, this.diastolicPressure, this.systolicPressure,
                this.heartRate, this.temperature);

        if (recovery != null) s += ", " + this.recovery.getId();
        s += "}";
        return s;
    }
}
