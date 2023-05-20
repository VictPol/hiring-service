package com.hirix.controller.controllers;


import com.hirix.controller.requests.create.EmployeeCreateRequest;
import com.hirix.controller.requests.create.UserCreateRequest;
import com.hirix.controller.requests.patch.EmployeePatchRequest;
import com.hirix.controller.requests.patch.UserPatchRequest;
import com.hirix.controller.requests.update.EmployeeUpdateRequest;
import com.hirix.controller.requests.update.UserUpdateRequest;
import com.hirix.domain.Employee;
import com.hirix.domain.LinkUsersRoles;
import com.hirix.exception.ConvertRequestToEntityException;
import com.hirix.exception.EntityNotCreatedOrNotUpdatedException;
import com.hirix.exception.EntityNotDeletedException;
import com.hirix.exception.EntityNotFoundException;
import com.hirix.exception.IllegalRequestException;
import com.hirix.exception.PoorInfoInRequestToCreateUpdateEntity;
import com.hirix.repository.LinkUsersRolesRepository;
import com.hirix.repository.RoleRepository;
import com.hirix.repository.UserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
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
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;


import com.hirix.domain.User;
import com.hirix.domain.Role;

import javax.validation.Valid;

@RestController
@RequestMapping("rest/users")
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final LinkUsersRolesRepository linkUsersRolesRepository;
    private final ConversionService conversionService;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users;
        try {
            users = userRepository.findAll();
        } catch (Exception e) {
            throw new EntityNotFoundException("Can not get users from required resource \'rest/users\', " +
                    e.getCause());
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/page_one_user/{page}")
    public ResponseEntity<Map<String, Page<User>>> findAllShowPageWithOneUser(@PathVariable String page) {
        Integer parsedPage;
        try {
            parsedPage = Integer.parseInt(page);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Bad {page} in resource path \'/rest/users/page_one_user/{page}\'. " +
                    "Must be Integer type");
        }
        if (parsedPage < 0) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Bad {page} in resource path \'/rest/users/page_one_user/{page}\'. " +
                    "Id must be not less than 0L");
        }
        Page<User> users;
        try {
            users = userRepository.findAll(PageRequest.of(parsedPage, 1, Sort.by("nickName").ascending()));
        } catch (Exception e) {
            throw new EntityNotFoundException
                    ("Can not get users from required resource \'/rest/users/page_one_user/{page}\', " + e.getCause());
        }
        return new ResponseEntity<>(Collections.singletonMap("page #" + parsedPage, users), HttpStatus.OK);
    }

    @GetMapping("/page_size_users/{page}/{size}")
    public ResponseEntity<Map<String, Page<User>>> findAllShowPageBySize(@PathVariable String page, @PathVariable String size) {
        Integer parsedPage;
        try {
            parsedPage = Integer.parseInt(page);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Bad {page} in resource path \'/rest/users/page_size_employees/{page}/{size}\'. " +
                    "Must be Integer type");
        }
        if (parsedPage < 0) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Bad {page} in resource path \'/rest/users/page_size_employees/{page}/{size}\'. " +
                    "Id must be not less than 0L");
        }
        Integer parsedSize;
        try {
            parsedSize = Integer.parseInt(size);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Bad {size} in resource path \'/rest/users/page_size_users/{page}/{size}\'. " +
                    "Must be Integer type");
        }
        if (parsedSize < 1) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Bad {size} in resource path \'/rest/users/page_size_users/{page}/{size}\', " +
                    "Id must be more than 0L");
        }
        Page<User> users;
        try {
            users = userRepository.findAll(PageRequest.of(parsedPage, parsedSize, Sort.by("nickName").ascending()));
        } catch (Exception e) {
            throw new EntityNotFoundException
                    ("Can not get users from required resource \'/rest/users/page_size_users/{page}/{size}\', " + e.getCause());
        }
        return new ResponseEntity<>(Collections.singletonMap("page #" + parsedPage, users), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable String id) {
        Long parsedId;
        try {
            parsedId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Bad user {id} in resource path \'/rest/users/{id}\'. Must be Long type");
        }
        if (parsedId < 1L) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Bad employee {id} in resource path \'/rest/users/{id}\'. " +
                    "Id must be more than 0L");
        }
        Optional<User> optionalUser;
        try {
            optionalUser = userRepository.findById(parsedId);
        }  catch (Exception e) {
            throw new EntityNotFoundException
                    ("Can not get user by id from from required resource \'/rest/users/{id}\', " + e.getCause());
        }
        User user = optionalUser.orElseThrow(() -> new NoSuchElementException("No user with such id"));
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        Optional<User> optionalUser;
        try {
            optionalUser = userRepository.findByEmail(email);
        }  catch (Exception e) {
            throw new EntityNotFoundException
                    ("Can not get user by email from from required resource \'/rest/users/email/{email}\', " + e.getCause());
        }
        User user = optionalUser.orElseThrow(() -> new NoSuchElementException("No user with such email"));
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/role/{ROLE_NAME}")
    public ResponseEntity<Map<String, Set<User>>> getUsersByRoleName(@PathVariable ("ROLE_NAME") String roleName) {
        Optional<Role> optionalRole;
        try {
            optionalRole = roleRepository.findRoleByRoleName(roleName.toUpperCase());
        }  catch (Exception e) {
            throw new EntityNotFoundException
                    ("Can not get role by ROLE_NAME from from required resource \'/rest/users/role/{ROLE_NAME}\', " + e.getCause());
        }
        Role role = optionalRole.orElseThrow(() -> new NoSuchElementException("No role with such ROLE_NAME"));
        Set<User> users = role.getUsers();
        return new ResponseEntity<>(Collections.singletonMap(roleName, users), HttpStatus.OK);
    }

    @Transactional(propagation = Propagation.REQUIRED, timeout = 3, rollbackFor = Exception.class)
    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody UserCreateRequest request, BindingResult result) throws Exception{
        if (result.hasErrors()) {
            throw new IllegalRequestException("Poor information in request body to create user", result);
        }
        User user;
        try {
            user = conversionService.convert(request, User.class);
        } catch (Exception e) {
            throw new ConvertRequestToEntityException("Can not convert create request to user, because of: " +
                    e.getCause());
        }
        if (user == null) {
            throw new NullPointerException("User has not created, check request body");
        }
        try {
            user = userRepository.save(user);
        } catch (Exception e) {
            throw new EntityNotCreatedOrNotUpdatedException("User has not created and saved to DB, because of: " + e.getCause());
        }
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @Transactional(propagation = Propagation.REQUIRED, timeout = 3, rollbackFor = Exception.class)
    @PutMapping
    public ResponseEntity<User> updateUser(@Valid @RequestBody UserUpdateRequest request, BindingResult result)
            throws Exception {
        if (result.hasErrors()) {
            throw new IllegalRequestException("Poor information in request body to update user", result);
        }
        User user;
        try {
            user = conversionService.convert(request, User.class);
        } catch (Exception e) {
            throw new ConvertRequestToEntityException("Can not convert update request to user, because of: " +
                    e.getCause());
        }
        try {
            user = userRepository.save(user);
        } catch (Exception e) {
            throw new EntityNotCreatedOrNotUpdatedException
                    ("User has not been updated and saved to DB, " + e.getCause());
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @Transactional(propagation = Propagation.REQUIRED, timeout = 3, rollbackFor = Exception.class)
    @PatchMapping
    public ResponseEntity<User> patchUpdateUser(@Valid @RequestBody UserPatchRequest request, BindingResult result)
            throws Exception {
        if (result.hasErrors()) {
            throw new IllegalRequestException(result);
        }
        User user;
        try {
            user = conversionService.convert(request, User.class);
        } catch (Exception e) {
            throw new ConvertRequestToEntityException("Can not convert patch request to user, " + e.getCause());
        }
        try {
            user = userRepository.save(user);
        } catch (Exception e) {
            throw new EntityNotCreatedOrNotUpdatedException
                    ("User has not been patch updated and saved to DB, " + e.getCause());
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PatchMapping("/update_nick_name/{id}/{nick_name}")
    public ResponseEntity<User> updateUserNickName(@PathVariable String id, @PathVariable(name = "nick_name") String nickName)
            throws Exception {
        Long parsedId;
        try {
            parsedId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Bad information about user id in resource \'/update_nick_name/{id}/{nick_name}\'. " +
                    "Must be Long type");
        }
        Optional<User> optionalUser;
        try {
            optionalUser = userRepository.findById(parsedId);
        } catch (Exception e) {
            throw new EntityNotFoundException("Can not get user to update nick_name  from DB, " + e.getCause());
        }
        User user = optionalUser.orElseThrow(() -> new NoSuchElementException("No user with such id"));

        try {
            user.setNickName(nickName);
            user.setChanged(Timestamp.valueOf(LocalDateTime.now()));
        } catch (Exception e) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Poor information in request body to patch update user. " +
                    e.getCause());
        }
        try {
            userRepository.save(user);
        } catch (Exception e) {
            throw new EntityNotCreatedOrNotUpdatedException("User has not been saved with new nick_name, " + e.getCause());
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/add_role/{id}/{role_id}")
    public ResponseEntity<LinkUsersRoles> addRoleToUser(@PathVariable String id, @PathVariable(name = "role_id") String roleId)
            throws Exception {
        Long parsedId;
        try {
            parsedId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Bad information about user id in resource \'/add_role/{id}/{role_id\'. " +
                    "Must be Long type");
        }
        Optional<User> optionalUser;
        try {
            optionalUser = userRepository.findById(parsedId);
        } catch (Exception e) {
            throw new EntityNotFoundException("Can not get user to add role to user from DB, " + e.getCause());
        }
        User user = optionalUser.orElseThrow(() -> new NoSuchElementException("No user with such id"));
        Long parsedRoleId;
        try {
            parsedRoleId = Long.parseLong(roleId);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Bad information about role id in resource \'/add_role/{id}/{role_id\'. " +
                    "Must be Long type");
        }
        Optional<Role> optionalRole;
        try {
            optionalRole = roleRepository.findById(parsedRoleId);
        } catch (Exception e) {
            throw new EntityNotFoundException("Can not get role to add role to user from DB, " + e.getCause());
        }
        Role role = optionalRole.orElseThrow(() -> new NoSuchElementException("No role with such id"));
        LinkUsersRoles link = new LinkUsersRoles();
        try {
            link.setUserId(user.getId());
            link.setRoleId(role.getId());
            link.setCreated(Timestamp.valueOf(LocalDateTime.now()));
            link.setChanged(Timestamp.valueOf(LocalDateTime.now()));
        } catch (Exception e) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Poor information in resource to add role to user. " +
                    e.getCause());
        }
        try {
            link = linkUsersRolesRepository.save(link);
        } catch (Exception e) {
            throw new EntityNotCreatedOrNotUpdatedException("New role has not been added to user, " + e.getCause());
        }
        return new ResponseEntity<>(link, HttpStatus.CREATED);
    }

    @Transactional(propagation = Propagation.REQUIRED, timeout = 3, rollbackFor = Exception.class)
    @DeleteMapping("/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable String id) throws Exception {
        Long parsedId;
        try {
            parsedId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Bad information about user id in resource \'/rest/users/{id}\'. " +
                    "Must be Long type");
        }
        Optional<User> optionalUser;
        try {
            optionalUser = userRepository.findById(parsedId);
        } catch (Exception e) {
            throw new EntityNotFoundException("Can not get user to be deleted from DB, " + e.getCause());
        }
        User user = optionalUser.orElseThrow(() -> new NoSuchElementException("No user with such id"));
        try {
            userRepository.delete(user);
        } catch (Exception e) {
            throw new EntityNotDeletedException("User has not been deleted, " + e.getCause());
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
