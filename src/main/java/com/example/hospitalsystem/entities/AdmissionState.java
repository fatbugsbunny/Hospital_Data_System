package com.example.hospitalsystem.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class AdmissionState {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime enteringDate;

    @Column
    private LocalDateTime exitingDate;

    @Column(nullable = false)
    private String cause;

    @Column(nullable = false)
    private String reason;

    @Column(nullable = false)
    private boolean discharge;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "clinical_data", referencedColumnName = "id")
    private ClinicalData clinicalData;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AdmissionState state)) return false;
        return Objects.equals(id, state.id) && Objects.equals(enteringDate, state.enteringDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, enteringDate);
    }
}
