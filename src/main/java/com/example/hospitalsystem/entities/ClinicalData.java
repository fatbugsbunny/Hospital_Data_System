package com.example.hospitalsystem.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
public class ClinicalData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String clinicalRecord;

    @JsonIgnore
    @OneToOne(mappedBy = "clinicalData", cascade = CascadeType.ALL)
    private AdmissionState admissionState;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getClinicalRecord() {
        return clinicalRecord;
    }

    public void setClinicalRecord(String clinicalRecord) {
        this.clinicalRecord = clinicalRecord;
    }

    public AdmissionState getAdmissionState() {
        return admissionState;
    }

    public void setAdmissionState(AdmissionState admissionState) {
        this.admissionState = admissionState;
    }
}
