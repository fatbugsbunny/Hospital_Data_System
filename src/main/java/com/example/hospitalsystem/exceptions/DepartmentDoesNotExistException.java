package com.example.hospitalsystem.exceptions;

public class DepartmentNotFoundException extends RuntimeException {
    public DepartmentNotFoundException(String name) {
        super("Department with name " + name + " could not be found");
    }
}
