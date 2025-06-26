package com.example.hospitalsystem.controllers;

import com.example.hospitalsystem.Dtos.DepartmentDto;
import com.example.hospitalsystem.Dtos.DepartmentSummaryDto;
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

    @PostMapping( "/add")
    public void addDepartment(@RequestBody DepartmentDto department) {
        DepartmentDto savedDepartment = departmentService.addDepartment(department);
        auditTrailService.createAuditTrailForNewDepartment(savedDepartment.id());
    }

    @GetMapping("/{name}")
    public DepartmentDto getDepartment(@PathVariable String name) {
        return departmentService.getDepartment(name);
    }

    @GetMapping("/all")
    public Iterable<DepartmentDto> getAllDepartments() {return departmentService.getAllDepartments();}

    @PutMapping("/{id}")
    public void updateDepartment(@PathVariable Long id, @RequestBody DepartmentSummaryDto department) {
        departmentService.updateDepartment(id,department);
    }

    @DeleteMapping("/{id}")
    public void deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
    }
}
