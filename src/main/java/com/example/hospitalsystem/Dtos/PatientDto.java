package com.example.hospitalsystem.Dtos;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.util.List;

public record PatientDto(Long id, String name, String lastName, @JsonFormat(pattern="yyyy-mm-dd") LocalDate birthDate, DepartmentSummaryDto department, List<AdmissionStateDto> admissionStates) {

    public AdmissionStateDto currentAdmissionState(){
        return admissionStates.getLast();
    }
}
