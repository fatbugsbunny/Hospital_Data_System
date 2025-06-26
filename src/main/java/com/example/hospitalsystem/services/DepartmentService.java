package com.example.hospitalsystem.services;

import com.example.hospitalsystem.Dtos.DepartmentDto;
import com.example.hospitalsystem.Dtos.DepartmentSummaryDto;
import com.example.hospitalsystem.entities.Department;
import com.example.hospitalsystem.exceptions.DepartmentAlreadyExistsException;
import com.example.hospitalsystem.exceptions.DepartmentHasPatientsException;
import com.example.hospitalsystem.exceptions.DepartmentDoesNotExistException;
import com.example.hospitalsystem.mappers.DepartmentMapper;
import com.example.hospitalsystem.repositories.DepartmentRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class DepartmentService {
    private final DepartmentRepository repository;
    private final DepartmentMapper mapper;

    public DepartmentService(DepartmentRepository repository, DepartmentMapper mapper) {
        this.mapper = mapper;
        this.repository = repository;
    }

    public DepartmentDto addDepartment(DepartmentDto department) {
        try {
            return mapper.toDto(repository.save(mapper.toEntity(department)));
        } catch (DataIntegrityViolationException e) {
            throw new DepartmentAlreadyExistsException("Department with that name or code already exists");
        }
    }

    public Iterable<DepartmentDto> getAllDepartments() {
        return mapper.toDtos(repository.findAll());
    }

    public DepartmentDto getDepartment(String name) {
        return mapper.toDto(repository.findByName(name).orElseThrow(() -> new DepartmentDoesNotExistException("Department with name " + name + " does not exist")));
    }

    public DepartmentDto getDepartment(Long id) {
        return mapper.toDto(repository.findById(id).orElseThrow(() -> new DepartmentDoesNotExistException("Department with id " + id + " does not exist")));
    }

    public String deleteDepartment(long id) {
        Department department = repository.findById(id).orElseThrow(() -> new DepartmentDoesNotExistException("Department with id " + id + " does not exist"));
        if (department.hasPatients()) {
            throw new DepartmentHasPatientsException("Department with id " + id + " contains patients and cannot be deleted");
        } else {
            repository.deleteById(id);
            return "Department deleted successfully";
        }
    }

    public void updateDepartment(long id, DepartmentSummaryDto updatedDepartment) {
        Department department = repository.findById(id).orElseThrow(() -> new DepartmentDoesNotExistException("Department with id " + id + " does not exist"));
        try {
            repository.save(mapper.updateDepartmentFromDto(updatedDepartment, department));
        } catch (DataIntegrityViolationException e) {
            throw new DepartmentAlreadyExistsException("The code or name is taken by another department");
        }
    }
}
