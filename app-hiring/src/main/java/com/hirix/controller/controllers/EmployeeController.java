package com.hirix.controller.controllers;

import com.hirix.controller.requests.create.EmployeeCreateRequest;
import com.hirix.controller.requests.create.UserCreateRequest;
import com.hirix.controller.requests.search.EmployeeSearchCriteria;
import com.hirix.controller.requests.search.EmployeeSearchCriteriaWithBirthday;
import com.hirix.controller.requests.update.EmployeeUpdateRequest;
import com.hirix.domain.Employee;
import com.hirix.domain.Location;
import com.hirix.domain.User;
import com.hirix.domain.enums.Education;
import com.hirix.domain.enums.Gender;
import com.hirix.domain.enums.Health;
import com.hirix.repository.EmployeeRepository;
import com.hirix.repository.LocationRepository;
import com.hirix.repository.UserRepository;
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
@RequestMapping("/employees")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;

    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable String id) {
        Long parsedId = Long.parseLong(id);
        Optional<Employee> employee = employeeRepository.findById(parsedId);
        return new ResponseEntity<>(employee.get(), HttpStatus.OK);
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

    @PostMapping
    public ResponseEntity<Employee> createEmployee(@RequestBody EmployeeCreateRequest request) {
//        if (result.hasErrors()) {
//            throw new IllegalRequestException(result);
//        }
        Employee employee = new Employee();
        employee.setFullName(request.getFullName());
        employee.setBirthday(Timestamp.valueOf(request.getBirthday()));
        employee.setEducation(Education.valueOf(request.getEducation()));
        employee.setHealth(Health.valueOf(request.getHealth()));
        employee.setGender(Gender.valueOf(request.getGender()));
        employee.setCreated(Timestamp.valueOf(LocalDateTime.now()));
        employee.setChanged(Timestamp.valueOf(LocalDateTime.now()));
        Optional<User> optionalUser = userRepository.findById(request.getUserId());
        User user = optionalUser.get();
        employee.setUser(user);
        Optional<Location> optionalLocation = locationRepository.findById(request.getLocationId());
        Location location = optionalLocation.get();
        employee.setLocation(location);
        employee = employeeRepository.save(employee);
        return new ResponseEntity<>(employee, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Employee> updateEmployee(@RequestBody EmployeeUpdateRequest request) {
//        if (result.hasErrors()) {
//            throw new IllegalRequestException(result);
//        }
        Optional<Employee> optionalEmployee = employeeRepository.findById(request.getId());
        Employee employee = optionalEmployee.get();
        employee.setFullName(request.getFullName());
        employee.setBirthday(Timestamp.valueOf(request.getBirthday()));
        employee.setEducation(Education.valueOf(request.getEducation()));
        employee.setHealth(Health.valueOf(request.getHealth()));
        employee.setGender(Gender.valueOf(request.getGender()));
        employee.setChanged(Timestamp.valueOf(LocalDateTime.now()));
//        Optional<User> optionalUser = userRepository.findById(request.getUserId());
//        User user = optionalUser.get();
//        employee.setUser(user);
        Optional<Location> optionalLocation = locationRepository.findById(request.getLocationId());
        Location location = optionalLocation.get();
        employee.setLocation(location);
        employee = employeeRepository.save(employee);
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
