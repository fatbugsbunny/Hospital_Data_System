package com.example.hospitalsystem.services;

import com.example.hospitalsystem.Dtos.*;
import com.example.hospitalsystem.entities.*;
import com.example.hospitalsystem.exceptions.PatientDoesNotExistException;
import com.example.hospitalsystem.mappers.DepartmentMapper;
import com.example.hospitalsystem.mappers.PatientMapper;
import com.example.hospitalsystem.repositories.PatientRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PatientService {
    private final PatientRepository repository;
    private final PatientMapper mapper;
    private final DepartmentMapper departmentMapper;

    public PatientService(PatientRepository repository, PatientMapper mapper, DepartmentMapper departmentMapper) {
        this.departmentMapper = departmentMapper;
        this.mapper = mapper;
        this.repository = repository;
    }

    public PatientDto savePatient(PatientDto patientDto) {
        return mapper.toDto(repository.save(mapper.toEntity(patientDto)));
    }

    public Iterable<PatientDto> getAllPatients() {
        return mapper.toDtos(repository.findAll());
    }

    public PatientDto getPatient(String name, String lastName) {
        return mapper.toDto(repository.findByNameAndLastName(name, lastName).orElseThrow(() -> new PatientDoesNotExistException("Patient with name " + name + " and surname " + lastName + " does not exist")));
    }

    public PatientDto getPatient(Long patientId) {
        return getPatientDtoOrThrowEx(patientId);
    }

    public String deletePatient(Long id) {
        repository.deleteById(id);
        return "Patient deleted successfully";
    }

    public void updatePatient(Long patientId, PatientSummaryDto patientSummaryDto) {
        Patient patient = repository.findById(patientId).orElseThrow(() -> new PatientDoesNotExistException("Patient with id " + patientId + " does not exist"));
        repository.save(mapper.updatePatientFromDto(patientSummaryDto, patient));
    }

    public void dischargePatient(Long patientId) {
        Patient patient = repository.findById(patientId).orElseThrow(() -> new PatientDoesNotExistException("Patient with id " + patientId + " does not exist"));
        patient.getCurrentAdmissionState().setDischarge(true);
        patient.getCurrentAdmissionState().setExitingDate(LocalDateTime.now());
        repository.save(patient);
    }

    public List<AdmissionStateDto> getAllAdmissionStates(Long patientId) {
        return getPatientDtoOrThrowEx(patientId).admissionStates();
    }

    public AdmissionStateDto getCurrentAdmissionState(Long patientId) {
        return getPatientDtoOrThrowEx(patientId).currentAdmissionState();
    }

    public AdmissionStateDto addAdmissionState(Long patientId, AdmissionStateDto admissionState) {
        Patient patient = repository.findById(patientId).orElseThrow(() -> new PatientDoesNotExistException("Patient with id " + patientId + " does not exist"));
        patient.addAdmissionState(mapper.toEntity(admissionState));
        repository.save(patient);
        return mapper.toDto(patient.getCurrentAdmissionState());
    }


    public List<ClinicalDataDto> getAllClinicalData(Long patientId) {
        PatientDto patientDto = getPatientDtoOrThrowEx(patientId);
        List<ClinicalDataDto> clinicalDataList = new ArrayList<>();
        patientDto.admissionStates().stream().map(AdmissionStateDto::clinicalData).forEach(clinicalDataList::add);
        return clinicalDataList;
    }

    public ClinicalDataDto getCurrentClinicalData(Long patientId){
        PatientDto patientDto = getPatientDtoOrThrowEx(patientId);
        return patientDto.currentAdmissionState().clinicalData();
    }

    public String getCurrentClinicalRecord(Long patientId) {
        PatientDto patientDto = getPatientDtoOrThrowEx(patientId);
        return patientDto.currentAdmissionState().clinicalData() != null ? patientDto.currentAdmissionState().clinicalData().clinicalRecord() : "No clinical dat exists";
    }

    public void deleteCurrentClinicalData(Long patientId) {
        Patient patient = repository.findById(patientId).orElseThrow(() -> new PatientDoesNotExistException("Patient with id " + patientId + " does not exist"));
        patient.getCurrentAdmissionState().setClinicalData(null);
        repository.save(patient);
    }

    public void setCurrentClinicalData(Long patientId, ClinicalDataDto clinicalData) {
        Patient patient = repository.findById(patientId).orElseThrow(() -> new PatientDoesNotExistException("Patient with id " + patientId + " does not exist"));
        patient.setCurrentClinicalData(mapper.toEntity(clinicalData));
        repository.save(patient);
    }

    public void setDepartment(Long patientId, DepartmentDto department) {
        Patient patient = repository.findById(patientId).orElseThrow(() -> new PatientDoesNotExistException("Patient with id " + patientId + " does not exist"));
        patient.setDepartment(departmentMapper.toEntity(department));
        repository.save(patient);
    }

    private PatientDto getPatientDtoOrThrowEx(Long id) {
        return mapper.toDto(repository.findById(id).orElseThrow(() -> new PatientDoesNotExistException("Patient with id " + id + " does not exist")));
    }

}
