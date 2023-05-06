package com.hirix.controller.controllers;

import com.hirix.domain.Employee;
import com.hirix.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeRepository employeeRepository;

    @GetMapping
    public ResponseEntity<Object> getAllUsers() {
        List<Employee> employees = employeeRepository.findAll();
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getEmployeeById(@PathVariable String id) {
        Long parsedId = Long.parseLong(id);
        Optional<Employee> employee = employeeRepository.findById(parsedId);
        return new ResponseEntity<>(employee.get(), HttpStatus.OK);
    }
}
