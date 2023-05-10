package com.hirix.controller.controllers;

import com.hirix.controller.requests.create.CompanyCreateRequest;
import com.hirix.controller.requests.create.EmployeeCreateRequest;
import com.hirix.controller.requests.search.CompanySearchCriteria;
import com.hirix.controller.requests.search.EmployeeSearchCriteria;
import com.hirix.controller.requests.update.CompanyUpdateRequest;
import com.hirix.domain.Company;
import com.hirix.domain.Employee;
import com.hirix.domain.Location;
import com.hirix.domain.User;
import com.hirix.domain.enums.Education;
import com.hirix.domain.enums.Gender;
import com.hirix.domain.enums.Health;
import com.hirix.repository.CompanyRepository;
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
@RequestMapping("/companies")
@RequiredArgsConstructor
public class CompanyController {
    private  final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;

    @GetMapping
    public ResponseEntity<List<Company>> getAllCompanies() {
        List<Company> companies = companyRepository.findAll();
        return new ResponseEntity<>(companies, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Company> getCompanyById(@PathVariable String id) {
        Long parsedId = Long.parseLong(id);
        Optional<Company> company = companyRepository.findById(parsedId);
        return new ResponseEntity<>(company.get(), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<Map<String, List<Company>>> searchCompaniesByFullTitleLike
            (@ModelAttribute CompanySearchCriteria criteria) {
        List<Company> companies = companyRepository.findCompaniesByFullTitleLike("%" + criteria.getQuery() + "%");
        return new ResponseEntity<>(Collections.singletonMap("companies", companies), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Company> createCompany(@RequestBody CompanyCreateRequest request) {
//        if (result.hasErrors()) {
//            throw new IllegalRequestException(result);
//        }
        Company company = new Company();
        company.setFullTitle(request.getFullTitle());
        company.setShortTitle(request.getShortTitle());
        company.setRegNumber(request.getRegNumber());
        company.setOrgType(request.getOrgType());
        company.setCreated(Timestamp.valueOf(LocalDateTime.now()));
        company.setChanged(Timestamp.valueOf(LocalDateTime.now()));
        Optional<User> optionalUser = userRepository.findById(request.getUserId());
        User user = optionalUser.get();
        company.setUser(user);
        Optional<Location> optionalLocation = locationRepository.findById(request.getLocationId());
        Location location = optionalLocation.get();
        company.setLocation(location);
        company = companyRepository.save(company);
        return new ResponseEntity<>(company, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Company> updateCompany(@RequestBody CompanyUpdateRequest request) {
//        if (result.hasErrors()) {
//            throw new IllegalRequestException(result);
//        }
        Optional<Company> optionalCompany = companyRepository.findById(request.getId());
        Company company = optionalCompany.get();
        company.setFullTitle(request.getFullTitle());
        company.setShortTitle(request.getShortTitle());
        company.setRegNumber(request.getRegNumber());
        company.setOrgType(request.getOrgType());
        company.setChanged(Timestamp.valueOf(LocalDateTime.now()));
//        Optional<User> optionalUser = userRepository.findById(request.getUserId());
//        User user = optionalUser.get();
//        company.setUser(user);
        Optional<Location> optionalLocation = locationRepository.findById(request.getLocationId());
        Location location = optionalLocation.get();
        company.setLocation(location);
        company = companyRepository.save(company);
        return new ResponseEntity<>(company, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Company> deleteCompany(@PathVariable Long id) {
//        if (result.hasErrors()) {
//            throw new IllegalRequestException(result);
//        }
        Optional<Company> optionalCompany = companyRepository.findById(id);
        Company company = optionalCompany.get();
        companyRepository.delete(company);
        return new ResponseEntity<>(company, HttpStatus.OK);
    }

}
