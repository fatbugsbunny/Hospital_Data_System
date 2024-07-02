package com.example.hospitalsystem.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

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

    @JsonManagedReference
    @ManyToOne
    @JoinColumn(name = "department_id", referencedColumnName = "id")
    private Department department;


    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    private List<AdmissionState> admissionState;

    @JsonIgnore
    public AdmissionState getCurrentState() {
        return admissionState.get(admissionState.size() - 1);
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

    public List<AdmissionState> getAdmissionStates() {
        return admissionState;
    }

    @Override
    public boolean equals(Object o){
        if (o == this) {
            return true;
        }

        if (!(o instanceof Patient p)) {
            return false;
        }

        return p.getDepartment() == this.getDepartment() && p.getLastName().equals(this.getLastName()) && p.getBirthDate().equals(this.getBirthDate()) && p.getName().equals(this.getName());
    }
}
