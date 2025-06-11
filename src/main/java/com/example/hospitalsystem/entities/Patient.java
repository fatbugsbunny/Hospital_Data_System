package com.example.hospitalsystem.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Entity
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String lastName;

    @Column(unique = true, nullable = false)
    private LocalDate birthDate;

    @ManyToOne
    @JoinColumn(name = "department_id", referencedColumnName = "id")
    private Department department;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    private List<AdmissionState> admissionState;

    @JsonIgnore
    public AdmissionState getCurrentState() {
        return admissionState.getLast();
    }

    public void addAdmissionState(AdmissionState admissionState) {
        this.admissionState.add(admissionState);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public void setAdmissionState(List<AdmissionState> admissionState) {
        this.admissionState = admissionState;
    }

    public List<AdmissionState> getAdmissionState() {
        return admissionState;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Patient patient)) return false;
        return id == patient.id && Objects.equals(name, patient.name) && Objects.equals(lastName, patient.lastName) && Objects.equals(birthDate, patient.birthDate) && Objects.equals(department, patient.department) && Objects.equals(admissionState, patient.admissionState);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, lastName, birthDate, department, admissionState);
    }
}


