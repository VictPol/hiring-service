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
import com.hirix.exception.EntityNotDeletedException;
import com.hirix.exception.EntityNotFoundException;
import com.hirix.exception.IllegalRequestException;
import com.hirix.exception.PoorInfoInRequestToCreateUpdateEntity;
import com.hirix.repository.EmployeeRepository;
import com.hirix.repository.LocationRepository;
import com.hirix.repository.UserRepository;
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

    @GetMapping("/page_one_employee/{page}")
    public ResponseEntity<Map<String, Page<Employee>>> findAllShowPageWithOneEmployee(@PathVariable String page) {
        Integer parsedPage;
        try {
            parsedPage = Integer.parseInt(page);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Bad {page} in resource path \'/rest/employees/page_one_employee/{page}\'. " +
                    "Must be Integer type");
        }
        if (parsedPage < 0) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Bad {page} in resource path \'/rest/employees/page_one_employee/{page}\'. " +
                    "Id must be not less than 0L");
        }
        Page<Employee> employees;
        try {
            employees = employeeRepository.findAll(PageRequest.of(parsedPage, 1, Sort.by("fullName").ascending()));
        } catch (Exception e) {
            throw new EntityNotFoundException
                    ("Can not get employees from required resource \'/rest/page_one_employee/page/{page}/{size}\', " + e.getCause());
        }
        return new ResponseEntity<>(Collections.singletonMap("page #" + parsedPage, employees), HttpStatus.OK);
    }

    @GetMapping("/page_size_employees/{page}/{size}")
    public ResponseEntity<Map<String, Page<Employee>>> findAllShowPageBySize(@PathVariable String page, @PathVariable String size) {
        Integer parsedPage;
        try {
            parsedPage = Integer.parseInt(page);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Bad {page} in resource path \'/rest/employees/page_size_employees/{page}/{size}\'. " +
                    "Must be Integer type");
        }
        if (parsedPage < 0) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Bad {page} in resource path \'/rest/employees/page_size_employees/{page}/{size}\'. " +
                    "Id must be not less than 0L");
        }
        Integer parsedSize;
        try {
            parsedSize = Integer.parseInt(size);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Bad {size} in resource path \'/rest/employees/page_size_employees/{page}/{size}\'. " +
                    "Must be Integer type");
        }
        if (parsedSize < 1) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Bad {size} in resource path \'/rest/employees/page_size_employees/{page}/{size}\'. " +
                    "Id must be more than 0L");
        }
        Page<Employee> employees;
        try {
            employees = employeeRepository.findAll(PageRequest.of(parsedPage, parsedSize, Sort.by("fullName").ascending()));
        } catch (Exception e) {
            throw new EntityNotFoundException
                    ("Can not get employees from required resource \'/rest/employees/page_size_employees/{page}/{size}\', " + e.getCause());
        }
        return new ResponseEntity<>(Collections.singletonMap("page #" + parsedPage, employees), HttpStatus.OK);
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
            (@Valid @ModelAttribute EmployeeSearchCriteria criteria, BindingResult result) {
        if (result.hasErrors()) {
            throw new IllegalRequestException
                    ("Bad argument in search path, must be: \'search?query=word_like_employee_fullName\'", result);
        }
        String query;
        try {
            query = criteria.getQuery();
        } catch (Exception e) {
            throw new IllegalArgumentException("Can not get Query from criteria. " + e.getCause());
        }
        if (query == null) {
            throw new IllegalArgumentException
                    ("Bad argument in search path, must be: \'search?query=word_like_employee_fullName\'");
        }
        List<Employee> employees;
        try {
            employees = employeeRepository.findEmployeesByFullNameLike("%" + query + "%");
        } catch (Exception e) {
            throw new EntityNotFoundException
                    ("Can not search employees from required resource \'/rest/employees/search?query=query\'" +
                            criteria.getQuery() + ", " + e.getCause());
        }
        return new ResponseEntity<>(Collections.singletonMap("employees", employees), HttpStatus.OK);
    }

    @GetMapping("/search_name_birthday")
    public ResponseEntity<Map<String, List<Employee>>> searchEmployeesByFullNameLikeAndBirthdayAfter
            (@Valid @ModelAttribute EmployeeSearchCriteriaWithBirthday criteria, BindingResult result) {
        if (result.hasErrors()) {
            throw new IllegalRequestException
                    ("Bad argument in search path, must be: \'search?query=word_like_employee_fullName" +
                            "&birthday=yyyy-mm-dd HH:mm:ss.000\'", result);
        }
        String query;
        try {
            query = criteria.getQuery();
        } catch (Exception e) {
            throw new IllegalArgumentException("Can not get Query from criteria. " + e.getCause());
        }
        if (query == null) {
            throw new IllegalArgumentException
                    ("Bad argument in search path, must be: \'search?query=word_like_employee_fullName\'");
        }
        Timestamp birthday;
        try {
            birthday = Timestamp.valueOf(criteria.getBirthday());
        } catch (Exception e) {
            throw new IllegalArgumentException("Can not get birthday from criteria. " + e.getCause());
        }
        List<Employee> employees;
        try {
            employees = employeeRepository.findEmployeesByFullNameLikeAndBirthdayAfter("%" + criteria.getQuery() + "%",
                    birthday);
        } catch (Exception e) {
            throw new EntityNotFoundException
                    ("Can not search employees from required resource \'/rest/employees/search?query=query&birthday=birthday\'" +
                            criteria.getQuery() + ", " + e.getCause());
        }
        return new ResponseEntity<>(Collections.singletonMap("employees", employees), HttpStatus.OK);
    }

    @Transactional(propagation = Propagation.REQUIRED, timeout = 3, rollbackFor = Exception.class)
    @PostMapping
    public ResponseEntity<Employee> createEmployee(@Valid @RequestBody EmployeeCreateRequest request, BindingResult result) throws Exception {
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
    public ResponseEntity<Employee> updateEmployee(@Valid @RequestBody EmployeeUpdateRequest request, BindingResult result)
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

    @Transactional(propagation = Propagation.REQUIRED, timeout = 3, rollbackFor = Exception.class)
    @DeleteMapping("/{id}")
    public ResponseEntity<Employee> deleteEmployee(@PathVariable String id) throws Exception {
        Long parsedId;
        try {
            parsedId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Bad information about employee id in resource \'/rest/employees/{id}\'. " +
                    "Must be Long type");
        }
        Optional<Employee> optionalEmployee;
        try {
            optionalEmployee = employeeRepository.findById(parsedId);
        } catch (Exception e) {
            throw new EntityNotFoundException("Can not get employee to be deleted from DB, " + e.getCause());
        }
        Employee employee = optionalEmployee.orElseThrow(() -> new NoSuchElementException("No employee with such id"));
        try {
            employeeRepository.delete(employee);
        } catch (Exception e) {
            throw new EntityNotDeletedException("Employee has not been deleted, " + e.getCause());
        }
        return new ResponseEntity<>(employee, HttpStatus.OK);
    }

}
