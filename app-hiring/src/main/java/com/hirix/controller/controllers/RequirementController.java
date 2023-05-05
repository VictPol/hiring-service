package com.hirix.controller.controllers;

import com.hirix.domain.Employee;
import com.hirix.domain.Requirement;
import com.hirix.repository.EmployeeRepository;
import com.hirix.repository.RequirementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/requirements")
@RequiredArgsConstructor
public class RequirementController {
    private final RequirementRepository requirementRepository;

    @GetMapping
    public ResponseEntity<Object> getAllRequirements() {
        List<Requirement> requirements = requirementRepository.findAll();
        return new ResponseEntity<>(requirements, HttpStatus.OK);
    }
}
