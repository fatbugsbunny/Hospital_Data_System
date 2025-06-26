package com.example.hospitalsystem.mappers;

import com.example.hospitalsystem.Dtos.DepartmentDto;
import com.example.hospitalsystem.Dtos.DepartmentSummaryDto;
import com.example.hospitalsystem.Dtos.PatientSummaryDto;
import com.example.hospitalsystem.entities.Department;
import com.example.hospitalsystem.entities.Patient;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, uses = PatientMapper.class)
public interface DepartmentMapper {
    DepartmentDto toDto(Department department);
    Department toEntity(DepartmentDto departmentDto);

    Iterable<DepartmentDto> toDtos(Iterable<Department> departments);
    Department updateDepartmentFromDto(DepartmentSummaryDto departmentSummaryDto, @MappingTarget Department department);
}
