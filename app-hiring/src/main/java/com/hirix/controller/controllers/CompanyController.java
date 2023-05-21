package com.hirix.controller.controllers;

import com.hirix.controller.requests.create.*;
import com.hirix.controller.requests.patch.*;
import com.hirix.controller.requests.search.*;
import com.hirix.controller.requests.update.*;
import com.hirix.domain.*;
import com.hirix.exception.*;
import com.hirix.repository.*;
import lombok.*;
import org.springframework.core.convert.*;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.transaction.annotation.*;
import org.springframework.validation.*;
import org.springframework.web.bind.annotation.*;

import javax.validation.*;
import java.util.*;

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
            throw new EntityNotFoundException("Can not get companies from required resource \'/rest/companies\', " +
                e.getCause());
        }
        return new ResponseEntity<>(companies, HttpStatus.OK);
    }

    @GetMapping("/page_one_company/{page}")
    public ResponseEntity<Map<String, Page<Company>>> findAllShowPageWithOneCompany(@PathVariable String page) {
        Integer parsedPage;
        try {
            parsedPage = Integer.parseInt(page);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Bad {page} in resource path \'/rest/companies/page_one_company/{page}\'. " +
                    "Must be Integer type");
        }
        if (parsedPage < 0) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Bad {page} in resource path \'/rest/companies/page_one_company/{page}\'. " +
                    "Id must be not less than 0L");
        }
        Page<Company> companies;
        try {
            companies = companyRepository.findAll(PageRequest.of(parsedPage, 1, Sort.by("fullTitle").ascending()));
        } catch (Exception e) {
            throw new EntityNotFoundException
                    ("Can not get companies from required resource \'/rest/companies/page_one_company/{page}/{size}\', " + e.getCause());
        }
        return new ResponseEntity<>(Collections.singletonMap("page #" + parsedPage, companies), HttpStatus.OK);
    }

    @GetMapping("/page_number_of_companies/{page}/{size}")
    public ResponseEntity<Map<String, Page<Company>>> findAllShowPageBySize(@PathVariable String page, @PathVariable String size) {
        Integer parsedPage;
        try {
            parsedPage = Integer.parseInt(page);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Bad {page} in resource path \'/rest/companies/page_number_of_companies/{page}/{size}\'. " +
                "Must be Integer type");
        }
        if (parsedPage < 0) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Bad {page} in resource path \'/rest/companies/page_number_of_companies/{page}/{size}\'. " +
                "Id must be not less than 0L");
        }
        Integer parsedSize;
        try {
            parsedSize = Integer.parseInt(size);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Bad {size} in resource path \'/rest/companies/page_number_of_companies/{page}/{size}\'. " +
                    "Must be Integer type");
        }
        if (parsedSize < 1) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Bad {size} in resource path \'/rest/companies/page_number_of_companies/{page}/{size}\'. " +
                "Id must be more than 0L");
        }
        Page<Company> companies;
        try {
            companies = companyRepository.findAll(PageRequest.of(parsedPage, parsedSize, Sort.by("fullTitle").ascending()));
        } catch (Exception e) {
            throw new EntityNotFoundException
                ("Can not get companies from required resource \'/rest/companies/page_number_of_companies/{page}/{size}\', " + e.getCause());
        }
        return new ResponseEntity<>(Collections.singletonMap("page #" + parsedPage, companies), HttpStatus.OK);
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
            throw new PoorInfoInRequestToCreateUpdateEntity("Bad company {id} in resource path \'/rest/companies/{id}\'. " +
                    "Id must be more than 0L");
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
        (@Valid @ModelAttribute CompanySearchCriteria criteria, BindingResult result) {
        if (result.hasErrors()) {
            throw new IllegalRequestException
                ("Bad argument in search path, must be: \'search?query=word_like_company_fullTitle\'", result);
        }
        String query;
        try {
            query = criteria.getQuery();
        } catch (Exception e) {
            throw new IllegalArgumentException("Can not get Query from criteria. " + e.getCause());
        }
        if (query == null) {
            throw new IllegalArgumentException
                ("Bad argument in search path, must be: \'search?query=word_like_company_fullTitle\'");
        }
        List<Company> companies;
        try {
            companies = companyRepository.findCompaniesByFullTitleLike("%" + query + "%");
        } catch (Exception e) {
            throw new EntityNotFoundException
                ("Can not search companies from required resource \'/rest/companies/search?query=\'" +
                    criteria.getQuery() + ", " + e.getCause());
        }
        return new ResponseEntity<>(Collections.singletonMap("companies", companies), HttpStatus.OK);
    }

    @Transactional(propagation = Propagation.REQUIRED, timeout = 3, rollbackFor = Exception.class)
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
            throw new ConvertRequestToEntityException("Can not convert create request to company, because of: " +
                e.getCause());
        }
        if (company == null) {
            throw new NullPointerException("Company has not created, check request body");
        }
        try {
            company = companyRepository.save(company);
        } catch (Exception e) {
            throw new EntityNotCreatedOrNotUpdatedException("Company has not created and saved to DB, because of: " + e.getCause());
        }
        return new ResponseEntity<>(company, HttpStatus.CREATED);
    }

    @Transactional(propagation = Propagation.REQUIRED, timeout = 3, rollbackFor = Exception.class)
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
            throw new ConvertRequestToEntityException("Can not convert update request to company, because of: " +
                    e.getCause());
        }
        try {
            company = companyRepository.save(company);
        } catch (Exception e) {
            throw new EntityNotCreatedOrNotUpdatedException
                ("Company has not been updated and saved to DB, " + e.getCause());
        }
        return new ResponseEntity<>(company, HttpStatus.OK);
    }

    @Transactional(propagation = Propagation.REQUIRED, timeout = 3, rollbackFor = Exception.class)
    @PatchMapping
    public ResponseEntity<Company> patchUpdateCompany(@Valid @RequestBody CompanyPatchRequest request, BindingResult result) throws Exception {
        if (result.hasErrors()) {
            throw new IllegalRequestException(result);
        }
        Company company;
        try {
            company = conversionService.convert(request, Company.class);
        } catch (Exception e) {
            throw new ConvertRequestToEntityException("Can not convert patch request to company, " + e.getCause());
        }
        try {
            company = companyRepository.save(company);
        } catch (Exception e) {
            throw new EntityNotCreatedOrNotUpdatedException
                ("Company has not been patch updated and saved to DB, " + e.getCause());
        }
        return new ResponseEntity<>(company, HttpStatus.OK);
    }

    @Transactional(propagation = Propagation.REQUIRED, timeout = 3, rollbackFor = Exception.class)
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
            throw new EntityNotFoundException("Can not get company to be deleted from DB, " + e.getCause());
        }
        Company company = optionalCompany.orElseThrow(() -> new NoSuchElementException("No company with such id"));
        try {
            companyRepository.delete(company);
        } catch (Exception e) {
            throw new EntityNotDeletedException("Company has not been deleted, " + e.getCause());
        }
        return new ResponseEntity<>(company, HttpStatus.OK);
    }
}
