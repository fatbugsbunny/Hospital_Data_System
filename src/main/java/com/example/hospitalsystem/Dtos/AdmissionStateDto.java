package com.example.hospitalsystem.Dtos;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record AdmissionStateDto(long id, @JsonFormat(pattern="yyyy-mm-dd HH:mm:ss") LocalDateTime enteringDate, @JsonFormat(pattern="yyyy-mm-dd HH:mm:ss") LocalDateTime exitingDate, String cause, String reason, Boolean discharge, ClinicalDataDto clinicalData, PatientDto patient) {
}
