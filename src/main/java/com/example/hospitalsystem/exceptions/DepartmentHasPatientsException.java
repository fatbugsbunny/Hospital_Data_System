package com.example.hospitalsystem.exceptions;

public class DepartmentHasPatientsException extends RuntimeException{
    public DepartmentHasPatientsException(String message) {
        super(message);
    }
}
