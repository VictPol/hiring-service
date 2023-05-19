package com.hirix.controller.controllers;

import com.hirix.controller.requests.create.EmployeeCreateRequest;
import com.hirix.controller.requests.create.UserCreateRequest;
import com.hirix.controller.requests.patch.CompanyPatchRequest;
import com.hirix.controller.requests.patch.EmployeePatchRequest;
import com.hirix.controller.requests.search.EmployeeSearchCriteria;
import com.hirix.controller.requests.search.EmployeeSearchCriteriaWithBirthday;
import com.hirix.controller.requests.update.EmployeeUpdateRequest;
import com.hirix.domain.Company;
import com.hirix.domain.Employee;
import com.hirix.domain.Location;
import com.hirix.domain.User;
import com.hirix.domain.enums.Education;
import com.hirix.domain.enums.Gender;
import com.hirix.domain.enums.Health;
import com.hirix.exception.ConvertRequestToEntityException;
import com.hirix.exception.EntityNotCreatedOrNotUpdatedException;
import com.hirix.exception.EntityNotFoundException;
import com.hirix.exception.IllegalRequestException;
import com.hirix.exception.PoorInfoInRequestToCreateUpdateEntity;
import com.hirix.repository.EmployeeRepository;
import com.hirix.repository.LocationRepository;
import com.hirix.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
@RequestMapping("rest/employees")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeRepository employeeRepository;
    private final ConversionService conversionService;
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;

    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        List<Employee> employees;
        try {
            employees = employeeRepository.findAll();
        } catch (Exception e) {
            throw new EntityNotFoundException("Can not get employees from required resource \'rest/employees\', " +
                    e.getCause());
        }
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable String id) {
        Long parsedId;
        try {
            parsedId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Bad employee {id} in resource path \'/rest/employees/{id}\'. Must be Long type");
        }
        if (parsedId < 1L) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Bad employee {id} in resource path \'/rest/employees/{id}\'. " +
                    "Id must be more than 0L");
        }
        Optional<Employee> optionalEmployee;
        try {
            optionalEmployee = employeeRepository.findById(parsedId);
        }  catch (Exception e) {
            throw new EntityNotFoundException
                    ("Can not get employee by id from from required resource \'/rest/employees/{id}\', " + e.getCause());
        }
        Employee employee = optionalEmployee.orElseThrow(() -> new NoSuchElementException("No employee with such id"));
        return new ResponseEntity<>(employee, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<Map<String, List<Employee>>> searchEmployeesByFullNameLike
            (@ModelAttribute EmployeeSearchCriteria criteria) {
        List<Employee> employees = employeeRepository.findEmployeesByFullNameLike("%" + criteria.getQuery() + "%");
        return new ResponseEntity<>(Collections.singletonMap("employees", employees), HttpStatus.OK);
    }

    @GetMapping("/search_name_birthday")
    public ResponseEntity<Map<String, List<Employee>>> searchEmployeesByFullNameLikeAndBirthdayAfter
            (@ModelAttribute EmployeeSearchCriteriaWithBirthday criteria) {
        Timestamp birthday = Timestamp.valueOf(criteria.getBirthday());
        List<Employee> employees = employeeRepository.findEmployeesByFullNameLikeAndBirthdayAfter
                ("%" + criteria.getQuery() + "%", birthday);
        return new ResponseEntity<>(Collections.singletonMap("employees", employees), HttpStatus.OK);
    }

    @Transactional(propagation = Propagation.REQUIRED, timeout = 3, rollbackFor = Exception.class)
    @PostMapping
    public ResponseEntity<Employee> createEmployee(@RequestBody EmployeeCreateRequest request, BindingResult result) throws Exception {
        if (result.hasErrors()) {
            throw new IllegalRequestException("Poor information in request body to create employee", result);
        }
        Employee employee;
        try {
            employee = conversionService.convert(request, Employee.class);
        } catch (Exception e) {
            throw new ConvertRequestToEntityException("Can not convert create request to employee, because of: " +
                    e.getCause());
        }
        if (employee == null) {
            throw new NullPointerException("Employee has not created, check request body");
        }
        try {
            employee = employeeRepository.save(employee);
        } catch (Exception e) {
            throw new EntityNotCreatedOrNotUpdatedException("Employee has not created and saved to DB, because of: " + e.getCause());
        }
        return new ResponseEntity<>(employee, HttpStatus.CREATED);
    }

    @Transactional(propagation = Propagation.REQUIRED, timeout = 3, rollbackFor = Exception.class)
    @PutMapping
    public ResponseEntity<Employee> updateEmployee(@RequestBody EmployeeUpdateRequest request, BindingResult result)
            throws Exception {
       if (result.hasErrors()) {
            throw new IllegalRequestException("Poor information in request body to update employee", result);
       }
        Employee employee;
        try {
            employee = conversionService.convert(request, Employee.class);
        } catch (Exception e) {
            throw new ConvertRequestToEntityException("Can not convert update request to employee, because of: " +
                    e.getCause());
        }
        try {
            employee = employeeRepository.save(employee);
        } catch (Exception e) {
            throw new EntityNotCreatedOrNotUpdatedException
                    ("Employee has not been updated and saved to DB, " + e.getCause());
        }
       return new ResponseEntity<>(employee, HttpStatus.OK);
    }

    @Transactional(propagation = Propagation.REQUIRED, timeout = 3, rollbackFor = Exception.class)
    @PatchMapping
    public ResponseEntity<Employee> patchUpdateEmployee(@Valid @RequestBody EmployeePatchRequest request, BindingResult result)
            throws Exception {
        if (result.hasErrors()) {
            throw new IllegalRequestException(result);
        }
        Employee employee;
        try {
            employee = conversionService.convert(request, Employee.class);
        } catch (Exception e) {
            throw new ConvertRequestToEntityException("Can not convert patch request to employee, " + e.getCause());
        }
        try {
            employee = employeeRepository.save(employee);
        } catch (Exception e) {
            throw new EntityNotCreatedOrNotUpdatedException
                    ("Employee has not been patch updated and saved to DB, " + e.getCause());
        }
        return new ResponseEntity<>(employee, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Employee> deleteEmployee(@PathVariable Long id) {
//        if (result.hasErrors()) {
//            throw new IllegalRequestException(result);
//        }
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        Employee employee = optionalEmployee.get();
        employeeRepository.delete(employee);
        return new ResponseEntity<>(employee, HttpStatus.OK);
    }

}
