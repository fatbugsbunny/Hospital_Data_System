package com.example.hospitalsystem.exceptions;

public class DepartmentDoesNotExistException extends RuntimeException {
    public DepartmentDoesNotExistException(String message) {
        super(message);
    }
}
