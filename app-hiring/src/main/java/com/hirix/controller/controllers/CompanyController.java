package com.hirix.controller.controllers;

import com.hirix.controller.requests.create.CompanyCreateRequest;
import com.hirix.controller.requests.search.CompanySearchCriteria;
import com.hirix.controller.requests.update.CompanyUpdateRequest;
import com.hirix.domain.Company;
import com.hirix.domain.Location;
import com.hirix.domain.User;
import com.hirix.exception.LongNumberFormatException;
import com.hirix.exception.PoorInfoInRequestToCreateUpdateEntity;
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
    public ResponseEntity<List<Company>> getAllCompanies() throws Exception {
        List<Company> companies;
        try {
            companies = companyRepository.findAll();
        } catch (RuntimeException runtimeException) {
            throw new RuntimeException("Can not get companies from required resource \'/rest/companies\', ", runtimeException.getCause());
        } catch (Exception exception) {
            throw new Exception("Can not get companies from required resource \'/rest/companies\', ", exception.getCause());
        }
        return new ResponseEntity<>(companies, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Company> getCompanyById(@PathVariable String id) throws Exception {
        Long parsedId;
        try {
            parsedId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Bad company {id} in resource \'/rest/companies/{id}\'. Must be Long type");
        }
        Optional<Company> optionalCompany;
        try {
            optionalCompany = companyRepository.findById(parsedId);
        } catch (RuntimeException runtimeException) {
            throw new RuntimeException
                ("Can not get company by id from from required resource \'/rest/companies/{id}\', ", runtimeException.getCause());
        } catch (Exception exception) {
            throw new Exception
                ("Can not get company by id from from required resource \'/rest/companies/{id}\', ", exception.getCause());
        }
        Company company = optionalCompany.orElseThrow(() -> new NoSuchElementException("No company with such id was found"));
        return new ResponseEntity<>(company, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<Map<String, List<Company>>> searchCompaniesByFullTitleLike
            (@ModelAttribute CompanySearchCriteria criteria) throws Exception {
        List<Company> companies;
        String query = criteria.getQuery();
        if (query == null) {
            throw new IllegalArgumentException("Bad argument in search line, must be: \'search?query=word_like_full_company_title\'");
        }
        try {
            companies = companyRepository.findCompaniesByFullTitleLike("%" + query + "%");
        } catch (RuntimeException runtimeException) {
            throw new RuntimeException
                    ("Can not search companies from required resource \'/rest/companies/search\'" + criteria.getQuery() + ", ", runtimeException.getCause());
        } catch (Exception exception) {
            throw new Exception
                    ("Can not search companies from required resource \'/rest/companies/search\'" + criteria.getQuery() + ", ", exception.getCause());
        }
        return new ResponseEntity<>(Collections.singletonMap("companies", companies), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Company> createCompany(@RequestBody CompanyCreateRequest request) throws Exception {
//        if (result.hasErrors()) {
//            throw new IllegalRequestException(result);
//        }
        Company company = new Company();
        Long userId;
        try {
            userId = request.getUserId();
        } catch (RuntimeException runtimeException) {
            throw new PoorInfoInRequestToCreateUpdateEntity
                    ("Poor information about user id in request body to create company. Must be Long type",
                            runtimeException.getCause());
        } catch (Exception exception) {
            throw new PoorInfoInRequestToCreateUpdateEntity
                    ("Poor information about user id in request body to create company. Must be Long type",
                            exception.getCause());
        }
        Long locationId;
        try {
            locationId = request.getLocationId();
        } catch (RuntimeException runtimeException) {
            throw new PoorInfoInRequestToCreateUpdateEntity
                    ("Poor information about location id in request body to create company. Must be Long type",
                            runtimeException.getCause());
        } catch (Exception exception) {
            throw new PoorInfoInRequestToCreateUpdateEntity
                    ("Poor information about location id in request body to create company. Must be Long type",
                            exception.getCause());
        }
        try {
            company.setFullTitle(request.getFullTitle());
            company.setShortTitle(request.getShortTitle());
            company.setRegNumber(request.getRegNumber());
            company.setOrgType(request.getOrgType());
        } catch (RuntimeException runtimeException) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Poor information in request body to create company", runtimeException.getCause());
        } catch (Exception exception) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Poor information in request body to create company", exception.getCause());
        }
        if (company.getFullTitle() == null ||
            company.getShortTitle() == null ||
            company.getRegNumber() == null ||
            company.getOrgType() == null) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Poor information in request body to create company");
        }
        company.setCreated(Timestamp.valueOf(LocalDateTime.now()));
        company.setChanged(Timestamp.valueOf(LocalDateTime.now()));
        Optional<User> optionalUser;
        try {
            optionalUser = userRepository.findById(userId);
        } catch (RuntimeException runtimeException) {
            throw new RuntimeException("Can not get user by id from DB, ", runtimeException.getCause());
        } catch (Exception exception) {
            throw new Exception("Can not get user by id from DB, ", exception.getCause());
        }
        User user = optionalUser.orElseThrow(() -> new NoSuchElementException("No user with such id was found"));
        if (user.getCompany() == null) {
            company.setUser(user);
        } else {
            throw new PoorInfoInRequestToCreateUpdateEntity
                    ("Can not create company, because company with such user id exists yet");
        }
        Optional<Location> optionalLocation;
        try {
            optionalLocation = locationRepository.findById(locationId);
        } catch (RuntimeException runtimeException) {
            throw new RuntimeException("Can not get location by id from DB, ", runtimeException.getCause());
        } catch (Exception exception) {
            throw new Exception("Can not get location by id from DB, ", exception.getCause());
        }
        Location location = optionalLocation.orElseThrow(() -> new NoSuchElementException("No location with such id was found"));
        company.setLocation(location);
        try {
            company = companyRepository.save(company);
        } catch (RuntimeException runtimeException) {
            throw new RuntimeException("Company has not created and saved to DB, ", runtimeException.getCause());
        } catch (Exception exception) {
            throw new Exception("Company has not created and saved to DB, ", exception.getCause());
        }
        return new ResponseEntity<>(company, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Company> updateCompany(@RequestBody CompanyUpdateRequest request) throws Exception {
//        if (result.hasErrors()) {
//            throw new IllegalRequestException(result);
//        }
        Long id;
        try {
            id = request.getId();
        } catch (RuntimeException runtimeException) {
            throw new PoorInfoInRequestToCreateUpdateEntity
                    ("Poor information about company id in request body to update company. Must be Long type",
                            runtimeException.getCause());
        } catch (Exception exception) {
            throw new PoorInfoInRequestToCreateUpdateEntity
                    ("Poor information about company id in request body to update company. Must be Long type",
                            exception.getCause());
        }
        Optional<Company> optionalCompany;
        try {
            optionalCompany = companyRepository.findById(id);
        } catch (RuntimeException runtimeException) {
            throw new RuntimeException("Can not get company by id from DB, ", runtimeException.getCause());
        } catch (Exception exception) {
            throw new RuntimeException("Can not get company by id from DB, ", exception.getCause());
        }
        Company company = optionalCompany.orElseThrow(() -> new NoSuchElementException("No company with such id was found"));
        try {
            company.setFullTitle(request.getFullTitle());
            company.setShortTitle(request.getShortTitle());
            company.setRegNumber(request.getRegNumber());
            company.setOrgType(request.getOrgType());
        } catch (RuntimeException runtimeException) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Poor information in request body to update company", runtimeException.getCause());
        } catch (Exception exception) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Poor information in request body to update company", exception.getCause());
        }
        company.setChanged(Timestamp.valueOf(LocalDateTime.now()));
//        Optional<User> optionalUser = userRepository.findById(request.getUserId());
//        User user = optionalUser.get();
//        company.setUser(user);
        Long locationId;
        try {
            locationId = request.getLocationId();
        } catch (RuntimeException runtimeException) {
            throw new PoorInfoInRequestToCreateUpdateEntity
                    ("Poor information about location id in request body to update company. Must be Long type",
                            runtimeException.getCause());
        } catch (Exception exception) {
            throw new PoorInfoInRequestToCreateUpdateEntity
                    ("Poor information about location id in request body to update company. Must be Long type",
                            exception.getCause());
        }
        Optional<Location> optionalLocation;
        try {
            optionalLocation = locationRepository.findById(locationId);
        } catch (RuntimeException runtimeException) {
            throw new RuntimeException("Can not get location by id from DB, ", runtimeException.getCause());
        } catch (Exception exception) {
            throw new Exception("Can not get location by id from DB, ", exception.getCause());
        }
        Location location = optionalLocation.orElseThrow(() -> new NoSuchElementException("No location with such id was found"));
        company.setLocation(location);
        try {
            company = companyRepository.save(company);
        } catch (RuntimeException runtimeException) {
            throw new RuntimeException
                    ("Company has not been updated and saved to DB, ", runtimeException.getCause());
        } catch (Exception exception) {
            throw new Exception
                    ("Company has not been updated and saved to DB, ", exception.getCause());
        }
        return new ResponseEntity<>(company, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Company> deleteCompany(@PathVariable String id) throws Exception {
//        if (result.hasErrors()) {
//            throw new IllegalRequestException(result);
//        }
        Long parsedId;
        try {
            parsedId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Bad information about company id in resource \'/rest/companies/{id}\'. " +
                    "Must be Long type");
        }
        Optional<Company> optionalCompany;
        try {
            optionalCompany = companyRepository.findById(parsedId);
        } catch (RuntimeException runtimeException) {
            throw new RuntimeException("Can not get company to be deleted from DB, ", runtimeException.getCause());
        } catch (Exception exception) {
            throw new Exception("Can not get company to be deleted from DB, ", exception.getCause());
        }
        Company company = optionalCompany.orElseThrow(() -> new NoSuchElementException("No company with such id was found"));

        try {
            companyRepository.delete(company);
        } catch (RuntimeException runtimeException) {
            throw new RuntimeException("Company has not been deleted, ", runtimeException.getCause());
        } catch (Exception exception) {
            throw new Exception("Company has not been deleted, ", exception.getCause());
        }
        return new ResponseEntity<>(company, HttpStatus.OK);
    }

}
