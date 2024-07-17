package com.example.hospitalsystem.services;

import com.example.hospitalsystem.entities.*;
import com.example.hospitalsystem.exceptions.PatientDoesNotExistException;
import com.example.hospitalsystem.repositories.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PatientService {
    private final PatientRepository repository;
    public PatientService(PatientRepository repository) {
        this.repository = repository;
    }

    public Patient createPatient(Patient patient) {
        return repository.save(patient);
    }

    public Patient getPatient(String name, String lastName) {
        return repository.findByNameAndLastName(name, lastName).orElseThrow(() -> new PatientDoesNotExistException("Patient with name " + name + " and surname " + lastName + " does not exist"));

    }

    public Iterable<Patient> getAllPatients() {
        return repository.findAll();
    }

    public Patient getPatient(Long patientId){
        return repository.findById(patientId).orElseThrow(() -> new PatientDoesNotExistException("Patient with id " + patientId + " does not exist"));
    }

    public String deletePatient(Long id) {
        repository.deleteById(id);
        return "Patient deleted successfully";
    }

    public void updatePatient(Long patientId, Patient updatedPatient) {
        Patient originalPatient = repository.findById(patientId).orElseThrow(() -> new PatientDoesNotExistException("Patient with id " + patientId + " does not exist"));
        repository.save(copyFields(updatedPatient, originalPatient));
    }

    public void dischargePatient(Long patientId) {
        Patient patient = repository.findById(patientId).orElseThrow(() -> new PatientDoesNotExistException("Patient with id " + patientId + " does not exist"));
        patient.getCurrentState().setDischarge(true);
    }

    public List<ClinicalData> getAllClinicalData(Long patientId) {
        Patient patient = repository.findById(patientId).orElseThrow(() -> new PatientDoesNotExistException("Patient with id " + patientId + " does not exist"));
        List<ClinicalData> clinicalDataList = new ArrayList<>();
        patient.getAdmissionStates().stream().map(AdmissionState::getClinicalData).forEach(clinicalDataList::add);
        return clinicalDataList;
    }

    public void deleteClinicalData(Long patientId) {
        Patient patient = repository.findById(patientId).orElseThrow(() -> new PatientDoesNotExistException("Patient with id " + patientId + " does not exist"));
        patient.getCurrentState().setClinicalData(null);
        repository.save(patient);
    }

    public void addAdmissionState(Long patientId, AdmissionState admissionState) {
        Patient patient = repository.findById(patientId).orElseThrow(() -> new PatientDoesNotExistException("Patient with id " + patientId + " does not exist"));
        patient.addAdmissionState(admissionState);
        repository.save(patient);
    }

    public AdmissionState getAdmissionState(Long patientId) {
        Patient patient = repository.findById(patientId).orElseThrow(() -> new PatientDoesNotExistException("Patient with id " + patientId + " does not exist"));
        return patient.getCurrentState();
    }

    public List<AdmissionState> getAllAdmissionStates(Long patientId){
        Patient patient = repository.findById(patientId).orElseThrow(() -> new PatientDoesNotExistException("Patient with id " + patientId + " does not exist"));
        return patient.getAdmissionStates();
    }

    public String getClinicalRecord(Long patientId) {
        Patient patient = repository.findById(patientId).orElseThrow(() -> new PatientDoesNotExistException("Patient with id " + patientId + " does not exist"));
        return patient.getCurrentState().getClinicalData() != null ? patient.getCurrentState().getClinicalData().getClinicalRecord() : "No clinical dat exists";
    }

    public void editClinicalData(Long patientId, ClinicalData clinicalData) {
        Patient patient = repository.findById(patientId).orElseThrow(() -> new PatientDoesNotExistException("Patient with id " + patientId + " does not exist"));
        patient.getCurrentState().setClinicalData(clinicalData);
        repository.save(patient);
    }

    private Patient copyFields(Patient source, Patient target) {
        target.setBirthDate(source.getBirthDate());
        target.setLastName(source.getLastName());
        target.setName(source.getName());
        target.setDepartment(source.getDepartment());
        return target;
    }
}
