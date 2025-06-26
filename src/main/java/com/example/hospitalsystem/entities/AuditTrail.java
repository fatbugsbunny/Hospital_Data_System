package com.example.hospitalsystem.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class AuditTrail {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long patientId;

    @Column(nullable = false)
    private String tableName;

    @Column(nullable = false)
    private String columnName;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(nullable = false)
    private String oldValue;

    @Column(nullable = false)
    private String newValue;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patient_id) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AuditTrail that)) return false;
        return id == that.id && patientId == that.patientId && Objects.equals(tableName, that.tableName) && Objects.equals(columnName, that.columnName) && Objects.equals(timestamp, that.timestamp) && Objects.equals(oldValue, that.oldValue) && Objects.equals(newValue, that.newValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, patientId, tableName, columnName, timestamp, oldValue, newValue);
    }

    @Override
    public String toString() {
        return tableName + columnName + oldValue + newValue;
    }
}
