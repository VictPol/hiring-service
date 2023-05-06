package com.hirix.controller.controllers;

import com.hirix.domain.Role;
import com.hirix.repository.RoleRepository;
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
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleController {
    private final RoleRepository roleRepository;

    @GetMapping
    public ResponseEntity<List<Role>> getAllRoles() {
        List<Role> roles = roleRepository.findAll();
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Role> getRoleById(@PathVariable String id) {
        Long parsedId = Long.parseLong(id);
        Optional<Role> role = roleRepository.findById(parsedId);
        return new ResponseEntity<>(role.get(), HttpStatus.OK);
    }

    @GetMapping("/role/{role_name}")
    public ResponseEntity<Role> getRoleByRoleName(@PathVariable("role_name") String roleName) {
        Optional<Role> role = roleRepository.findRoleByRoleName(roleName.toUpperCase().trim());
        return new ResponseEntity<>(role.get(), HttpStatus.OK);
    }

}
