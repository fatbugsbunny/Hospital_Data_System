package com.example.hospitalsystem;

import jakarta.persistence.*;

@Entity
public class ClinicalData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String clinicalRecord;

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
}
