package com.hirix.controller.controllers;

import com.hirix.controller.requests.create.CompanyCreateRequest;
import com.hirix.controller.requests.search.CompanySearchCriteria;
import com.hirix.controller.requests.update.CompanyUpdateRequest;
import com.hirix.domain.Company;
import com.hirix.domain.Location;
import com.hirix.domain.User;
import com.hirix.exception.LongNumberFormatException;
import com.hirix.exception.PoorInfoInRequestToCreateUpdateEntity;
import com.hirix.exception.SomeRuntimeException;
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

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("rest/companies")
@RequiredArgsConstructor
public class CompanyController {
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;

    @GetMapping
    public ResponseEntity<List<Company>> getAllCompanies() {
        List<Company> companies;
        try {
            companies = companyRepository.findAll();
        } catch (RuntimeException e) {
            throw new SomeRuntimeException("Can not get companies from required resource \'/rest/companies\', ", e.getCause());
        }
        return new ResponseEntity<>(companies, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Company> getCompanyById(@PathVariable String id) {
        Long parsedId;
        try {
            parsedId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new LongNumberFormatException("Bad company {id} in resource \'/rest/companies/{id}\'. Must be Long type");
        }
        Optional<Company> optionalCompany;
        try {
            optionalCompany = companyRepository.findById(parsedId);
        } catch (RuntimeException e) {
            throw new SomeRuntimeException
                    ("Can not get company by id from from required resource \'/rest/companies/{id}\', ", e.getCause());
        }
        Company company = optionalCompany.orElseThrow(() -> new NoSuchElementException("No company with such id was found"));
        return new ResponseEntity<>(company, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<Map<String, List<Company>>> searchCompaniesByFullTitleLike
            (@ModelAttribute CompanySearchCriteria criteria) {
        List<Company> companies;
        try {
            companies = companyRepository.findCompaniesByFullTitleLike("%" + criteria.getQuery() + "%");
        } catch (RuntimeException e) {
            throw new SomeRuntimeException
                    ("Can not search companies from required resource \'/rest/companies/search\'" + criteria.getQuery() + ", ", e.getCause());
        }
        return new ResponseEntity<>(Collections.singletonMap("companies", companies), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Company> createCompany(@RequestBody CompanyCreateRequest request) {
//        if (result.hasErrors()) {
//            throw new IllegalRequestException(result);
//        }
        Company company = new Company();
        Long userId;
        try {
            userId = request.getUserId();
        } catch (RuntimeException e) {
            throw new PoorInfoInRequestToCreateUpdateEntity
                    ("Poor information about user id in request body to create company. Must be Long type",
                            e.getCause());
        }
        Long locationId;
        try {
            locationId = request.getLocationId();
        } catch (RuntimeException e) {
            throw new PoorInfoInRequestToCreateUpdateEntity
                    ("Poor information about location id in request body to create company. Must be Long type",
                            e.getCause());
        }
        try {
            company.setFullTitle(request.getFullTitle());
            company.setShortTitle(request.getShortTitle());
            company.setRegNumber(request.getRegNumber());
            company.setOrgType(request.getOrgType());
        } catch (RuntimeException e) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Poor information in request body to create company", e.getCause());
        }
        company.setCreated(Timestamp.valueOf(LocalDateTime.now()));
        company.setChanged(Timestamp.valueOf(LocalDateTime.now()));
        Optional<User> optionalUser;
        try {
            optionalUser = userRepository.findById(userId);
        } catch (RuntimeException e) {
            throw new SomeRuntimeException("Can not get user by id from DB, ", e.getCause());
        }
        User user = optionalUser.orElseThrow(() -> new NoSuchElementException("No user with such id was found"));
        if (user.getCompany().getId() < 1L) {
            company.setUser(user);
        } else {
            throw new PoorInfoInRequestToCreateUpdateEntity
                    ("Can not create company, because company with such user exists yet");
        }
        Optional<Location> optionalLocation;
        try {
            optionalLocation = locationRepository.findById(locationId);
        } catch (RuntimeException e) {
            throw new SomeRuntimeException("Can not get location by id from DB, ", e.getCause());
        }
        Location location = optionalLocation.orElseThrow(() -> new NoSuchElementException("No location with such id was found"));
        company.setLocation(location);
        try {
            company = companyRepository.save(company);
        } catch (RuntimeException e) {
            throw new SomeRuntimeException("Company has not created and saved to DB, ", e.getCause());
        }
        return new ResponseEntity<>(company, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Company> updateCompany(@RequestBody CompanyUpdateRequest request) {
//        if (result.hasErrors()) {
//            throw new IllegalRequestException(result);
//        }
        Long id;
        try {
            id = request.getId();
        } catch (RuntimeException e) {
            throw new PoorInfoInRequestToCreateUpdateEntity
                    ("Poor information about company id in request body to update company. Must be Long type",
                            e.getCause());
        }
        Optional<Company> optionalCompany;
        try {
            optionalCompany = companyRepository.findById(id);
        } catch (RuntimeException e) {
            throw new SomeRuntimeException("Can not get company by id from DB, ", e.getCause());
        }
        Company company = optionalCompany.orElseThrow(() -> new NoSuchElementException("No company with such id was found"));
        try {
            company.setFullTitle(request.getFullTitle());
            company.setShortTitle(request.getShortTitle());
            company.setRegNumber(request.getRegNumber());
            company.setOrgType(request.getOrgType());
        } catch (RuntimeException e) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Poor information in request body to update company", e.getCause());
        }

        company.setChanged(Timestamp.valueOf(LocalDateTime.now()));
//        Optional<User> optionalUser = userRepository.findById(request.getUserId());
//        User user = optionalUser.get();
//        company.setUser(user);
        Long locationId;
        try {
            locationId = request.getLocationId();
        } catch (RuntimeException e) {
            throw new PoorInfoInRequestToCreateUpdateEntity
                    ("Poor information about location id in request body to update company. Must be Long type",
                            e.getCause());
        }
        Optional<Location> optionalLocation;
        try {
            optionalLocation = locationRepository.findById(locationId);
        } catch (RuntimeException e) {
            throw new SomeRuntimeException("Can not get location by id from DB, ", e.getCause());
        }
        Location location = optionalLocation.orElseThrow(() -> new NoSuchElementException("No location with such id was found"));
        company.setLocation(location);
        try {
            company = companyRepository.save(company);
        } catch (RuntimeException e) {
            throw new SomeRuntimeException
                    ("Company has not been updated saved to DB, ", e.getCause());
        }
        return new ResponseEntity<>(company, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Company> deleteCompany(@PathVariable String id) {
//        if (result.hasErrors()) {
//            throw new IllegalRequestException(result);
//        }
        Long parsedId;
        try {
            parsedId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new LongNumberFormatException("Bad information about company id in resource \'/rest/companies/{id}\'. " +
                    "Must be Long type");
        }
        Optional<Company> optionalCompany;
        try {
            optionalCompany = companyRepository.findById(parsedId);
        } catch (RuntimeException e) {
            throw new SomeRuntimeException("Can not get company to be deleted from DB, ", e.getCause());
        }
        Company company = optionalCompany.orElseThrow(() -> new NoSuchElementException("No company with such id was found"));

        try {
            companyRepository.delete(company);
        } catch (RuntimeException e) {
            throw new SomeRuntimeException("Company has not been deleted, ", e.getCause());
        }
        return new ResponseEntity<>(company, HttpStatus.OK);
    }

}
