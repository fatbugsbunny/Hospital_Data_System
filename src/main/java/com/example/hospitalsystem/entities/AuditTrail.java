package com.example.hospitalsystem.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
public class AuditTrail {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private long patientId;

    @Column(nullable = false)
    private String tableName;

    @Column(nullable = false)
    private String columnName;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(nullable = false)
    private String changedBy;

    @Column(nullable = false)
    private String oldValue;

    @Column(nullable = false)
    private String newValue;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getPatientId() {
        return patientId;
    }

    public void setPatientId(long patient_id) {
        this.patientId = patient_id;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String table) {
        this.tableName = table;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String column) {
        this.columnName = column;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime time) {
        this.timestamp = time;
    }

    public String getChangedBy() {
        return changedBy;
    }

    public void setChangedBy(String changed_by) {
        this.changedBy = changed_by;
    }

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String old_value) {
        this.oldValue = old_value;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String new_value) {
        this.newValue = new_value;
    }
}
