package com.example.hospitalsystem.mappers;

import com.example.hospitalsystem.Dtos.PatientDto;
import com.example.hospitalsystem.entities.Patient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import com.example.hospitalsystem.Dtos.PatientDto;

@Mapper
public interface PatientMapper {

    PatientMapper instance = Mappers.getMapper(PatientMapper.class);

    PatientDto patientToPatientDto(Patient patient);
}
