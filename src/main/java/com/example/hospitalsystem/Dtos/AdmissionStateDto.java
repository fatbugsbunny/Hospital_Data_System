package com.example.hospitalsystem.Dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public record AdmissionStateDto(Long id, @JsonFormat(pattern="yyyy-mm-dd HH:mm:ss") LocalDateTime enteringDate, @JsonFormat(pattern="yyyy-mm-dd HH:mm:ss") LocalDateTime exitingDate, String cause, String reason, Boolean discharge, ClinicalDataDto clinicalData) {

    public AdmissionStateDto(Long id) {
        this(id, null, null, "", "", null, null);
    }
}
