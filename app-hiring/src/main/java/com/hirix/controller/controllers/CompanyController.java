package com.hirix.controller.controllers;

import com.hirix.controller.requests.create.CompanyCreateRequest;
import com.hirix.controller.requests.patch.CompanyPatchRequest;
import com.hirix.controller.requests.search.CompanySearchCriteria;
import com.hirix.controller.requests.update.CompanyUpdateRequest;
import com.hirix.domain.Company;
import com.hirix.domain.Location;
import com.hirix.domain.User;
import com.hirix.exception.EntityNotCreatedOrNotUpdatedException;
import com.hirix.exception.EntityNotDeletedException;
import com.hirix.exception.EntityNotFoundException;
import com.hirix.exception.IllegalRequestException;
import com.hirix.exception.PoorInfoInRequestToCreateUpdateEntity;
import com.hirix.repository.CompanyRepository;
import com.hirix.repository.LocationRepository;
import com.hirix.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        } catch (Exception e) {
            throw new EntityNotFoundException("Can not get companies from required resource \'/rest/companies\', ",
                e.getCause());
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
        }  catch (Exception e) {
            throw new EntityNotFoundException
                ("Can not get company by id from from required resource \'/rest/companies/{id}\', ", e.getCause());
        }
        Company company = optionalCompany.orElseThrow(() -> new NoSuchElementException("No company with such id"));
        return new ResponseEntity<>(company, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<Map<String, List<Company>>> searchCompaniesByFullTitleLike
        (@ModelAttribute CompanySearchCriteria criteria) throws Exception {
        String query;
        try {
            query = criteria.getQuery();
        } catch (Exception e) {
            throw new Exception("Can not get Query from criteria", e.getCause());
        }
        if (query == null) {
            throw new IllegalArgumentException
                ("Bad argument in search path, must be: \'search?query=word_like_full_company_title\'");
        }
        List<Company> companies;
        try {
            companies = companyRepository.findCompaniesByFullTitleLike("%" + query + "%");
        } catch (Exception e) {
            throw new EntityNotFoundException
                ("Can not search companies from required resource \'/rest/companies/search?query=\'" +
                    criteria.getQuery() + ", ", e.getCause());
        }
        return new ResponseEntity<>(Collections.singletonMap("companies", companies), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Company> createCompany(@Valid @RequestBody CompanyCreateRequest request, BindingResult result)
            throws Exception {
        if (result.hasErrors()) {
            throw new IllegalRequestException("Poor information in request body to create company", result);
        }
        Company company = new Company();
        try {
            company.setFullTitle(request.getFullTitle());
            company.setShortTitle(request.getShortTitle());
            company.setRegNumber(request.getRegNumber());
            company.setOrgType(request.getOrgType());
        } catch (Exception e) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Poor information in request body to create company",
                e.getCause());
        }
        if (company.getFullTitle() == null ||
            company.getShortTitle() == null ||
            company.getRegNumber() == null ||
            company.getOrgType() == null ||
            company.getUser().getId() < 1 ||
            company.getLocation().getId() < 1) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Poor information in request body to create company");
        }
        company.setCreated(Timestamp.valueOf(LocalDateTime.now()));
        company.setChanged(Timestamp.valueOf(LocalDateTime.now()));
        Long userId;
        try {
            userId = request.getUserId();
        } catch (Exception e) {
            throw new PoorInfoInRequestToCreateUpdateEntity
                    ("Poor information about user id in request body to create company. Must be Long type",
                            e.getCause());
        }
        Optional<User> optionalUser;
        try {
            optionalUser = userRepository.findById(userId);
        } catch (Exception e) {
            throw new EntityNotFoundException("Can not get user by id from DB, ", e.getCause());
        }
        User user = optionalUser.orElseThrow(() -> new NoSuchElementException("No user with such id"));
        if (user.getCompany() == null) {
            company.setUser(user);
        } else {
            throw new PoorInfoInRequestToCreateUpdateEntity
                    ("Can not create company, because company with such user id exists yet");
        }
        Long locationId;
        try {
            locationId = request.getLocationId();
        } catch (Exception e) {
            throw new PoorInfoInRequestToCreateUpdateEntity
                    ("Poor information about location id in request body to create company. Must be Long type",
                            e.getCause());
        }
        Optional<Location> optionalLocation;
        try {
            optionalLocation = locationRepository.findById(locationId);
        } catch (Exception e) {
            throw new EntityNotFoundException("Can not get location by id from DB, ", e.getCause());
        }
        Location location = optionalLocation.orElseThrow(() -> new NoSuchElementException
            ("No location with such id was found"));
        company.setLocation(location);
        try {
            company = companyRepository.save(company);
        } catch (Exception e) {
            throw new EntityNotCreatedOrNotUpdatedException("Company has not created and saved to DB, ", e.getCause());
        }
        return new ResponseEntity<>(company, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Company> updateCompany(@Valid @RequestBody CompanyUpdateRequest request, BindingResult result)
        throws Exception {
        if (result.hasErrors()) {
            throw new IllegalRequestException("Poor information in request body to update company", result);
        }
        Long id;
        try {
            id = request.getId();
        } catch (Exception exception) {
            throw new PoorInfoInRequestToCreateUpdateEntity
                ("Poor information about company id in request body to update company. Must be Long type",
                    exception.getCause());
        }
        Optional<Company> optionalCompany;
        try {
            optionalCompany = companyRepository.findById(id);
        } catch (Exception e) {
            throw new EntityNotFoundException("Can not get company by id from DB, ", e.getCause());
        }
        Company company = optionalCompany.orElseThrow(() -> new NoSuchElementException("No company with such id"));
        try {
            company.setFullTitle(request.getFullTitle());
            company.setShortTitle(request.getShortTitle());
            company.setRegNumber(request.getRegNumber());
            company.setOrgType(request.getOrgType());
        } catch (Exception e) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Poor information in request body to update company",
                e.getCause());
        }
        company.setChanged(Timestamp.valueOf(LocalDateTime.now()));
        Long userId;
        try {
            userId = request.getUserId();
        } catch (Exception e) {
            throw new PoorInfoInRequestToCreateUpdateEntity
                    ("Poor information about user id in request body to create company. Must be Long type",
                            e.getCause());
        }
        Optional<User> optionalUser;
        try {
            optionalUser = userRepository.findById(userId);
        } catch (Exception e) {
            throw new EntityNotFoundException("Can not get user by id from DB, ", e.getCause());
        }
        User user = optionalUser.orElseThrow(() -> new NoSuchElementException("No user with such id"));
        if (user.getCompany() == null) {
            company.getUser().setCompany(null);
            company.setUser(user);
        } else {
            throw new PoorInfoInRequestToCreateUpdateEntity
                    ("Can not create company, because company with such user id exists yet");
        }
        Long locationId;
        try {
            locationId = request.getLocationId();
        } catch (Exception exception) {
            throw new PoorInfoInRequestToCreateUpdateEntity
                ("Poor information about location id in request body to update company. Must be Long type",
                    exception.getCause());
        }
        if (locationId < 1) {
            throw new PoorInfoInRequestToCreateUpdateEntity
                ("Poor information about location id in request body to update company. Value must be more then 1");
        }
        Optional<Location> optionalLocation;
        try {
            optionalLocation = locationRepository.findById(locationId);
        } catch (Exception exception) {
            throw new EntityNotFoundException("Can not get location by id from DB, ", exception.getCause());
        }
        Location location = optionalLocation.orElseThrow(() -> new NoSuchElementException("No location with such id"));
        company.setLocation(location);
        try {
            company = companyRepository.save(company);
        } catch (Exception e) {
            throw new EntityNotCreatedOrNotUpdatedException
                ("Company has not been updated and saved to DB, ", e.getCause());
        }
        return new ResponseEntity<>(company, HttpStatus.OK);
    }

    @PatchMapping
    public ResponseEntity<Company> patchUpdateCompany(@Valid @RequestBody CompanyPatchRequest request, BindingResult result) throws Exception {
        if (result.hasErrors()) {
            throw new IllegalRequestException(result);
        }
        Long id;
        try {
            id = request.getId();
        } catch (Exception e) {
            throw new PoorInfoInRequestToCreateUpdateEntity
                ("Poor information about company id in request body to update company. Must be Long type",
                    e.getCause());
        }
        Optional<Company> optionalCompany;
        try {
            optionalCompany = companyRepository.findById(id);
        } catch (Exception e) {
            throw new EntityNotFoundException("Can not get company by id from DB, ", e.getCause());
        }
        Company company = optionalCompany.orElseThrow(() -> new NoSuchElementException("No company with such id"));
        try {
            if (request.getFullTitle() != null) {
                company.setFullTitle(request.getFullTitle());
            }
            if (request.getShortTitle() != null) {
                company.setShortTitle(request.getShortTitle());
            }
            if (request.getRegNumber() != null) {
                company.setRegNumber(request.getRegNumber());
            }
            if (request.getOrgType() != null) {
                company.setOrgType(request.getOrgType());
            }
        } catch (Exception e) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Poor information in request body to update company",
                e.getCause());
        }
        company.setChanged(Timestamp.valueOf(LocalDateTime.now()));
        Long userId;
        try {
            userId = request.getUserId();
        } catch (Exception e) {
            throw new PoorInfoInRequestToCreateUpdateEntity
                    ("Poor information about user id in request body to create company. Must be Long type",
                            e.getCause());
        }
        if (userId != null && userId > 1) {
            Optional<User> optionalUser;
            try {
                optionalUser = userRepository.findById(userId);
            } catch (Exception e) {
                throw new EntityNotFoundException("Can not get user by id from DB, ", e.getCause());
            }
            User user = optionalUser.orElseThrow(() -> new NoSuchElementException("No user with such id"));
            if (user.getCompany() == null) {
                company.getUser().setCompany(null);
                company.setUser(user);
            } else {
                throw new PoorInfoInRequestToCreateUpdateEntity
                        ("Can not create company, because company with such user id exists yet");
            }
        }
        Long locationId;
        try {
            locationId = request.getLocationId();
        } catch (Exception e) {
            throw new PoorInfoInRequestToCreateUpdateEntity
                ("Poor information about location id in request body to update company. Must be Long type",
                    e.getCause());
        }
        if (locationId != null && locationId > 1) {
            Optional<Location> optionalLocation;
            try {
                optionalLocation = locationRepository.findById(locationId);
            } catch (Exception exception) {
                throw new EntityNotFoundException("Can not get location by id from DB, ", exception.getCause());
            }
            Location location = optionalLocation.orElseThrow(() -> new NoSuchElementException("No location with such id"));
            company.setLocation(location);
        }
        try {
            company = companyRepository.save(company);
        } catch (Exception e) {
            throw new EntityNotCreatedOrNotUpdatedException
                ("Company has not been updated and saved to DB, ", e.getCause());
        }
        return new ResponseEntity<>(company, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Company> deleteCompany(@PathVariable String id) throws Exception {
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
        } catch (Exception e) {
            throw new EntityNotFoundException("Can not get company to be deleted from DB, ", e.getCause());
        }
        Company company = optionalCompany.orElseThrow(() -> new NoSuchElementException("No company with such id"));
        try {
            companyRepository.delete(company);
        } catch (Exception e) {
            throw new EntityNotDeletedException("Company has not been deleted, ", e.getCause());
        }
        return new ResponseEntity<>(company, HttpStatus.OK);
    }

}
