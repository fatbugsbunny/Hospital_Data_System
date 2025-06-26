package com.example.hospitalsystem.services;

import com.example.hospitalsystem.Dtos.AdmissionStateDto;
import com.example.hospitalsystem.Dtos.PatientDto;
import com.example.hospitalsystem.Dtos.PatientSummaryDto;
import com.example.hospitalsystem.entities.AdmissionState;
import com.example.hospitalsystem.entities.AuditTrail;
import com.example.hospitalsystem.entities.Patient;
import com.example.hospitalsystem.repositories.AuditTrailRepository;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
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

    public void createAuditTrailForNewPatient(Long id) {
        AuditTrail auditTrail = new AuditTrail();
        auditTrail.setPatientId(id);
        auditTrail.setTableName("patient");
        auditTrail.setColumnName("all");
        auditTrail.setOldValue("-");
        auditTrail.setNewValue("Patient with id: " + id);
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

    public void createAuditTrailForPatientDischarge(Long id, String reason) {
        AuditTrail auditTrail = new AuditTrail();
        auditTrail.setPatientId(id);
        auditTrail.setTableName("admission_state");
        auditTrail.setColumnName("discharge");
        auditTrail.setOldValue("false");
        auditTrail.setNewValue("true-" + reason);
        repository.save(auditTrail);
    }

    public void createAuditTrailForPatientDepartmentChange(Long patientId, Long oldDepartmentId, Long newDepartmentId) {
        AuditTrail auditTrail = new AuditTrail();
        auditTrail.setPatientId(patientId);
        auditTrail.setTableName("patient");
        auditTrail.setColumnName("department_id");
        auditTrail.setOldValue(oldDepartmentId.toString());
        auditTrail.setNewValue(newDepartmentId.toString());
        repository.save(auditTrail);
    }

    public void createAuditTrailForPatientDataChange(PatientDto original, PatientSummaryDto updated){
        if(!original.name().equals(updated.name())){
            createAuditForNameChange(original, updated);
        }

        if(!original.lastName().equals(updated.lastName())){
            createAuditForLastNameChange(original, updated);
        }

        if(original.birthDate() != updated.birthDate()){
            createAuditForBirthDateChange(original, updated);
        }
    }

    private void createAuditForLastNameChange(PatientDto original, PatientSummaryDto updated) {
        AuditTrail auditTrail = new AuditTrail();
        auditTrail.setPatientId(original.id());
        auditTrail.setTableName("patient");
        auditTrail.setColumnName("last_name");
        auditTrail.setOldValue(original.lastName());
        auditTrail.setNewValue(updated.lastName());
        repository.save(auditTrail);
    }

    private void createAuditForBirthDateChange(PatientDto original, PatientSummaryDto updated) {
        AuditTrail auditTrail = new AuditTrail();
        auditTrail.setPatientId(original.id());
        auditTrail.setTableName("patient");
        auditTrail.setColumnName("birthDate");
        auditTrail.setOldValue(original.birthDate().toString());
        auditTrail.setNewValue(updated.birthDate().toString());
        repository.save(auditTrail);
    }

    private void createAuditForNameChange(PatientDto original, PatientSummaryDto updated) {
        AuditTrail auditTrail = new AuditTrail();
        auditTrail.setPatientId(original.id());
        auditTrail.setTableName("patient");
        auditTrail.setColumnName("name");
        auditTrail.setOldValue(original.name());
        auditTrail.setNewValue(updated.name());
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

    public void createAuditTrailForNewAdmission(Long id, AdmissionStateDto admissionState, List<AdmissionStateDto> states) {
        AuditTrail auditTrail = new AuditTrail();
        if (states.isEmpty()) {
            auditTrail.setOldValue("-");
        } else {
            auditTrail.setOldValue(states.get(states.size() - 2).enteringDate().toString());
        }
        auditTrail.setPatientId(id);
        auditTrail.setTableName("admission_state");
        auditTrail.setColumnName("all");
        auditTrail.setNewValue("New admission with id: " + admissionState.id());
        repository.save(auditTrail);
    }


}
