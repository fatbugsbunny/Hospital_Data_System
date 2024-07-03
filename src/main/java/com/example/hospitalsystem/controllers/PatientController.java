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
    public void addPatient(@RequestBody Patient patient) {
        auditTrailService.createAuditTrailForNewPatient(patient.getId());
        patientService.createPatient(patient);
    }

    @GetMapping
    public Patient getPatient(@RequestParam String name, @RequestParam String lastName) {
        return patientService.getPatient(name, lastName);
    }

    @GetMapping("/{id}")
    public Patient getPatient(@PathVariable Long id){
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
    public void updatePatient(@PathVariable Long id, @RequestBody Patient updatedPatient) {
        Patient originalPatient = patientService.getPatient(id);
        if(originalPatient.getDepartment() == null){
        }else if(originalPatient.getDepartment().getId() != updatedPatient.getDepartment().getId()){
            auditTrailService.createAuditTrailForDepartmentChange(updatedPatient, originalPatient);
        }
        patientService.updatePatient(id, updatedPatient);
    }

    @PutMapping("/{patientId}/department/{departmentId}")
    public void assignDepartmentToPatient(@PathVariable Long patientId, @PathVariable Long departmentId) {
        Patient patient = patientService.getPatient(patientId);
        Department department = departmentService.getDepartment(departmentId);
        patient.setDepartment(department);
        patientService.createPatient(patient);
    }

    @PostMapping("/{id}/admissionState")
    public void addAdmissionState(@PathVariable Long id, @RequestBody AdmissionState admissionState) {
        List<AdmissionState> states = patientService.getAllAdmissionStates(id);
        patientService.addAdmissionState(id, admissionState);
        auditTrailService.createAuditTrailForNewAdmission(id,admissionState, states);
    }

    @GetMapping("/{id}/admissionState")
    public AdmissionState getAdmissionState(@PathVariable Long id) {
        return patientService.getAdmissionState(id);
    }

    @PostMapping("/{id}/clinicalData")
    public void addClinicalData(@PathVariable Long id, @RequestBody ClinicalData clinicalData) {
        String oldClinicalRecord = patientService.getClinicalRecord(id);

        auditTrailService.createAuditTrailForClinicalDataChange(id, oldClinicalRecord, clinicalData.getClinicalRecord());
        patientService.editClinicalData(id, clinicalData);
    }

    @GetMapping("/{id}/clinicalData")
    public ClinicalData getClinicalData(@PathVariable Long id) {
        ClinicalData data = new ClinicalData();
        data.setClinicalRecord(patientService.getClinicalRecord(id));
        return data;
    }

    @PostMapping("/{id}/discharge")
    public void dischargePatient(@PathVariable Long id, @RequestBody String reason){
        auditTrailService.createAuditTrailForDischarge(id,reason);
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
