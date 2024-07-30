package com.example.hospitalsystem.controllers;

import com.example.hospitalsystem.entities.AdmissionState;
import com.example.hospitalsystem.entities.ClinicalData;
import com.example.hospitalsystem.entities.Department;
import com.example.hospitalsystem.entities.Patient;
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
    public Patient addPatient(@RequestBody Patient patient) {
        Patient savedPatient = patientService.savePatient(patient);
        auditTrailService.createAuditTrailForNewPatient(savedPatient.getId());
        return savedPatient;
    }

    @GetMapping
    public Patient getPatient(@RequestParam String name, @RequestParam String lastName) {
        return patientService.getPatient(name, lastName);
    }

    @GetMapping("/{id}")
    public Patient getPatient(@PathVariable Long id) {
        return patientService.getPatient(id);
    }

    @GetMapping("/all")
    public Iterable<Patient> getAllPatients() {
        return patientService.getAllPatients();
    }

    @DeleteMapping("/{id}")
    public void deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
    }

    @PutMapping("/{id}")
    public void updatePatient(@PathVariable Long id, @RequestBody Patient updatedPatient) throws IllegalAccessException {
        Patient originalPatient = patientService.getPatient(id);
        auditTrailService.createAuditTrailForPatientDataChange(originalPatient, updatedPatient);
        patientService.updatePatient(id, updatedPatient);
    }

    @PutMapping("/{patientId}/department/{departmentId}")
    public void assignDepartmentToPatient(@PathVariable Long patientId, @PathVariable Long departmentId) {
        try {
            auditTrailService.createAuditTrailForPatientDataChange(patientId, patientService.getPatient(patientId).getDepartment().getId(), departmentId);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        patientService.setDepartment(patientId, departmentService.getDepartment(departmentId));
    }

    @PostMapping("/{id}/admissionState")
    public void addAdmissionState(@PathVariable Long id, @RequestBody AdmissionState admissionState) {
        List<AdmissionState> states = patientService.getAllAdmissionStates(id);
        AdmissionState admissionStateWithId = patientService.addAdmissionState(id, admissionState);
        auditTrailService.createAuditTrailForNewAdmission(id, admissionStateWithId, states);
    }

    @GetMapping("/{id}/admissionState")
    public AdmissionState getAdmissionState(@PathVariable Long id) {
        return patientService.getAdmissionState(id);
    }

    @GetMapping("/{id}/allAdmissionStates")
    public List<AdmissionState> getAllAdmissionStates(@PathVariable Long id) {
        return patientService.getAllAdmissionStates(id);
    }

    @PutMapping("/{id}/clinicalData")
    public void setClinicalData(@PathVariable Long id, @RequestBody ClinicalData clinicalData) {
        String oldClinicalRecord = patientService.getClinicalRecord(id);
        auditTrailService.createAuditTrailForClinicalDataChange(id, oldClinicalRecord, clinicalData.getClinicalRecord());
        patientService.setClinicalData(id, clinicalData);
    }

    @GetMapping("/{id}/clinicalData")
    public ClinicalData getClinicalData(@PathVariable Long id) {
        ClinicalData data = new ClinicalData();
        data.setClinicalRecord(patientService.getClinicalRecord(id));
        return data;
    }

    @PutMapping("/{id}/discharge")
    public void dischargePatient(@PathVariable Long id, @RequestParam String reason) {
        auditTrailService.createAuditTrailForDischarge(id, reason);
        patientService.dischargePatient(id);
    }

    @GetMapping("/{id}/allClinicalData")
    public List<ClinicalData> getAllClinicalData(@PathVariable Long id) {
        return patientService.getAllClinicalData(id);
    }

    @DeleteMapping("/{id}/clinicalData")
    public void deleteClinicalData(@PathVariable Long id) {
        patientService.deleteClinicalData(id);
    }
}
