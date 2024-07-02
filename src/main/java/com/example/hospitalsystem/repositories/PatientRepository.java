package com.example.hospitalsystem.repositories;

import com.example.hospitalsystem.entities.Patient;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientRepository extends CrudRepository<Patient, Long> {
    Optional<Patient> findByNameAndLastName(String name, String lastName);
}
