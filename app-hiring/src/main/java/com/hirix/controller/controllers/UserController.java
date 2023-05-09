package com.hirix.controller.controllers;


import com.hirix.controller.requests.create.UserCreateRequest;
import com.hirix.controller.requests.update.UserUpdateRequest;
import com.hirix.repository.RoleRepository;
import com.hirix.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @PostMapping
    public ResponseEntity<User> saveUser(@RequestBody UserCreateRequest request) {
//        if (result.hasErrors()) {
//            throw new IllegalRequestException(result);
//        }
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setCreated(request.getCreated());
        user.setChanged(request.getChanged());
//        user.setCreated(Timestamp.valueOf(request.getCreated()));
//        user.setChanged(Timestamp.valueOf(request.getChanged()));
//        user.setRoles(request.getRoles());
//        user.setRoles(Collections.singleton(roleRepository.findRoleByRoleName("ROLE_USER").get()));
//        Role role = new Role();
//        role.setRoleName("ROLE_MODERATOR");
//        role.setCreated(request.getCreated());
//        role.setChanged(request.getChanged());
//        Set<Role> roles = Collections.singleton(role);
//        user.setRoles(roles);
        user = userRepository.save(user);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@RequestBody UserUpdateRequest request) {
//        if (result.hasErrors()) {
//            throw new IllegalRequestException(result);
//        }
        User user = userRepository.findById(request.getId()).get();
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setChanged(request.getChanged());
//        user.setChanged(Timestamp.valueOf(request.getChanged()));
        user = userRepository.save(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

}
