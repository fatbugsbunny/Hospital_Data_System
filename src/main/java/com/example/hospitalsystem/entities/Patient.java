package com.example.hospitalsystem.entities;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Entity
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
    private List<AdmissionState> admissionStates;

    public AdmissionState getCurrentAdmissionState() {
        return admissionStates.getLast();
    }

    public void addAdmissionState(AdmissionState admissionState) {
        admissionState.setPatient(this);
        this.admissionStates.add(admissionState);
    }

    public void setCurrentClinicalData(ClinicalData clinicalData) {
        AdmissionState currentState = getCurrentAdmissionState();
        currentState.setClinicalData(clinicalData);
        clinicalData.setAdmissionState(currentState);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
        if(this.department != null){
            this.department.removePatient(this);
        }

        this.department = department;
        department.addPatient(this);
    }

    public void setAdmissionStates(List<AdmissionState> admissionState) {
        this.admissionStates = admissionState;
    }

    public List<AdmissionState> getAdmissionStates() {
        return admissionStates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Patient patient)) return false;
        return Objects.equals(id, patient.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}


