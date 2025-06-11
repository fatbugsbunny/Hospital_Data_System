package com.example.hospitalsystem.repositories;

import com.example.hospitalsystem.entities.Department;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)

class DepartmentRepositoryTest {
    @Autowired
    private DepartmentRepository repository;

    @Container
    @ServiceConnection
    static MySQLContainer<?> mySQL = new MySQLContainer<>("mysql:8.4");


    @Test
    void departmentIsFoundByName(){
        Department department = new Department();
        department.setName("D1");
        department.setCode("1");

        repository.save(department);

        Optional<Department> savedDepartment = repository.findByName("D1");

        assertEquals("D1",savedDepartment.get().getCode());
        assertEquals("1",savedDepartment.get().getName());
    }
}
