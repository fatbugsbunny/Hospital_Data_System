package com.example.hospitalsystem.services;

import com.example.hospitalsystem.entities.AdmissionState;
import com.example.hospitalsystem.entities.AuditTrail;
import com.example.hospitalsystem.entities.Patient;
import com.example.hospitalsystem.repositories.AuditTrailRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuditTrailService {
    private final AuditTrailRepository repository;

    public AuditTrailService(AuditTrailRepository repository) {
        this.repository = repository;
    }

    public void add(AuditTrail auditTrail) {
        repository.save(auditTrail);
    }

    public void createAuditTrailForDischarge(Long id, String reason) {
        AuditTrail auditTrail = new AuditTrail();
        auditTrail.setPatientId(id);
        auditTrail.setTableName("admission_state");
        auditTrail.setColumnName("discharge");
        auditTrail.setOldValue("false");
        auditTrail.setNewValue("true-" + reason);
        repository.save(auditTrail);
    }

    public void createAuditTrailForNewAdmission(Long id,AdmissionState admissionState, List<AdmissionState> states) {
        AuditTrail auditTrail = new AuditTrail();
        if (states.isEmpty()) {
            auditTrail.setOldValue("-");
        } else {
            auditTrail.setOldValue(states.get(states.size() - 1).getEnteringDate().toString());
        }
        auditTrail.setPatientId(id);
        auditTrail.setTableName("admission_state");
        auditTrail.setColumnName("all");
        auditTrail.setNewValue("New admission with id: " + admissionState.getId());
        repository.save(auditTrail);
    }

    public void createAuditTrailForDepartmentChange(Patient updatedPatient, Patient originalPatient) {
        AuditTrail auditTrail = new AuditTrail();
        auditTrail.setPatientId(originalPatient.getId());
        auditTrail.setTableName("department");
        auditTrail.setColumnName("name");
        auditTrail.setOldValue(originalPatient.getDepartment().getName());
        auditTrail.setNewValue(updatedPatient.getDepartment().getName());
        repository.save(auditTrail);
    }

    public void createAuditTrailForNewPatient(Long id) {
        AuditTrail auditTrail = new AuditTrail();
        auditTrail.setPatientId(id);
        auditTrail.setTableName("patient");
        auditTrail.setColumnName("all");
        auditTrail.setOldValue("-");
        auditTrail.setNewValue("Patient with id: " + id);
        repository.save(auditTrail);
    }

    public void createAuditTrailForClinicalDataChange(Long id, String previousClinicalData, String newClinicalData) {
        AuditTrail auditTrail = new AuditTrail();
        auditTrail.setPatientId(id);
        auditTrail.setTableName("clinical_data");
        auditTrail.setColumnName("clinical_record");
        auditTrail.setOldValue(previousClinicalData);
        auditTrail.setNewValue(newClinicalData);
        repository.save(auditTrail);
    }

    public void createAuditTrailForNewDepartment(Long id) {
        AuditTrail auditTrail = new AuditTrail();
        auditTrail.setPatientId(id);
        auditTrail.setTableName("department");
        auditTrail.setColumnName("all");
        auditTrail.setOldValue("-");
        auditTrail.setNewValue("Department with id: " + id);
        repository.save(auditTrail);
    }
}
