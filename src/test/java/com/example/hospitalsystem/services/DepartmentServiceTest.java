package com.example.hospitalsystem.services;

import com.example.hospitalsystem.entities.Department;
import com.example.hospitalsystem.entities.Patient;
import com.example.hospitalsystem.exceptions.DepartmentAlreadyExistsException;
import com.example.hospitalsystem.exceptions.DepartmentHasPatientsException;
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


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DepartmentServiceTest {

    @Mock
    private DepartmentRepository repository;

    @InjectMocks
    private DepartmentService service;

    private Department d1;

    @BeforeEach
    void setUp(){
        d1 = new Department();
        d1.setId(0L);
        d1.setName("D1");
        d1.setCode("1");
    }

    @Test
    void shouldAddDepartmentWhenNoSameDepartmentExists() {
        assertDoesNotThrow(() -> service.addDepartment(d1));

        Mockito.verify(repository, Mockito.times(1)).save(d1);
    }

    @Test
    void shouldNotAddDepartmentWhenSameDepartmentExists() {
        Department d2 = new Department();
        d2.setId(1L);
        d2.setName("D1");
        d2.setCode("1");

        service.addDepartment(d1);

        Mockito.when(repository.save(d2)).thenThrow(DataIntegrityViolationException.class);

        assertThrows(DepartmentAlreadyExistsException.class, ()-> service.addDepartment(d2));
        Mockito.verify(repository, Mockito.times(2)).save(ArgumentMatchers.isA(Department.class));
        Mockito.verify(repository, Mockito.times(1)).save(d1);
    }

    @Test
    void shouldDeleteDepartmentWhenItHasNoPatients(){
        Mockito.when(repository.findById(0L)).thenReturn(Optional.of(d1));

        String response = service.deleteDepartment(0L);

        assertEquals("Department deleted successfully", response);
        Mockito.verify(repository, Mockito.times(1)).findById(0L);
        Mockito.verify(repository, Mockito.times(1)).deleteById(0L);
    }

    @Test
    void shouldThrowExceptionWhenDeletingDepartmentWithPatients(){
        List<Patient> patients = new ArrayList<>();
        patients.add(new Patient());
        d1.setPatients(patients);

        Mockito.when(repository.findById(0L)).thenReturn(Optional.of(d1));

        assertThrows(DepartmentHasPatientsException.class,() -> service.deleteDepartment(0L));
        Mockito.verify(repository, Mockito.times(1)).findById(0L);
        Mockito.verify(repository, Mockito.never()).deleteById(0L);
    }

    @Test
    void shouldUpdateDepartmentWhenNewValuesArentTaken(){
        Department updatedDepartment = new Department();
        updatedDepartment.setCode("2");
        updatedDepartment.setName("D2");

        Mockito.when((repository.findById(0L))).thenReturn(Optional.of(d1));

        service.addDepartment(d1);
        service.updateDepartment(0L, updatedDepartment);

        assertEquals(d1.getId(),updatedDepartment.getId());
        Mockito.verify(repository, Mockito.times(1)).findById(0L);
        Mockito.verify(repository, Mockito.times(1)).save(d1);
        Mockito.verify(repository, Mockito.times(1)).save(updatedDepartment);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingDepartmentWithTakenValues(){
        Department d2 = new Department();
        d2.setId(1L);
        d2.setCode("2");
        d2.setName("D2");

        Department updatedDepartment = new Department();
        updatedDepartment.setId(d1.getId());
        updatedDepartment.setCode("2");
        updatedDepartment.setName("Psychiatric unit");

        service.addDepartment(d1);
        service.addDepartment(d2);

        Mockito.when((repository.findById(0L))).thenReturn(Optional.of(d1));
        Mockito.when(repository.save(updatedDepartment)).thenThrow(DataIntegrityViolationException.class);

        assertThrows(DepartmentAlreadyExistsException.class, () -> service.updateDepartment(0L, updatedDepartment));

        assertNotEquals(d1,updatedDepartment);
        Mockito.verify(repository, Mockito.times(1)).findById(0L);
        Mockito.verify(repository, Mockito.times(1)).save(d1);
        Mockito.verify(repository, Mockito.times(1)).save(d2);
    }
}
