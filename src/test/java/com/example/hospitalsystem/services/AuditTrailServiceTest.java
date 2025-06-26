package com.example.hospitalsystem.services;


import com.example.hospitalsystem.Dtos.AdmissionStateDto;
import com.example.hospitalsystem.Dtos.PatientDto;
import com.example.hospitalsystem.Dtos.PatientSummaryDto;
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
        PatientDto original = new PatientDto(0L,"John","Doe",LocalDate.of(2002, 3, 14),null, null);

        PatientSummaryDto updated = new PatientSummaryDto("Jane","Smith",LocalDate.of(2004, 3, 14));

        AuditTrail nameAuditTrail = new AuditTrail();
        nameAuditTrail.setPatientId(original.id());
        nameAuditTrail.setTableName("patient");
        nameAuditTrail.setColumnName("name");
        nameAuditTrail.setOldValue(original.name());
        nameAuditTrail.setNewValue(updated.name());

        AuditTrail lastNameAuditTrail = new AuditTrail();
        lastNameAuditTrail.setPatientId(original.id());
        lastNameAuditTrail.setTableName("patient");
        lastNameAuditTrail.setColumnName("last_name");
        lastNameAuditTrail.setOldValue(original.lastName());
        lastNameAuditTrail.setNewValue(updated.lastName());

        AuditTrail birthDateAuditTrail = new AuditTrail();
        birthDateAuditTrail.setPatientId(original.id());
        birthDateAuditTrail.setTableName("patient");
        birthDateAuditTrail.setColumnName("birthDate");
        birthDateAuditTrail.setOldValue(original.birthDate().toString());
        birthDateAuditTrail.setNewValue(updated.birthDate().toString());

        assertDoesNotThrow(() -> auditTrailService.createAuditTrailForPatientDataChange(original, updated));
        verify(repository, Mockito.times(3)).save(ArgumentMatchers.isA(AuditTrail.class));
        verify(repository).save(nameAuditTrail);
        verify(repository).save(lastNameAuditTrail);
        verify(repository).save(birthDateAuditTrail);
    }

    @Test
    void shouldSetOldValueToDashWhenUpdatingStateWithNoPreviousAdmissionStates(){
        List<AdmissionStateDto> admissionStates = new ArrayList<>();
        AdmissionStateDto state = new AdmissionStateDto(0L);

        AuditTrail auditTrail = new AuditTrail();
        auditTrail.setPatientId(0L);
        auditTrail.setTableName("admission_state");
        auditTrail.setColumnName("all");
        auditTrail.setOldValue("-");
        auditTrail.setNewValue("New admission with id: " + state.id());

        auditTrailService.createAuditTrailForNewAdmission(0L, state, admissionStates);

        verify(repository,times(1)).save(auditTrail);
    }

    @Test
    void shouldSetOldValueToPreviousAdmissionStateWhenItExists(){
        List<AdmissionStateDto> admissionStates = new ArrayList<>();
        AdmissionStateDto state1 = new AdmissionStateDto(0L,LocalDateTime.now(),null,null,null,null,null);

        AdmissionStateDto state2 = new AdmissionStateDto(1L);

        admissionStates.add(state1);
        admissionStates.add(state2);

        AuditTrail auditTrail = new AuditTrail();
        auditTrail.setPatientId(0L);
        auditTrail.setTableName("admission_state");
        auditTrail.setColumnName("all");
        auditTrail.setOldValue(state1.enteringDate().toString());
        auditTrail.setNewValue("New admission with id: " + state2.id());

        auditTrailService.createAuditTrailForNewAdmission(0L, state2, admissionStates);

        verify(repository,times(1)).save(auditTrail);
    }

}
