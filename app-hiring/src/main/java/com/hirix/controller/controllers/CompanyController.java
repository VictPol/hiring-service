package com.hirix.controller.controllers;

import com.hirix.controller.requests.create.CompanyCreateRequest;
import com.hirix.controller.requests.patch.CompanyPatchRequest;
import com.hirix.controller.requests.search.CompanySearchCriteria;
import com.hirix.controller.requests.update.CompanyUpdateRequest;
import com.hirix.domain.Company;
import com.hirix.exception.ConvertRequestToEntityException;
import com.hirix.exception.EntityNotCreatedOrNotUpdatedException;
import com.hirix.exception.EntityNotDeletedException;
import com.hirix.exception.EntityNotFoundException;
import com.hirix.exception.IllegalRequestException;
import com.hirix.exception.PoorInfoInRequestToCreateUpdateEntity;
import com.hirix.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
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
    private final ConversionService conversionService;

    @GetMapping()
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
            throw new NumberFormatException("Bad company {id} in resource path \'/rest/companies/{id}\'. Must be Long type");
        }
        if (parsedId < 1L) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Bad company {id} in resource path \'/rest/companies/{id}\'." +
                    "Id must be more then 0L");
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
        (@Valid @ModelAttribute CompanySearchCriteria criteria, BindingResult result) throws Exception {
        if (result.hasErrors()) {
            throw new IllegalRequestException
                ("Bad argument in search path, must be: \'search?query=word_like_full_company_title\'", result);
        }
        String query;
        try {
            query = criteria.getQuery();
        } catch (Exception e) {
            throw new IllegalArgumentException("Can not get Query from criteria", e.getCause());
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
        Company company;
        try {
            company = conversionService.convert(request, Company.class);
        } catch (Exception e) {
            throw new ConvertRequestToEntityException("Can not convert update request to company, because of: " +
                e.getCause());
        }
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
        Company company;
        try {
            company = conversionService.convert(request, Company.class);
        } catch (Exception e) {
            throw new ConvertRequestToEntityException("Can not convert update request to company", e.getCause());
        }
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
        Company company;
        try {
            company = conversionService.convert(request, Company.class);
        } catch (Exception e) {
            throw new ConvertRequestToEntityException("Can not convert patch request to company", e.getCause());
        }
        try {
            company = companyRepository.save(company);
        } catch (Exception e) {
            throw new EntityNotCreatedOrNotUpdatedException
                ("Company has not been patch updated and saved to DB, ", e.getCause());
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
