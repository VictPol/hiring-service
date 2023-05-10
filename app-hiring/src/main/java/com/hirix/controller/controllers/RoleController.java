package com.hirix.controller.controllers;

import com.hirix.controller.requests.create.RoleCreateRequest;
import com.hirix.controller.requests.search.EmployeeSearchCriteria;
import com.hirix.controller.requests.search.RoleSearchCriteria;
import com.hirix.controller.requests.update.RoleUpdateRequest;
import com.hirix.domain.Employee;
import com.hirix.domain.Role;
import com.hirix.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
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
    public ResponseEntity<Role> getRoleById(@PathVariable Long id) {
//        Long parsedId = Long.parseLong(id);
        Optional<Role> optionalRole = roleRepository.findById(id);
        Role role = optionalRole.get();
        return new ResponseEntity<>(role, HttpStatus.OK);
    }

    @GetMapping("/role/{ROLE_NAME}")
    public ResponseEntity<Role> getRoleByRoleName(@PathVariable("ROLE_NAME") String roleName) {
        Optional<Role> optionalRole = roleRepository.findRoleByRoleName(roleName);
        Role role = optionalRole.get();
        return new ResponseEntity<>(role, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<Map<String, List<Role>>> searchRolesByRoleNameLike
            (@ModelAttribute RoleSearchCriteria criteria) {
        List<Role> roles = roleRepository.findRolesByRoleNameLike("%" + criteria.getQuery().toUpperCase() + "%");
        return new ResponseEntity<>(Collections.singletonMap("roles", roles), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Role> createRole(@RequestBody RoleCreateRequest request) {
//        if (result.hasErrors()) {
//            throw new IllegalRequestException(result);
//        }
        Role role = new Role();
        role.setRoleName(request.getRoleName());
        role.setCreated(Timestamp.valueOf(LocalDateTime.now()));
        role.setChanged(Timestamp.valueOf(LocalDateTime.now()));
        role = roleRepository.save(role);
        return new ResponseEntity<>(role, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Role> updateRole(@RequestBody RoleUpdateRequest request) {
//        if (result.hasErrors()) {
//            throw new IllegalRequestException(result);
//        }
        Optional<Role> optionalRole = roleRepository.findById(request.getId());
        Role role = optionalRole.get();
        role.setRoleName(request.getRoleName());
        role.setChanged(Timestamp.valueOf(LocalDateTime.now()));
        role = roleRepository.save(role);
        return new ResponseEntity<>(role, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Role> deleteRole(@PathVariable Long id) {
//        if (result.hasErrors()) {
//            throw new IllegalRequestException(result);
//        }
        Optional<Role> optionalRole = roleRepository.findById(id);
        Role role = optionalRole.get();

        roleRepository.delete(role);
        return new ResponseEntity<>(role, HttpStatus.OK);
    }

}
