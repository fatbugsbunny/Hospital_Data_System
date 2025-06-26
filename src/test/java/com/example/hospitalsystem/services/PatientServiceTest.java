package com.example.hospitalsystem.services;

import com.example.hospitalsystem.Dtos.*;
import com.example.hospitalsystem.entities.AdmissionState;
import com.example.hospitalsystem.entities.ClinicalData;
import com.example.hospitalsystem.entities.Department;
import com.example.hospitalsystem.entities.Patient;
import com.example.hospitalsystem.exceptions.PatientDoesNotExistException;
import com.example.hospitalsystem.mappers.DepartmentMapper;
import com.example.hospitalsystem.mappers.PatientMapper;
import com.example.hospitalsystem.repositories.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatientServiceTest {
    @Mock
    PatientRepository repository;

    @Mock
    PatientMapper patientMapper;
    @Mock
    DepartmentMapper departmentMapper;

    @InjectMocks
    PatientService service;


    @Test
    void shouldGetPatientByNameAndLastName() {
        PatientDto patientDto = new PatientDto(0L, "John", "Doe", null, null, null);
        Patient patient = new Patient();
        patient.setId(0L);
        patient.setName("John");
        patient.setLastName("Doe");

        when(repository.findByNameAndLastName("John", "Doe")).thenReturn(Optional.of(patient));
        when(patientMapper.toDto(patient)).thenReturn(patientDto);

        PatientDto returnedPatient = service.getPatient("John", "Doe");

        assertEquals(patientDto, returnedPatient);
        verify(repository, times(1)).findByNameAndLastName("John", "Doe");
    }

    @Nested
    class whenPatientExists {
        Patient patient = new Patient();

        @BeforeEach
        void setUp() {
            patient.setId(0L);
            when(repository.findById(0L)).thenReturn(Optional.of(patient));
        }

        @Test
        void shouldGetPatientById() {
            PatientDto patientDto = new PatientDto(0L, null, null, null, null, null);

            when(patientMapper.toDto(patient)).thenReturn(patientDto);
            PatientDto returnedPatient = service.getPatient(0L);

            assertEquals(patientDto, returnedPatient);
            verify(repository, times(1)).findById(0L);
        }

        @Test
        void shouldUpdatePatient() {
            PatientSummaryDto updatedPatientDto = new PatientSummaryDto("George", null, null);
            Patient updatedPatient = new Patient();
            updatedPatient.setId(0L);
            updatedPatient.setName("George");

            when(patientMapper.updatePatientFromDto(updatedPatientDto, patient)).thenReturn(updatedPatient);

            service.updatePatient(0L, updatedPatientDto);

            verify(repository, times(1)).findById(0L);
            verify(repository, times(1)).save(updatedPatient);
        }

        @Test
        void shouldDischargePatient() {
            AdmissionState state = new AdmissionState();
            state.setDischarge(false);
            patient.setAdmissionStates(List.of(state));
            service.dischargePatient(0L);

            assertTrue(patient.getCurrentAdmissionState().isDischarge());
            assertNotNull(patient.getCurrentAdmissionState().getExitingDate());
            verify(repository, times(1)).findById(0L);
            verify(repository, times(1)).save(patient);
        }

        @Test
        void shouldGetAllClinicalData() {
            ClinicalDataDto clinicalDataDto1 = new ClinicalDataDto("R1");
            ClinicalDataDto clinicalDataDto2 = new ClinicalDataDto("R2");

            AdmissionStateDto state1Dto = new AdmissionStateDto(null, null, null, null, null, null, clinicalDataDto1);
            AdmissionStateDto state2Dto = new AdmissionStateDto(null, null, null, null, null, null, clinicalDataDto2);

            PatientDto patientDto = new PatientDto(0L, null, null, null, null, List.of(state1Dto, state2Dto));

            when(patientMapper.toDto(patient)).thenReturn(patientDto);

            List<ClinicalDataDto> clinicalDataList = service.getAllClinicalData(0L);

            assertEquals(List.of(clinicalDataDto1, clinicalDataDto2), clinicalDataList);
            verify(repository, times(1)).findById(0L);
        }

        @Test
        void shouldDeleteCurrentClinicalData() {
            AdmissionState state1 = new AdmissionState();
            ClinicalData clinicalData1 = new ClinicalData();
            clinicalData1.setClinicalRecord("R1");
            state1.setClinicalData(clinicalData1);
            patient.setAdmissionStates(List.of(state1));

            service.deleteCurrentClinicalData(0L);

            assertNull(patient.getCurrentAdmissionState().getClinicalData());
            verify(repository, times(1)).save(patient);
            verify(repository, times(1)).findById(0L);
        }

        @Test
        void shouldAddAdmissionState() {
            AdmissionStateDto stateDto = new AdmissionStateDto(0L);
            AdmissionState state = new AdmissionState();
            state.setId(0L);
            patient.setAdmissionStates(new ArrayList<>());

            when(patientMapper.toEntity(stateDto)).thenReturn(state);
            when(patientMapper.toDto(state)).thenReturn(stateDto);

            AdmissionStateDto returnedState = service.addAdmissionState(0L, stateDto);

            assertEquals(returnedState, stateDto);
            assertEquals(state.getPatient(), patient);
            verify(repository, times(1)).save(patient);
            verify(repository, times(1)).findById(0L);
        }

        @Test
        void shouldGetClinicalRecordWhenItExists() {
            ClinicalDataDto clinicalDataDto = new ClinicalDataDto("R1");
            AdmissionStateDto stateDto = new AdmissionStateDto(null, null, null, null, null, null, clinicalDataDto);
            PatientDto patientDto = new PatientDto(null, null, null, null, null, List.of(stateDto));

            when(patientMapper.toDto(patient)).thenReturn(patientDto);

            String clinicalRecord = service.getCurrentClinicalRecord(0L);

            assertEquals(clinicalDataDto.clinicalRecord(), clinicalRecord);
            verify(repository, times(1)).findById(0L);
        }

        @Test
        void shouldShowMessageWhenGettingClinicalRecordAndItDoesntExist() {
            AdmissionStateDto stateDto = new AdmissionStateDto(null);
            PatientDto patientDto = new PatientDto(null, null, null, null, null, List.of(stateDto));

            when(patientMapper.toDto(patient)).thenReturn(patientDto);
            String message = service.getCurrentClinicalRecord(0L);

            assertEquals("No clinical dat exists", message);
            verify(repository, times(1)).findById(0L);
        }

        @Test
        void shouldSetCurrentClinicalData() {
            AdmissionState state = new AdmissionState();
            patient.setAdmissionStates(List.of(state));
            ClinicalDataDto dataDto = new ClinicalDataDto("R1");
            ClinicalData data = new ClinicalData();
            data.setClinicalRecord("R1");

            when(patientMapper.toEntity(dataDto)).thenReturn(data);
            service.setCurrentClinicalData(0L, dataDto);

            assertEquals(state.getClinicalData(), data);
            assertEquals(state, data.getAdmissionState());
            verify(repository, times(1)).save(patient);
            verify(repository, times(1)).findById(0L);
        }

        @Test
        void shouldSetDepartment() {
            DepartmentDto departmentDto = new DepartmentDto(null, null, null, new HashSet<PatientSummaryDto>());
            Department department = new Department();
            department.setPatients(new HashSet<Patient>());

            when(departmentMapper.toEntity(departmentDto)).thenReturn(department);
            service.setDepartment(0L, departmentDto);

            assertEquals(department, patient.getDepartment());
            assertTrue(department.getPatients().contains(patient));
            verify(repository, times(1)).save(patient);
            verify(repository, times(1)).findById(0L);
        }
    }

    @Nested
    class whenPatientDoesNotExist {
        @Test
        void shouldThrowExceptionWhenGettingById() {
            when(repository.findById(0L)).thenReturn(Optional.empty());

            assertThrows(PatientDoesNotExistException.class, () -> service.getPatient(0L));
            verify(repository, times(1)).findById(0L);
        }

        @Test
        void shouldThrowExceptionWhenGettingByNameAndLastName() {
            when(repository.findByNameAndLastName("John", "Doe")).thenReturn(Optional.empty());

            assertThrows(PatientDoesNotExistException.class, () -> service.getPatient("John", "Doe"));
            verify(repository, times(1)).findByNameAndLastName("John", "Doe");
        }

    }

}
