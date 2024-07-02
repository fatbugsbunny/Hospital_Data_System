package com.example.hospitalsystem.exceptions;

public class DepartmentAlreadyExistsException extends RuntimeException {
    public DepartmentAlreadyExistsException(String message) {
        super(message);
    }
}
