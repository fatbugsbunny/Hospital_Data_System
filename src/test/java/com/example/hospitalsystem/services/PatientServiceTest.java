package com.example.hospitalsystem.services;

import com.example.hospitalsystem.entities.AdmissionState;
import com.example.hospitalsystem.entities.ClinicalData;
import com.example.hospitalsystem.entities.Department;
import com.example.hospitalsystem.entities.Patient;
import com.example.hospitalsystem.exceptions.PatientDoesNotExistException;
import com.example.hospitalsystem.repositories.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatientServiceTest {
    @Mock
    PatientRepository repository;

    @InjectMocks
    PatientService service;

    @Nested
    class whenPatientExists{
        Patient patient = new Patient();

        @BeforeEach
        void setUp(){
            patient.setId(0L);
            when(repository.findById(0L)).thenReturn(Optional.of(patient));
        }

        @Test
        void shouldGetPatientById(){
            Patient returnedPatient = service.getPatient(0L);

            assertEquals(patient,returnedPatient);
            verify(repository, times(1)).findById(0L);
        }

        @Test
        void shouldGetPatientByNameAndLastName(){
            patient.setName("John");
            patient.setLastName("Doe");

            when(repository.findByNameAndLastName("John","Doe")).thenReturn(Optional.of(patient));

            Patient returnedPatient = service.getPatient("John","Doe");

            assertEquals(patient,returnedPatient);
            verify(repository, times(1)).findByNameAndLastName("John","Doe");
        }

        @Test
        void shouldUpdatePatient(){
            patient.setName("John");
            patient.setLastName("Doe");
            patient.setBirthDate(LocalDate.of(2000, 4, 4));

            Patient updatedPatient = new Patient();
            updatedPatient.setName("Gorge");
            updatedPatient.setLastName("Smith");
            updatedPatient.setBirthDate(LocalDate.of(2001, 5, 5));

            service.updatePatient(0L, updatedPatient);

            assertEquals(patient,updatedPatient);
            verify(repository, times(1)).findById(0L);
            verify(repository, times(1)).save(patient);
        }

        @Test
        void shouldDischargePatient(){
            AdmissionState state = new AdmissionState();
            state.setDischarge(false);
            patient.setAdmissionState(List.of(state));
            service.dischargePatient(0L);

            assertTrue(patient.getCurrentState().isDischarge());
            assertNotNull(patient.getCurrentState().getExitingDate());
            verify(repository, times(1)).findById(0L);
            verify(repository, times(1)).save(patient);
        }

        @Test
        void shouldGetAllClinicalData(){
            AdmissionState state1 = new AdmissionState();
            ClinicalData clinicalData1 = new ClinicalData();
            clinicalData1.setClinicalRecord("R1");
            state1.setClinicalData(clinicalData1);
            AdmissionState state2 = new AdmissionState();
            ClinicalData clinicalData2 = new ClinicalData();
            clinicalData2.setClinicalRecord("R2");
            state2.setClinicalData(clinicalData2);
            patient.setAdmissionState(List.of(state1, state2));

            List<ClinicalData> clinicalDataList = service.getAllClinicalData(0L);

            assertEquals(List.of(state1.getClinicalData(),state2.getClinicalData()), clinicalDataList);

            verify(repository, times(1)).findById(0L);
            verify(repository,never()).save(ArgumentMatchers.any(Patient.class));
        }

        @Test
        void shouldDeleteCurrentClinicalData(){
            AdmissionState state1 = new AdmissionState();
            ClinicalData clinicalData1 = new ClinicalData();
            clinicalData1.setClinicalRecord("R1");
            state1.setClinicalData(clinicalData1);
            patient.setAdmissionState(List.of(state1));

            service.deleteCurrentClinicalData(0L);

            assertNull(patient.getCurrentState().getClinicalData());
            verify(repository, times(1)).save(patient);
            verify(repository, times(1)).findById(0L);
        }

        @Test
        void shouldAddAdmissionState(){
            AdmissionState state1 = new AdmissionState();
            patient.setAdmissionState(new ArrayList<>());

            AdmissionState returnedState = service.addAdmissionState(0L, state1);

            state1.setPatient(patient);

            assertEquals(returnedState, state1);
            assertEquals(patient, returnedState.getPatient());
            verify(repository, times(1)).save(patient);
            verify(repository, times(1)).findById(0L);
        }

        @Test
        void shouldGetClinicalRecordWhenItExists(){
            AdmissionState state1 = new AdmissionState();
            ClinicalData clinicalData1 = new ClinicalData();
            clinicalData1.setClinicalRecord("R1");
            state1.setClinicalData(clinicalData1);
            patient.setAdmissionState(List.of(state1));

            String clinicalRecord = service.getCurrentClinicalRecord(0L);

            assertEquals(clinicalData1.getClinicalRecord(), clinicalRecord);
            verify(repository, times(1)).findById(0L);
        }

        @Test
        void shouldShowMessageWhenGettingClinicalRecordAndItDoesntExist(){
            AdmissionState state1 = new AdmissionState();
            patient.setAdmissionState(List.of(state1));
            String message = service.getCurrentClinicalRecord(0L);

            assertEquals("No clinical dat exists", message);
            verify(repository, times(1)).findById(0L);
        }

        @Test
        void shouldSetCurrentClinicalData(){
            AdmissionState state1 = new AdmissionState();
            patient.setAdmissionState(List.of(state1));
            ClinicalData data = new ClinicalData();
            data.setClinicalRecord("R");

            service.setCurrentClinicalData(0L, data);

            assertEquals(state1.getClinicalData(), data);
            assertEquals(state1,data.getAdmissionState());
            verify(repository, times(1)).save(patient);
            verify(repository, times(1)).findById(0L);
        }

        @Test
        void shouldSetDepartment(){
            Department department = new Department();

            service.setDepartment(0L, department);

            assertEquals(department,patient.getDepartment());
            assertEquals(department.getPatients().getFirst(),patient);
            verify(repository, times(1)).save(patient);
            verify(repository, times(1)).findById(0L);
        }
    }

    @Nested
    class whenPatientDoesNotExist{
        @Test
        void shouldThrowExceptionWhenGettingById(){
            when(repository.findById(0L)).thenReturn(Optional.empty());

            assertThrows(PatientDoesNotExistException.class, () -> service.getPatient(0L));
            verify(repository, times(1)).findById(0L);
        }

        @Test
        void shouldThrowExceptionWhenGettingByNameAndLastName(){
            when(repository.findByNameAndLastName("John","Doe")).thenReturn(Optional.empty());

            assertThrows(PatientDoesNotExistException.class, () -> service.getPatient("John","Doe"));
            verify(repository, times(1)).findByNameAndLastName("John","Doe");
        }

    }

}
