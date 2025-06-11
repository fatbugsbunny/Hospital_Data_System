package com.example.hospitalsystem.services;


import com.example.hospitalsystem.entities.AdmissionState;
import com.example.hospitalsystem.entities.AuditTrail;
import com.example.hospitalsystem.entities.Patient;
import com.example.hospitalsystem.repositories.AuditTrailRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AuditTrailServiceTest {
    @Mock
    AuditTrailRepository repository;

    @InjectMocks
    AuditTrailService auditTrailService;

    @Test
    @DisplayName("Patient name, last name, and birthdate is changed successfully ")
    void shouldRecordPatientDataChangeSuccessfullyWhenItsChanged() {
        Patient original = new Patient();
        Patient updated = new Patient();

        original.setName("John");
        original.setLastName("Doe");
        original.setBirthDate(LocalDate.of(2002, 3, 14));

        updated.setName("Jane");
        updated.setLastName("Smith");
        updated.setBirthDate(LocalDate.of(2004, 3, 14));

        AuditTrail nameAuditTrail = new AuditTrail();
        nameAuditTrail.setPatientId(original.getId());
        nameAuditTrail.setTableName("patient");
        nameAuditTrail.setColumnName("name");
        nameAuditTrail.setOldValue(original.getName());
        nameAuditTrail.setNewValue(updated.getName());

        AuditTrail lastNameAuditTrail = new AuditTrail();
        lastNameAuditTrail.setPatientId(original.getId());
        lastNameAuditTrail.setTableName("patient");
        lastNameAuditTrail.setColumnName("lastName");
        lastNameAuditTrail.setOldValue(original.getLastName());
        lastNameAuditTrail.setNewValue(updated.getLastName());

        AuditTrail birthDateAuditTrail = new AuditTrail();
        birthDateAuditTrail.setPatientId(original.getId());
        birthDateAuditTrail.setTableName("patient");
        birthDateAuditTrail.setColumnName("birthDate");
        birthDateAuditTrail.setOldValue(original.getBirthDate().toString());
        birthDateAuditTrail.setNewValue(updated.getBirthDate().toString());

        assertDoesNotThrow(() -> auditTrailService.createAuditTrailForPatientDataChange(original, updated));
        verify(repository, Mockito.times(3)).save(ArgumentMatchers.isA(AuditTrail.class));
        verify(repository).save(nameAuditTrail);
        verify(repository).save(lastNameAuditTrail);
        verify(repository).save(birthDateAuditTrail);
    }


    @Test
    void shouldSetOldValueToDashWhenUpdatingStateWithNoPreviousAdmissionStates(){
        List<AdmissionState> admissionStates = new ArrayList<>();
        AdmissionState state = new AdmissionState();
        state.setId(0L);

        AuditTrail auditTrail = new AuditTrail();
        auditTrail.setPatientId(0L);
        auditTrail.setTableName("admission_state");
        auditTrail.setColumnName("all");
        auditTrail.setOldValue("-");
        auditTrail.setNewValue("New admission with id: " + state.getId());

        auditTrailService.createAuditTrailForNewAdmission(0L, state, admissionStates);

        verify(repository,times(1)).save(auditTrail);
    }

    @Test
    void shouldSetOldValueToPreviousAdmissionStateWhenItExists(){
        List<AdmissionState> admissionStates = new ArrayList<>();
        AdmissionState state1 = new AdmissionState();
        state1.setId(0L);
        state1.setEnteringDate(LocalDateTime.of(2020, 3, 14, 12, 12));

        AdmissionState state2 = new AdmissionState();
        state2.setEnteringDate(LocalDateTime.now());
        state2.setId(1L);

        admissionStates.add(state1);
        admissionStates.add(state2);

        AuditTrail auditTrail = new AuditTrail();
        auditTrail.setPatientId(0L);
        auditTrail.setTableName("admission_state");
        auditTrail.setColumnName("all");
        auditTrail.setOldValue(state1.getEnteringDate().toString());
        auditTrail.setNewValue("New admission with id: " + state2.getId());

        auditTrailService.createAuditTrailForNewAdmission(0L, state2, admissionStates);

        verify(repository,times(1)).save(auditTrail);
    }

}
