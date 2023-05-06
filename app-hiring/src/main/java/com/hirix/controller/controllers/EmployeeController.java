package com.hirix.controller.controllers;

import com.hirix.controller.requests.SearchCriteriaEmployee;
import com.hirix.domain.Employee;
import com.hirix.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeRepository employeeRepository;

    @GetMapping
    public ResponseEntity<List<Employee>> getAllUsers() {
        List<Employee> employees = employeeRepository.findAll();
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable String id) {
        Long parsedId = Long.parseLong(id);
        Optional<Employee> employee = employeeRepository.findById(parsedId);
        return new ResponseEntity<>(employee.get(), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<Map<String, List<Employee>>> searchEmployeesByFullNameAndBirthdayAfter
            (@ModelAttribute SearchCriteriaEmployee criteria) {
        Timestamp birthday = Timestamp.valueOf(criteria.getBirthday());
        List<Employee> employees = employeeRepository.findEmployeesByFullNameLikeAndBirthdayAfter
            ("%" + criteria.getQuery() + "%", birthday);
        return new ResponseEntity<>(Collections.singletonMap("employees", employees), HttpStatus.OK);
    }

}
