package com.example.hospitalsystem.Dtos;

import java.util.List;

public record DepartmentDto(long id, String code, String name, List<PatientDto> patients) {
}
