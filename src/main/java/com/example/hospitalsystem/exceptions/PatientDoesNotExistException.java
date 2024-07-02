package com.example.hospitalsystem.exceptions;

public class PatientDoesNotExistException extends RuntimeException {
    public PatientDoesNotExistException(String message) {
        super(message);
    }
}
