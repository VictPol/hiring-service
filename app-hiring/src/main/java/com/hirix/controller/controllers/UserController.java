package com.hirix.controller.controllers;


import com.hirix.repository.RoleRepository;
import com.hirix.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;


import com.hirix.domain.User;
import com.hirix.domain.Role;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userRepository.findById(id);
        return new ResponseEntity<>(user.get(), HttpStatus.OK);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return new ResponseEntity<>(user.get(), HttpStatus.OK);
    }

    @GetMapping("/role/{role_name}")
    public ResponseEntity<Map<String, Set<User>>> getUsersByRoleName(@PathVariable ("role_name") String roleName) {
        Optional<Role> role = roleRepository.findRoleByRoleName(roleName);
        Set<User> users = role.get().getUsers();
        return new ResponseEntity<>(Collections.singletonMap(roleName, users), HttpStatus.OK);
    }

}