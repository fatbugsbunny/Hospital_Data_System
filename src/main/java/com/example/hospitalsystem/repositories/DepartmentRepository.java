package com.example.hospitalsystem.repositories;

import com.example.hospitalsystem.entities.Department;
import com.fasterxml.jackson.annotation.OptBoolean;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepartmentRepository extends CrudRepository<Department, Long> {
    Optional<Department> findByName(String name);
}
