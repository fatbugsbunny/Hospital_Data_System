package com.example.hospitalsystem.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false,unique = true)
    private String code;

    @Column(nullable = false,unique = true)
    private String name;

    @JsonBackReference
    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL)
    private List<Patient> patients;

    public boolean hasPatients(){
        return patients != null && !patients.isEmpty();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Patient> getPatients() {
        return patients;
    }

    public void setPatients(List<Patient> patients) {
        this.patients = patients;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof Department d)) return false;

        return d.getCode().equals(this.getCode()) && d.getName().equals(this.getName());
    }
}
