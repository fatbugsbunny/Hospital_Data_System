package com.example.hospitalsystem.mappers;

import com.example.hospitalsystem.Dtos.AdmissionStateDto;
import com.example.hospitalsystem.Dtos.ClinicalDataDto;
import com.example.hospitalsystem.Dtos.PatientDto;
import com.example.hospitalsystem.Dtos.PatientSummaryDto;
import com.example.hospitalsystem.entities.AdmissionState;
import com.example.hospitalsystem.entities.ClinicalData;
import com.example.hospitalsystem.entities.Patient;
import org.mapstruct.*;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, uses = DepartmentMapper.class
)
public interface PatientMapper {

    PatientDto toDto(Patient patient);
    Patient toEntity(PatientDto patientDto);

    PatientSummaryDto toPatientSummaryDto(Patient patient);
    Patient fromPatientSummaryToEntity(PatientSummaryDto patientSummaryDto);

    AdmissionStateDto toDto(AdmissionState admissionState);
    AdmissionState toEntity(AdmissionStateDto admissionStateDto);

    ClinicalDataDto toDto(ClinicalData clinicalData);
    ClinicalData toEntity(ClinicalDataDto clinicalDataDto);


    Iterable<PatientDto> toDtos(Iterable<Patient> patients);
    Patient updatePatientFromDto(PatientSummaryDto patientDto, @MappingTarget Patient patient);
    void updateDtoFromUser(Patient patient, @MappingTarget PatientDto patientDto);
}
