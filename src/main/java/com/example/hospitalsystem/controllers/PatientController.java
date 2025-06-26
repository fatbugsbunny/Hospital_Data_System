package com.example.hospitalsystem.controllers;

import com.example.hospitalsystem.Dtos.AdmissionStateDto;
import com.example.hospitalsystem.Dtos.ClinicalDataDto;
import com.example.hospitalsystem.Dtos.PatientDto;
import com.example.hospitalsystem.Dtos.PatientSummaryDto;
import com.example.hospitalsystem.entities.AdmissionState;
import com.example.hospitalsystem.entities.ClinicalData;
import com.example.hospitalsystem.entities.Patient;
import com.example.hospitalsystem.exceptions.PatientIsAlreadyAdmittedException;
import com.example.hospitalsystem.services.AuditTrailService;
import com.example.hospitalsystem.services.DepartmentService;
import com.example.hospitalsystem.services.PatientService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:8100")
@RestController
@RequestMapping("/patient")
public class PatientController {
    private final PatientService patientService;
    private final DepartmentService departmentService;
    private final AuditTrailService auditTrailService;

    public PatientController(PatientService patientService, DepartmentService departmentService, AuditTrailService auditTrailService) {
        this.departmentService = departmentService;
        this.patientService = patientService;
        this.auditTrailService = auditTrailService;
    }

    @PostMapping("/add")
    public PatientDto addPatient(@RequestBody PatientDto patient) {
        PatientDto newPatient = patientService.savePatient(patient);
        auditTrailService.createAuditTrailForNewPatient(newPatient.id());
        return newPatient;
    }

    @GetMapping
    public PatientDto getPatient(@RequestParam String name, @RequestParam String lastName) {
        return patientService.getPatient(name, lastName);
    }

    @GetMapping("/{id}")
    public PatientDto getPatient(@PathVariable Long id) {
        return patientService.getPatient(id);
    }

    @GetMapping("/all")
    public Iterable<PatientDto> getAllPatients() {
        return patientService.getAllPatients();
    }

    @DeleteMapping("/{id}")
    public void deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
    }

    @PutMapping("/{id}")
    public void updatePatient(@PathVariable Long id, @RequestBody PatientSummaryDto updatedPatient) throws IllegalAccessException {
        PatientDto originalPatient = patientService.getPatient(id);
        auditTrailService.createAuditTrailForPatientDataChange(originalPatient, updatedPatient);
        patientService.updatePatient(id, updatedPatient);
    }

    @PutMapping("/{patientId}/department/{newDepartmentId}")
    public void assignDepartmentToPatient(@PathVariable Long patientId, @PathVariable Long newDepartmentId) {
        try {
            auditTrailService.createAuditTrailForPatientDepartmentChange(patientId, patientService.getPatient(patientId).department().id(), newDepartmentId);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        patientService.setDepartment(patientId, departmentService.getDepartment(newDepartmentId));
    }

    @PostMapping("/{id}/admissionState")
    public void addAdmissionState(@PathVariable Long id, @RequestBody AdmissionStateDto admissionState) {
        List<AdmissionStateDto> states = patientService.getAllAdmissionStates(id);
        if(!states.getLast().discharge()){
            throw new PatientIsAlreadyAdmittedException();
        }
        AdmissionStateDto admissionStateWithId = patientService.addAdmissionState(id, admissionState);
        auditTrailService.createAuditTrailForNewAdmission(id, admissionStateWithId, states);
    }

    @GetMapping("/{id}/admissionState")
    public AdmissionStateDto getCurrentAdmissionState(@PathVariable Long id) {
        return patientService.getCurrentAdmissionState(id);
    }

    @GetMapping("/{id}/allAdmissionStates")
    public List<AdmissionStateDto> getAllAdmissionStates(@PathVariable Long id) {
        return patientService.getAllAdmissionStates(id);
    }

    @PutMapping("/{id}/clinicalData")
    public void setCurrentClinicalData(@PathVariable Long id, @RequestBody ClinicalDataDto clinicalData) {
        String oldClinicalRecord = patientService.getCurrentClinicalRecord(id);
        auditTrailService.createAuditTrailForClinicalDataChange(id, oldClinicalRecord, clinicalData.clinicalRecord());
        patientService.setCurrentClinicalData(id, clinicalData);
    }

    @GetMapping("/{id}/clinicalData")
    public ClinicalDataDto getCurrentClinicalData(@PathVariable Long id) {
        return patientService.getCurrentClinicalData(id);
    }

    @PutMapping("/{id}/discharge")
    public void dischargePatient(@PathVariable Long id, @RequestParam String reason) {
        auditTrailService.createAuditTrailForPatientDischarge(id, reason);
        patientService.dischargePatient(id);
    }

    @GetMapping("/{id}/allClinicalData")
    public List<ClinicalDataDto> getAllClinicalData(@PathVariable Long id) {
        return patientService.getAllClinicalData(id);
    }

    @DeleteMapping("/{id}/clinicalData")
    public void deleteCurrentClinicalData(@PathVariable Long id) {
        patientService.deleteCurrentClinicalData(id);
    }
}
