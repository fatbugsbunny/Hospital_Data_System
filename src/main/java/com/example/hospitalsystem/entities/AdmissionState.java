package com.example.hospitalsystem.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class AdmissionState {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(nullable = false)
    private LocalDateTime enteringDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column
    private LocalDateTime exitingDate;

    @Column(nullable = false)
    private String cause;

    @Column(nullable = false)
    private String reason;

    @Column(nullable = false)
    private boolean discharge;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "clinical_data")
    private ClinicalData clinicalData;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDateTime getEnteringDate() {
        return enteringDate;
    }

    public void setEnteringDate(LocalDateTime enteringDate) {
        this.enteringDate = enteringDate;
    }

    public LocalDateTime getExitingDate() {
        return exitingDate;
    }

    public void setExitingDate(LocalDateTime exitingDate) {
        this.exitingDate = exitingDate;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public boolean isDischarge() {
        return discharge;
    }

    public void setDischarge(boolean discharge) {
        this.discharge = discharge;
    }

    public ClinicalData getClinicalData() {
        return clinicalData;
    }

    public void setClinicalData(ClinicalData clinicalData) {
        this.clinicalData = clinicalData;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }
}
