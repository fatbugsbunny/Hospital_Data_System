package com.example.hospitalsystem.exceptions;

public class PatientIsAlreadyAdmittedException extends RuntimeException {
    public PatientIsAlreadyAdmittedException() {
        super("Patient is already admitted");
    }
}
