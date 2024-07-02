package com.example.hospitalsystem;

import com.example.hospitalsystem.exceptions.DepartmentAlreadyExistsException;
import com.example.hospitalsystem.exceptions.DepartmentDoesNotExistException;
import com.example.hospitalsystem.exceptions.DepartmentHasPatientsException;
import com.example.hospitalsystem.exceptions.PatientDoesNotExistException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DepartmentAlreadyExistsException.class)
    public ResponseEntity<String> handleDepartmentAlreadyExistsException(DepartmentAlreadyExistsException ex){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(DepartmentDoesNotExistException.class)
    public ResponseEntity<String> handleDepartmentDoesNotExistException(DepartmentDoesNotExistException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(DepartmentHasPatientsException.class)
    public ResponseEntity<String> handleDepartmentHasPatientsException(DepartmentHasPatientsException ex){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(PatientDoesNotExistException.class)
    public ResponseEntity<String> handlePatientDoesNotExistException(PatientDoesNotExistException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
