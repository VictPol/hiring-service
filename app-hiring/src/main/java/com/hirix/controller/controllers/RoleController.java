package com.hirix.controller.controllers;

import com.hirix.controller.requests.search.RoleSearchCriteria;
import com.hirix.domain.Role;
import com.hirix.exception.EntityNotCreatedOrNotUpdatedException;
import com.hirix.exception.EntityNotFoundException;
import com.hirix.exception.IllegalRequestException;
import com.hirix.exception.PoorInfoInRequestToCreateUpdateEntity;
import com.hirix.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("rest/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleRepository roleRepository;

    @GetMapping
    public ResponseEntity<List<Role>> getAllRoles() {
        List<Role> roles;
        try {
            roles = roleRepository.findAll();
        } catch (Exception e) {
            throw new EntityNotFoundException("Can not get roles from required resource \'rest/roles\', " +
                    e.getCause());
        }
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }

    @GetMapping("/page_one_role/{page}")
    public ResponseEntity<Map<String, Page<Role>>> findAllShowPageWithOneRole(@PathVariable String page) {
        Integer parsedPage;
        try {
            parsedPage = Integer.parseInt(page);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Bad {page} in resource path \'/rest/roles/page_one_role/{page}\'. " +
                    "Must be Integer type");
        }
        if (parsedPage < 0) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Bad {page} in resource path \'/rest/roles/page_one_role/{page}\'. " +
                    "Id must be not less than 0L");
        }
        Page<Role> roles;
        try {
            roles = roleRepository.findAll(PageRequest.of(parsedPage, 1, Sort.by("roleName").ascending()));
        } catch (Exception e) {
            throw new EntityNotFoundException
                    ("Can not get roles from required resource \'/rest/roles/page_one_role/{page}\', " + e.getCause());
        }
        return new ResponseEntity<>(Collections.singletonMap("page #" + parsedPage, roles), HttpStatus.OK);
    }

    @GetMapping("/page_size_roles/{page}/{size}")
    public ResponseEntity<Map<String, Page<Role>>> findAllShowPageBySize(@PathVariable String page, @PathVariable String size) {
        Integer parsedPage;
        try {
            parsedPage = Integer.parseInt(page);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Bad {page} in resource path \'/rest/roles/page_size_roles/{page}/{size}}\'. " +
                    "Must be Integer type");
        }
        if (parsedPage < 0) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Bad {page} in resource path \'/rest/roles/page_size_roles/{page}/{size}}\'. " +
                    "Id must be not less than 0L");
        }
        Integer parsedSize;
        try {
            parsedSize = Integer.parseInt(size);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Bad {size} in resource path \'/rest/roles/page_size_roles/{page}/{size}}\'. " +
                    "Must be Integer type");
        }
        if (parsedSize < 1) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Bad {size} in resource path \'/rest/users/page_size_users/{page}/{size}\', " +
                    "Id must be more than 0L");
        }
        Page<Role> roles;
        try {
            roles = roleRepository.findAll(PageRequest.of(parsedPage, parsedSize, Sort.by("roleName").ascending()));
        } catch (Exception e) {
            throw new EntityNotFoundException
                    ("Can not get users from required resource \'/rest/users/page_size_users/{page}/{size}\', " + e.getCause());
        }
        return new ResponseEntity<>(Collections.singletonMap("page #" + parsedPage, roles), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Role> getRoleById(@PathVariable String id) {
        Role role = parseRoleId(id);
        return new ResponseEntity<>(role, HttpStatus.OK);
    }

    @GetMapping("/role/{ROLE_NAME}")
    public ResponseEntity<Role> getRoleByRoleName(@PathVariable("ROLE_NAME") String roleName) {
        Optional<Role> optionalRole;
        try {
            optionalRole = roleRepository.findRoleByRoleName(roleName);
        } catch (Exception e) {
            throw new EntityNotFoundException
                    ("Can not get role by ROLE_NAME from required resource \'/rest/roles/role/{ROLE_NAME}\', " + e.getCause());
        }
        Role role = optionalRole.orElseThrow(() -> new NoSuchElementException("No role with such ROLE_NAME"));
        return new ResponseEntity<>(role, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<Map<String, List<Role>>> searchRolesByRoleNameLike
            (@Valid @ModelAttribute RoleSearchCriteria criteria, BindingResult result) {
        if (result.hasErrors()) {
            throw new IllegalRequestException
                    ("Bad argument in search path, must be: \'search?query=word_like_ROLE_NAME\'", result);
        }
        String query;
        try {
            query = criteria.getQuery();
        } catch (Exception e) {
            throw new IllegalArgumentException("Can not get query=word_like_ROLE_NAME from criteria. " + e.getCause());
        }
        if (query == null) {
            throw new IllegalArgumentException
                    ("Bad argument in search path, must be: \'search?query=word_like_ROLE_NAME\'");
        }
        List<Role> roles;
        try {
            roles = roleRepository.findRolesByRoleNameLike("%" + criteria.getQuery().toUpperCase() + "%");
        } catch (Exception e) {
            throw new EntityNotFoundException
                    ("Can not search employees from required resource \'/rest/roles/search?query=word_like_ROLE_NAME\'" +
                            criteria.getQuery() + ", " + e.getCause());
        }
        return new ResponseEntity<>(Collections.singletonMap("roles", roles), HttpStatus.OK);
    }

    @PostMapping("/role/{ROLE_NAME}")
    public ResponseEntity<Role> createRole(@PathVariable("ROLE_NAME") String roleName) throws Exception {
        if (!roleName.startsWith("ROLE_")) {
            throw new PoorInfoInRequestToCreateUpdateEntity("ROLE_NAME must start with prefix \"ROLE_\"");
        }
        Role role = new Role();
        try {
            role.setRoleName(roleName.toUpperCase());
            role.setCreated(Timestamp.valueOf(LocalDateTime.now()));
            role.setChanged(Timestamp.valueOf(LocalDateTime.now()));
        } catch (Exception e) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Poor information in resource to create role, " + e.getCause());
        }
        try {
            role = roleRepository.save(role);
        } catch (Exception e) {
            throw new EntityNotCreatedOrNotUpdatedException("New role has not been created and saved, " + e.getCause());
        }
        return new ResponseEntity<>(role, HttpStatus.CREATED);
    }

    @PutMapping("/role/{old_ROLE_NAME}/{new_ROLE_NAME}")
    public ResponseEntity<Role> updateRole(@PathVariable("old_ROLE_NAME") String oldRoleName,
                                           @PathVariable("new_ROLE_NAME") String newRoleName) throws Exception {
        if (!oldRoleName.startsWith("ROLE_") || !newRoleName.startsWith("ROLE_")) {
            throw new PoorInfoInRequestToCreateUpdateEntity("ROLE_NAME must start with prefix \"ROLE_\"");
        }
        Optional<Role> optionalRole;
        try {
            optionalRole = roleRepository.findRoleByRoleName(oldRoleName);
        } catch (Exception e) {
            throw new EntityNotFoundException
                    ("Can not get role by old_ROLE_NAME from required resource \'/rest/roles/role/{old_ROLE_NAME/{new_ROLE_NAME}\', " +
                            e.getCause());
        }
        Role role = optionalRole.orElseThrow(() -> new NoSuchElementException("No role with such ROLE_NAME"));
        try {
            role.setRoleName(newRoleName);
            role.setChanged(Timestamp.valueOf(LocalDateTime.now()));
        } catch (Exception e) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Poor information in resource to create role, " + e.getCause());
        }
        try {
            role = roleRepository.save(role);
        } catch (Exception e) {
            throw new EntityNotCreatedOrNotUpdatedException("New role has not been updated and saved, " + e.getCause());
        }
        return new ResponseEntity<>(role, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Role> deleteRoleById(@PathVariable String id) throws Exception {
        Role role = parseRoleId(id);
        try {
            roleRepository.delete(role);
        } catch (Exception e) {
            throw new EntityNotCreatedOrNotUpdatedException("New role has not been updated and saved, " + e.getCause());
        }
        return new ResponseEntity<>(role, HttpStatus.OK);
    }

    private Role parseRoleId(@PathVariable String id) {
        Long parsedId;
        try {
            parsedId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Bad role {id} in resource path \'/rest/roles/{id}\'. Must be Long type");
        }
        if (parsedId < 1L) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Bad role {id} in resource path \'/rest/roles/{id}\'. " +
                    "Id must be more than 0L");
        }
        Optional<Role> optionalRole;
        try {
            optionalRole = roleRepository.findById(parsedId);
        } catch (Exception e) {
            throw new EntityNotFoundException
                    ("Can not get role by id from from required resource \'/rest/roles/{id}\', " + e.getCause());
        }
        Role role = optionalRole.orElseThrow(() -> new NoSuchElementException("No role with such id"));
        return role;
    }
}
