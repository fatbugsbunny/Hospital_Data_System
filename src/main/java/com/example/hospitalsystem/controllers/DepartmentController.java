package com.example.hospitalsystem;

import com.example.hospitalsystem.entities.Department;
import com.example.hospitalsystem.services.DepartmentService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/department")
public class DepartmentController {
    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;

    }

    @PostMapping("/add")
    public void addDepartment(@RequestBody Department department) {
        departmentService.addDepartment(department);
    }

    @GetMapping("/{name}")
    public Department getDepartment(@PathVariable String name) {
        return departmentService.getDepartment(name);
    }

    @PutMapping("/{id}")
    public void updateDepartment(@PathVariable Long id, @RequestBody Department department) {
        departmentService.editDepartment(id,department);
    }

    @DeleteMapping("/{id}")
    public void deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
    }




}
