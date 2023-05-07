package com.hirix.controller.controllers;

import com.hirix.controller.requests.RoleCreateRequest;
import com.hirix.domain.Role;
import com.hirix.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.sql.Timestamp;
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

    @PostMapping
    public ResponseEntity<Role> saveRole(@RequestBody RoleCreateRequest request) {
//        if (result.hasErrors()) {
//            throw new IllegalRequestException(result);
//        }
        Role role = new Role();
        role.setRoleName(request.getRoleName());
        role.setCreated(Timestamp.valueOf(request.getCreated()));
        role.setChanged(Timestamp.valueOf(request.getChanged()));
        role = roleRepository.save(role);
        return new ResponseEntity<>(role, HttpStatus.CREATED);
    }

}
