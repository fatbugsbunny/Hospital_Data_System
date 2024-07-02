package com.example.hospitalsystem;

import com.example.hospitalsystem.entities.Department;
import com.example.hospitalsystem.entities.Patient;
import com.example.hospitalsystem.exceptions.DepartmentHasPatientsException;
import com.example.hospitalsystem.repositories.DepartmentRepository;
import com.example.hospitalsystem.services.DepartmentService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class DepartmentServiceTest {
    @Mock
    private DepartmentRepository repository;

    @InjectMocks
    private DepartmentService departmentService;

    @Test
    void deleteDepartment_Null(){
        Department department = new Department();
        department.setId(0L);

        Mockito.when(repository.findById(0L)).thenReturn(Optional.of(department));

        String response = departmentService.deleteDepartment(0L);

        assertEquals("Department deleted successfully", response);
        Mockito.verify(repository, Mockito.times(1)).deleteById(0L);
        Mockito.verify(repository, Mockito.times(1)).findById(0L);
    }

    @Test
    void deleteDepartmentThrowsException_ContainsPatients(){
        Department department = new Department();
        department.setId(0L);
        List<Patient> patients = new ArrayList<>();
        patients.add(new Patient());
        department.setPatients(patients);

        Mockito.when(repository.findById(0L)).thenReturn(Optional.of(department));

        assertThrows(DepartmentHasPatientsException.class,() -> departmentService.deleteDepartment(0L));
    }

    @Test
    void updateDepartment_ChangeValues(){
        Department originalDepartment = new Department();
        originalDepartment.setCode("AAA");
        originalDepartment.setName("Depression");

        Department updatedDepartment = new Department();
        updatedDepartment.setCode("BBB");
        updatedDepartment.setName("Psychiatric unit");


        Mockito.when((repository.findById(0L))).thenReturn(Optional.of(originalDepartment));

        departmentService.updateDepartment(0L, updatedDepartment);

        assertEquals(originalDepartment,updatedDepartment);
        Mockito.verify(repository, Mockito.times(1)).findById(0L);
        Mockito.verify(repository, Mockito.times(1)).save(originalDepartment);
    }
}
