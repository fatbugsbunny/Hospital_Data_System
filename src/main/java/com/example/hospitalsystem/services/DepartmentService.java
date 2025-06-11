package com.example.hospitalsystem.services;

import com.example.hospitalsystem.entities.Department;
import com.example.hospitalsystem.exceptions.DepartmentAlreadyExistsException;
import com.example.hospitalsystem.exceptions.DepartmentHasPatientsException;
import com.example.hospitalsystem.exceptions.DepartmentDoesNotExistException;
import com.example.hospitalsystem.repositories.DepartmentRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class DepartmentService {
    private final DepartmentRepository repository;

    public DepartmentService(DepartmentRepository repository) {
        this.repository = repository;
    }

    public Department addDepartment(Department department) {
        try {
            return repository.save(department);
        } catch (DataIntegrityViolationException e) {
            throw new DepartmentAlreadyExistsException("Department with that name or code already exists");
        }
    }

    public Iterable<Department> getAllDepartments() {
        return repository.findAll();
    }

    public Department getDepartment(String name) {
        return repository.findByName(name).orElseThrow(() -> new DepartmentDoesNotExistException("Department with name " + name + " does not exist"));
    }

    public Department getDepartment(Long id) {
        return repository.findById(id).orElseThrow(() -> new DepartmentDoesNotExistException("Department with id " + id + " does not exist"));
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

    public void updateDepartment(long id, Department updatedDepartment) {
        Department department = repository.findById(id).orElseThrow(() -> new DepartmentDoesNotExistException("Department with id " + id + " does not exist"));

        updatedDepartment.setId(department.getId());

        try {
            repository.save(updatedDepartment);
        } catch (DataIntegrityViolationException e) {
            throw new DepartmentAlreadyExistsException("The code or name is taken by another department");
        }
    }
}
