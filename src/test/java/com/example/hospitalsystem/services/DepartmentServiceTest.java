package com.example.hospitalsystem.services;

import com.example.hospitalsystem.Dtos.DepartmentDto;
import com.example.hospitalsystem.Dtos.DepartmentSummaryDto;
import com.example.hospitalsystem.entities.Department;
import com.example.hospitalsystem.entities.Patient;
import com.example.hospitalsystem.exceptions.DepartmentAlreadyExistsException;
import com.example.hospitalsystem.exceptions.DepartmentHasPatientsException;
import com.example.hospitalsystem.mappers.DepartmentMapper;
import com.example.hospitalsystem.repositories.DepartmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;


import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DepartmentServiceTest {

    @Mock
    private DepartmentRepository repository;
    @Mock
    private DepartmentMapper mapper;

    @InjectMocks
    private DepartmentService service;

    private DepartmentDto d1Dto;
    private Department d1;

    @BeforeEach
    void setUp() {
        d1Dto = new DepartmentDto(0L, "1", "D1", null);
        d1 = new Department();
        d1.setId(0L);
        d1.setName("D1");
        d1.setCode("1");
    }

    @Test
    void shouldAddDepartmentWhenNoSameDepartmentExists() {
        when(mapper.toDto(d1)).thenReturn(d1Dto);
        when(mapper.toEntity(d1Dto)).thenReturn(d1);
        when(repository.save(d1)).thenReturn(d1);

        DepartmentDto savedDepartment = service.addDepartment(d1Dto);

        assertEquals(d1Dto, savedDepartment);
        Mockito.verify(repository, Mockito.times(1)).save(d1);
    }

    @Test
    void shouldNotAddDepartmentWhenSameDepartmentExists() {
        when(mapper.toEntity(d1Dto)).thenReturn(d1);
        when(repository.save(d1)).thenThrow(DataIntegrityViolationException.class);

        assertThrows(DepartmentAlreadyExistsException.class, () -> service.addDepartment(d1Dto));
        Mockito.verify(repository, Mockito.times(1)).save(d1);
    }

    @Test
    void shouldDeleteDepartmentWhenItHasNoPatients() {
        when(repository.findById(0L)).thenReturn(Optional.of(d1));

        String response = service.deleteDepartment(0L);

        assertEquals("Department deleted successfully", response);
        Mockito.verify(repository, Mockito.times(1)).findById(0L);
        Mockito.verify(repository, Mockito.times(1)).deleteById(0L);
    }

    @Test
    void shouldThrowExceptionWhenDeletingDepartmentWithPatients() {
        Set<Patient> patients = new HashSet<>();
        patients.add(new Patient());
        d1.setPatients(patients);

        when(repository.findById(0L)).thenReturn(Optional.of(d1));

        assertThrows(DepartmentHasPatientsException.class, () -> service.deleteDepartment(0L));
        Mockito.verify(repository, Mockito.times(1)).findById(0L);
        Mockito.verify(repository, Mockito.never()).deleteById(0L);
    }

    @Test
    void shouldUpdateDepartmentWhenNewValuesArentTaken() {
        DepartmentSummaryDto updatedDepartmentDto = new DepartmentSummaryDto(null, "2", "D2");
        Department updatedDepartment = new Department();
        updatedDepartment.setId(0L);
        updatedDepartment.setName("D2");
        updatedDepartment.setCode("2");

        when((repository.findById(0L))).thenReturn(Optional.of(d1));
        when(mapper.updateDepartmentFromDto(updatedDepartmentDto,d1)).thenReturn(updatedDepartment);

        service.updateDepartment(0L, updatedDepartmentDto);

        Mockito.verify(repository, Mockito.times(1)).findById(0L);
        Mockito.verify(repository, Mockito.times(1)).save(updatedDepartment);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingDepartmentWithTakenValues() {
        DepartmentSummaryDto updatedDepartmentDto = new DepartmentSummaryDto(null,"2", "Psychiatric Unit");
        Department updatedDepartment = new Department();
        updatedDepartment.setId(0L);
        updatedDepartment.setName("Psychiatric Unit");
        updatedDepartment.setCode("2");

        when((repository.findById(0L))).thenReturn(Optional.of(d1));
        when(mapper.updateDepartmentFromDto(updatedDepartmentDto,d1)).thenReturn(updatedDepartment);
        when(repository.save(updatedDepartment)).thenThrow(DataIntegrityViolationException.class);

        assertThrows(DepartmentAlreadyExistsException.class, () -> service.updateDepartment(0L, updatedDepartmentDto));
        Mockito.verify(repository, Mockito.times(1)).findById(0L);
        Mockito.verify(repository, Mockito.times(1)).save(updatedDepartment);
    }
}
