package com.example.hospitalsystem.Dtos;

import com.example.hospitalsystem.entities.AdmissionState;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public record PatientDto(long id, String name, String lastName, @JsonFormat(pattern="yyyy-mm-dd") LocalDate birthDate, DepartmentDto department, AdmissionStateDto admissionState) {
}
