package com.example.hospitalsystem.controllers;

import com.example.hospitalsystem.entities.Department;
import com.example.hospitalsystem.services.AuditTrailService;
import com.example.hospitalsystem.services.DepartmentService;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:8100")
@RestController
@RequestMapping("/department")
public class DepartmentController {
    private final DepartmentService departmentService;
    private final AuditTrailService auditTrailService;

    public DepartmentController(DepartmentService departmentService, AuditTrailService auditTrailService) {
        this.departmentService = departmentService;
        this.auditTrailService = auditTrailService;
    }

    @PostMapping("/add")
    public void addDepartment(@RequestBody Department department, @RequestBody String doctorName) {
        auditTrailService.createAuditTrailForNewDepartment(department.getId(), doctorName );
        departmentService.addDepartment(department);
    }

    @GetMapping("/{name}")
    public Department getDepartment(@PathVariable String name) {
        return departmentService.getDepartment(name);
    }

    @GetMapping("/all")
    public Iterable<Department> getAllDepartments() {return departmentService.getAllDepartments();}

    @PutMapping("/{id}")
    public void updateDepartment(@PathVariable Long id, @RequestBody Department department) {
        departmentService.updateDepartment(id,department);
    }

    @DeleteMapping("/{id}")
    public void deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
    }
}
