package com.example.hospitalsystem.repositories;

import com.example.hospitalsystem.entities.Patient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PatientRepositoryTest {

    @ServiceConnection
    @Container
    static MySQLContainer<?> mySQL = new MySQLContainer<>("mysql:8.4");

    @Autowired
    PatientRepository repository;

    @Test
    void patientIsFoundByNameAndLastName(){
        Patient patient = new Patient();
        patient.setName("John");
        patient.setLastName("Doe");
        patient.setBirthDate(LocalDate.now());
        repository.save(patient);
        Optional<Patient> savedPatient = repository.findByNameAndLastName("John","Doe");
        assertEquals("John", savedPatient.get().getName());
        assertEquals("Doe", savedPatient.get().getLastName() );
    }
}
