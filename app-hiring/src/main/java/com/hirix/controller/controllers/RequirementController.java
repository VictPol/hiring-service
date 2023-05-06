package com.hirix.controller.controllers;

import com.hirix.domain.Requirement;
import com.hirix.repository.RequirementRepository;
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
@RequestMapping("/requirements")
@RequiredArgsConstructor
public class RequirementController {
    private final RequirementRepository requirementRepository;

    @GetMapping
    public ResponseEntity<List<Requirement>> getAllRequirements() {
        List<Requirement> requirements = requirementRepository.findAll();
        return new ResponseEntity<>(requirements, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Requirement> getRequirementById(@PathVariable String id) {
        Long parsedId = Long.parseLong(id);
        Optional<Requirement> requirement = requirementRepository.findById(parsedId);
        return new ResponseEntity<>(requirement.get(), HttpStatus.OK);
    }

}
