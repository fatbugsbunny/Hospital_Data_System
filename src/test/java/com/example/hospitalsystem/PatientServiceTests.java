package com.example.hospitalsystem;

import com.example.hospitalsystem.entities.AdmissionState;
import com.example.hospitalsystem.entities.ClinicalData;
import com.example.hospitalsystem.entities.Department;
import com.example.hospitalsystem.entities.Patient;
import com.example.hospitalsystem.repositories.PatientRepository;
import com.example.hospitalsystem.services.PatientService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@ExtendWith(MockitoExtension.class)
class PatientServiceTests {
    @Mock
    private PatientRepository repository;

    @InjectMocks
    private PatientService patientService;

    @Test
    void changeDischargeOfPatient_True(){
        Patient patient = new Patient();
        List<AdmissionState> admissionStates = new ArrayList<>();
        AdmissionState admissionState = new AdmissionState();
        admissionState.setDischarge(false);
        admissionStates.add(admissionState);
        patient.setId(0L);
        patient.setAdmissionState(admissionStates);

        Mockito.when((repository.findById(0L))).thenReturn(Optional.of(patient));

        patientService.dischargePatient(0L);

        assertTrue(patient.getCurrentState().isDischarge());
        Mockito.verify(repository, Mockito.times(1)).findById(0L);
    }

    @Test
    void updatePatient_ChangeValues(){
        Patient originalPatient = new Patient();
        Department originalDepartment = new Department();
        LocalDate originalDate = LocalDate.now();
        originalDepartment.setName("Original");
        originalDepartment.setId(0L);
        originalDepartment.setCode("1");
        originalPatient.setDepartment(originalDepartment);
        originalPatient.setBirthDate(originalDate);
        originalPatient.setName("Jorge");
        originalPatient.setLastName("Michal");

        Patient updatedPatient = new Patient();
        Department updatedDepartment = new Department();
        LocalDate updatedDate = LocalDate.now();
        updatedDepartment.setName("Updated");
        updatedDepartment.setId(1L);
        updatedDepartment.setCode("2");
        updatedPatient.setDepartment(updatedDepartment);
        updatedPatient.setBirthDate(updatedDate);
        updatedPatient.setName("Smith");
        updatedPatient.setLastName("White");

        Mockito.when((repository.findById(0L))).thenReturn(Optional.of(originalPatient));

        patientService.updatePatient(0L, updatedPatient);

        assertEquals(originalPatient,updatedPatient);
        Mockito.verify(repository, Mockito.times(1)).findById(0L);
        Mockito.verify(repository, Mockito.times(1)).save(originalPatient);
    }

    @Test
    void getAllClinicalData_ClinicalDataList(){
        Patient patient = new Patient();
        patient.setId(0L);
        List<AdmissionState> admissionStates = new ArrayList<>();
        AdmissionState admissionState = new AdmissionState();
        admissionState.setClinicalData(new ClinicalData());
        AdmissionState admissionState1 = new AdmissionState();
        admissionState1.setClinicalData(new ClinicalData());
        admissionStates.add(admissionState);
        admissionStates.add(admissionState1);
        patient.setAdmissionState(admissionStates);

        Mockito.when((repository.findById(0L))).thenReturn(Optional.of(patient));

        List<ClinicalData> clinicalDataList =  patientService.getAllClinicalData(0L);

        Mockito.verify(repository, Mockito.times(1)).findById(0L);
        assertFalse(clinicalDataList.isEmpty());
    }

    @Test
    void deleteCurrentClinicalData_Null(){
        Patient patient = new Patient();
        patient.setId(0L);
        List<AdmissionState> admissionStates = new ArrayList<>();
        AdmissionState admissionState = new AdmissionState();
        admissionState.setClinicalData(new ClinicalData());
        AdmissionState admissionState1 = new AdmissionState();
        admissionState1.setClinicalData(new ClinicalData());
        admissionStates.add(admissionState);
        admissionStates.add(admissionState1);
        patient.setAdmissionState(admissionStates);

        Mockito.when(repository.findById(0L)).thenReturn(Optional.of(patient));

        patientService.deleteClinicalData(0L);

        Mockito.verify(repository, Mockito.times(1)).findById(0L);
        assertNull(patient.getCurrentState().getClinicalData());
    }

    @Test
    void addAdmissionState_NewAdmissionState(){
        Patient patient = new Patient();
        patient.setId(0L);
        List<AdmissionState> admissionStates = new ArrayList<>();
        AdmissionState admissionState = new AdmissionState();
        admissionState.setClinicalData(new ClinicalData());
        admissionStates.add(admissionState);
        patient.setAdmissionState(admissionStates);

        AdmissionState admissionState1 = new AdmissionState();
        admissionState1.setClinicalData(new ClinicalData());

        Mockito.when(repository.findById(0L)).thenReturn(Optional.of(patient));

        patientService.addAdmissionState(0L, admissionState1);

        Mockito.verify(repository, Mockito.times(1)).findById(0L);
        assertEquals(2, patient.getAdmissionState().size());
    }

    @Test
    void setClinicalData_ChangeData(){
        Patient patient = new Patient();
        patient.setId(0L);
        List<AdmissionState> admissionStates = new ArrayList<>();
        AdmissionState admissionState = new AdmissionState();
        admissionState.setClinicalData(new ClinicalData());
        admissionStates.add(admissionState);
        patient.setAdmissionState(admissionStates);

        ClinicalData data = new ClinicalData();
        data.setClinicalRecord("AAAA");

        Mockito.when(repository.findById(0L)).thenReturn(Optional.of(patient));

        patientService.setClinicalData(0L, data);

        Mockito.verify(repository, Mockito.times(1)).findById(0L);
        assertEquals(patient.getCurrentState().getClinicalData().getClinicalRecord(), data.getClinicalRecord());
    }
}

