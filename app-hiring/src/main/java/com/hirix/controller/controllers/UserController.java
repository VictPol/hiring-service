package com.hirix.controller.controllers;


import com.hirix.controller.requests.create.UserCreateRequest;
import com.hirix.controller.requests.update.UserUpdateRequest;
import com.hirix.domain.LinkUsersRoles;
import com.hirix.repository.LinkUsersRolesRepository;
import com.hirix.repository.RoleRepository;
import com.hirix.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Instanceof;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
import java.util.Set;


import com.hirix.domain.User;
import com.hirix.domain.Role;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final LinkUsersRolesRepository linkUsersRolesRepository;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        User user = optionalUser.get();
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        User user = optionalUser.get();
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/role/{ROLE_NAME}")
    public ResponseEntity<Map<String, Set<User>>> getUsersByRoleName(@PathVariable ("ROLE_NAME") String roleName) {
        Optional<Role> optionalRole = roleRepository.findRoleByRoleName(roleName.toUpperCase());
        Role role = optionalRole.get();
        Set<User> users = role.getUsers();
        return new ResponseEntity<>(Collections.singletonMap(roleName, users), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody UserCreateRequest request) {
//        if (result.hasErrors()) {
//            throw new IllegalRequestException(result);
//        }
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setPassword(request.getNickName());
        user.setCreated(Timestamp.valueOf(LocalDateTime.now()));
        user.setChanged(Timestamp.valueOf(LocalDateTime.now()));
        user = userRepository.save(user);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@RequestBody UserUpdateRequest request) {
//        if (result.hasErrors()) {
//            throw new IllegalRequestException(result);
//        }
        Optional<User> optionalUser = userRepository.findById(request.getId());
        User user = optionalUser.get();
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setNickName(request.getNickName());
        user.setChanged(Timestamp.valueOf(LocalDateTime.now()));
        user = userRepository.save(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PatchMapping("/update_nick_name/{id}/{nick_name}")
    public ResponseEntity<User> updateUserNickName(@PathVariable Long id, @PathVariable(name = "nick_name") String nickName) {
//        if (result.hasErrors()) {
//            throw new IllegalRequestException(result);
//        }
        Optional<User> optionalUser = userRepository.findById(id);
        User user = optionalUser.get();
        user.setNickName(nickName);
        user.setChanged(Timestamp.valueOf(LocalDateTime.now()));
        user = userRepository.save(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable Long id) {
//        if (result.hasErrors()) {
//            throw new IllegalRequestException(result);
//        }
        Optional<User> optionalUser = userRepository.findById(id);
        User user = optionalUser.get();
        userRepository.delete(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/add_role/{user_id}/{role_id}")
    public ResponseEntity<LinkUsersRoles> addRoleToUser(@PathVariable Long user_id, @PathVariable Long role_id) {
//        if (result.hasErrors()) {
//            throw new IllegalRequestException(result);
//        }
        LinkUsersRoles link = new LinkUsersRoles();
        link.setUserId(user_id);
        link.setRoleId(role_id);
        link.setCreated(Timestamp.valueOf(LocalDateTime.now()));
        link.setChanged(Timestamp.valueOf(LocalDateTime.now()));
        link = linkUsersRolesRepository.save(link);
        return new ResponseEntity<>(link, HttpStatus.CREATED);
    }

}
