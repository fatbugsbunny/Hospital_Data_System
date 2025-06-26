package com.example.hospitalsystem.Dtos;

import java.util.Set;

public record DepartmentDto(Long id, String code, String name, Set<PatientSummaryDto> patients) {
}
