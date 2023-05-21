package com.hirix.controller.controllers;

import com.hirix.controller.requests.create.OfferCreateRequest;
import com.hirix.controller.requests.update.OfferUpdateRequestCompany;
import com.hirix.controller.requests.update.OfferUpdateRequestEmployee;
import com.hirix.domain.Offer;
import com.hirix.exception.ConvertRequestToEntityException;
import com.hirix.exception.EntityNotCreatedOrNotUpdatedException;
import com.hirix.exception.EntityNotDeletedException;
import com.hirix.exception.EntityNotFoundException;
import com.hirix.exception.IllegalRequestException;
import com.hirix.exception.PoorInfoInRequestToCreateUpdateEntity;
import com.hirix.repository.OfferRepository;
import com.hirix.repository.RequirementRepository;
import com.hirix.repository.SkillRepository;
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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
@RequestMapping("rest/offers")
@RequiredArgsConstructor
public class OfferController {
    private final OfferRepository offerRepository;
    private final ConversionService conversionService;

    @GetMapping
    public ResponseEntity<List<Offer>> getAllOffers() {
        List<Offer> offers;
        try {
            offers = offerRepository.findAll();
        } catch (Exception e) {
            throw new EntityNotFoundException("Can not get offers from required resource \'rest/offers\'. " +
                    e.getCause());
        }
        return new ResponseEntity<>(offers, HttpStatus.OK);
    }

    @GetMapping("/page_one_offer/{page}")
    public ResponseEntity<Map<String, Page<Offer>>> findAllShowPageWithOneOffer(@PathVariable String page) {
        Integer parsedPage;
        try {
            parsedPage = Integer.parseInt(page);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Bad {page} in resource path \'/rest/offers/page_one_offer/{page}\'. " +
                    "Must be Integer type");
        }
        if (parsedPage < 0) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Bad {page} in resource path \'/rest/offers/page_one_offer/{page}\'. " +
                    "Id must be not less than 0L");
        }
        Page<Offer> offers;
        try {
            offers = offerRepository.findAll(PageRequest.of(parsedPage, 1, Sort.by("id").ascending()));
        } catch (Exception e) {
            throw new EntityNotFoundException
                    ("Can not get offers from required resource \'/rest/offers/page_one_offer/{page}\', " + e.getCause());
        }
        return new ResponseEntity<>(Collections.singletonMap("page #" + parsedPage, offers), HttpStatus.OK);
    }

    @GetMapping("/page_size_offers/{page}/{size}")
    public ResponseEntity<Map<String, Page<Offer>>> findAllShowPageBySize(@PathVariable String page, @PathVariable String size) {
        Integer parsedPage;
        try {
            parsedPage = Integer.parseInt(page);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Bad {page} in resource path \'/rest/offers/page_size_offers/{page}/{size}\'. " +
                    "Must be Integer type");
        }
        if (parsedPage < 0) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Bad {page} in resource path \'/rest/offers/page_size_offers/{page}/{size}\'. " +
                    "Id must be not less than 0L");
        }
        Integer parsedSize;
        try {
            parsedSize = Integer.parseInt(size);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Bad {size} in resource path \'/rest/offers/page_size_offers/{page}/{size}\'. " +
                    "Must be Integer type");
        }
        if (parsedSize < 1) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Bad {size} in resource path \'/rest/offers/page_size_offers/{page}/{size}\'. " +
                    "Id must be more than 0L");
        }
        Page<Offer> offers;
        try {
            offers = offerRepository.findAll(PageRequest.of(parsedPage, parsedSize, Sort.by("id").ascending()));
        } catch (Exception e) {
            throw new EntityNotFoundException
                    ("Can not get skills from required resource \'/rest/offers/page_size_offers/{page}/{size}\', " + e.getCause());
        }
        return new ResponseEntity<>(Collections.singletonMap("page #" + parsedPage, offers), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Offer> getOfferById(@PathVariable String id) {
        Long parsedId;
        try {
            parsedId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Bad offer {id} in resource path \'/rest/offers/{id}\'. Must be Long type");
        }
        if (parsedId < 1L) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Bad offer {id} in resource path \'/rest/offers/{id}\'. " +
                    "Id must be more than 0L");
        }
        Optional<Offer> optionalOffer;
        try {
            optionalOffer = offerRepository.findById(parsedId);
        } catch (Exception e) {
            throw new EntityNotFoundException
                    ("Can not get offer by id from from required resource \'/rest/skills/{id}\'. " + e.getCause());
        }
        Offer offer = optionalOffer.orElseThrow(() -> new NoSuchElementException("No offer with such id"));
        return new ResponseEntity<>(offer, HttpStatus.OK);
    }

    @GetMapping("/skill/{id}")
    public ResponseEntity<List<Offer>> getOfferBySkillId(@PathVariable String id) {
        Long parsedId;
        try {
            parsedId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Bad skill {id} in resource path \'rest/offers/skill/{id}\'. " +
                "Must be Long type");
        }
        if (parsedId < 1L) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Bad skill {id} in resource path \'rest/offers/skill/{id}\'. " +
                "Id must be more than 0L");
        }
        List<Offer> offers = offerRepository.findOffersBySkillIdQuery(parsedId);
        return new ResponseEntity<>(offers, HttpStatus.OK);
    }

    @GetMapping("/skill/{id}/requirement_salary_max")
    public ResponseEntity<List<Offer>> getOfferBySkillIdAndSalaryMax(@PathVariable String id) {
        Long parsedId;
        try {
            parsedId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Bad skill {id} in resource path \'rest/offers/skill/{id}/requirement_salary_max\'. " +
                "Must be Long type");
        }
        if (parsedId < 1L) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Bad skill {id} in resource path \'rest/offers/skill/{id}/requirement_salary_max\'. " +
                "Id must be more than 0L");
        }
        List<Offer> offers;
        try {
            offers = offerRepository.findOffersBySkillIdQueryAndSalaryMax(parsedId);
        } catch (Exception e) {
            throw new EntityNotFoundException
                ("Can not find offers by skill id with salary_max from required resource \'rest/offers/skill/{id}/requirement_salary_max\', " +
                    e.getCause());
        }
        return new ResponseEntity<>(offers, HttpStatus.OK);
    }

    @GetMapping("/requirement/{id}/skill_salary_min")
    public ResponseEntity<List<Offer>> getOfferByRequirementIdAndSalaryMin(@PathVariable String id) {
        Long parsedId;
        try {
            parsedId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Bad skill {id} in resource path \'rest/offers/requirement/{id}/skill_salary_min\'. " +
                    "Must be Long type");
        }
        if (parsedId < 1L) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Bad skill {id} in resource path \'rest/offers/requirement/{id}/skill_salary_min\'. " +
                    "Id must be more than 0L");
        }
        List<Offer> offers;
        try {
            offers = offerRepository.findOffersByRequirementIdQueryAndSalaryMin(parsedId);
        } catch (Exception e) {
            throw new EntityNotFoundException
                    ("Can not find offers by skill id with salary_max from required resource \'rest/offers/requirement/{id}/skill_salary_min\', " +
                            e.getCause());
        }
        return new ResponseEntity<>(offers, HttpStatus.OK);
    }

    @GetMapping("/employee/{id}")
    public ResponseEntity<List<Offer>> getOfferByEmployeeId(@PathVariable String id) {
        Long parsedId;
        try {
            parsedId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Bad employee {id} in resource path \'rest/offers/employee/{id}\'. " +
                "Must be Long type");
        }
        if (parsedId < 1L) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Bad employee {id} in resource path \'rest/offers/employee/{id}\'. " +
               "Id must be more than 0L");
        }
        List<Offer> offers;
        try {
            offers = offerRepository.findOffersByEmployeeIdQuery(parsedId);
        } catch (Exception e) {
            throw new EntityNotFoundException
                ("Can not find offers by employee id from required resource \'rest/offers/employee/{id}\', " +
                    e.getCause());
        }
        return new ResponseEntity<>(offers, HttpStatus.OK);
    }

    @GetMapping("/requirement/{id}")
    public ResponseEntity<List<Offer>> getOfferByRequirementId(@PathVariable String id) {
        Long parsedId;
        try {
            parsedId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Bad requirement {id} in resource path \'rest/offers/requirement/{id}\'. " +
                "Must be Long type");
        }
        if (parsedId < 1L) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Bad requirement {id} in resource path \'rest/offers/requirement/{id}\'. " +
                "Id must be more than 0L");
        }
        List<Offer> offers = offerRepository.findOffersByRequirementIdQuery(parsedId);
        return new ResponseEntity<>(offers, HttpStatus.OK);
    }

    @GetMapping("/company/{id}")
    public ResponseEntity<List<Offer>> getOfferByCompanyId(@PathVariable String id) {
        Long parsedId;
        try {
            parsedId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Bad company {id} in resource path \'rest/offers/company/{id}\'. " +
                "Must be Long type");
        }
        if (parsedId < 1L) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Bad company {id} in resource path \'rest/offers/company/{id}\'. " +
                "Id must be more than 0L");
        }
        List<Offer> offers = offerRepository.findOffersByCompanyIdQuery(parsedId);
        return new ResponseEntity<>(offers, HttpStatus.OK);
    }

    @Transactional(propagation = Propagation.REQUIRED, timeout = 3, rollbackFor = Exception.class)
    @PostMapping
    public ResponseEntity<Offer> createOffer(@Valid @RequestBody OfferCreateRequest request, BindingResult result)
            throws Exception {
        if (result.hasErrors()) {
            throw new IllegalRequestException("Poor information in request body to create offer", result);
        }
        Offer offer;
        try {
            offer = conversionService.convert(request, Offer.class);
        } catch (Exception e) {
            throw new ConvertRequestToEntityException("Can not convert create request to offer, because of: " +
                    e.getCause());
        }
        if (offer == null) {
            throw new NullPointerException("Offer has not created, check request body");
        }
        try {
            offer = offerRepository.save(offer);
        } catch (Exception e) {
            throw new EntityNotCreatedOrNotUpdatedException("Offer has not created and saved to DB, because of: " + e.getCause());
        }
        return new ResponseEntity<>(offer, HttpStatus.CREATED);
    }

    @Transactional(propagation = Propagation.REQUIRED, timeout = 3, rollbackFor = Exception.class)
    @PatchMapping("/company")
    public ResponseEntity<Offer> updateOfferByCompany(@Valid @RequestBody OfferUpdateRequestCompany request, BindingResult result)
            throws Exception {
        if (result.hasErrors()) {
            throw new IllegalRequestException("Poor information in request body to update offer", result);
        }
        Offer offer;
        try {
            offer = conversionService.convert(request, Offer.class);
        } catch (Exception e) {
            throw new ConvertRequestToEntityException("Can not convert update request to offer, because of: " +
                    e.getCause());
        }
        try {
            offer = offerRepository.save(offer);
        } catch (Exception e) {
            throw new EntityNotCreatedOrNotUpdatedException
                    ("Offer has not been updated and saved to DB, " + e.getCause());
        }
        return new ResponseEntity<>(offer, HttpStatus.OK);
    }

    @Transactional(propagation = Propagation.REQUIRED, timeout = 3, rollbackFor = Exception.class)
    @PatchMapping("/employee")
    public ResponseEntity<Offer> updateOfferByEmployee(@Valid @RequestBody OfferUpdateRequestEmployee request, BindingResult result)
            throws Exception {
        if (result.hasErrors()) {
            throw new IllegalRequestException("Poor information in request body to update offer by employee", result);
        }
        Offer offer;
        try {
            offer = conversionService.convert(request, Offer.class);
        } catch (Exception e) {
            throw new ConvertRequestToEntityException("Can not convert update request by employee to offer, because of: " +
                    e.getCause());
        }
        try {
            offer = offerRepository.save(offer);
        } catch (Exception e) {
            throw new EntityNotCreatedOrNotUpdatedException
                    ("Offer has not been updated and saved to DB, " + e.getCause());
        }
        return new ResponseEntity<>(offer, HttpStatus.OK);
    }

    @Transactional(propagation = Propagation.REQUIRED, timeout = 3, rollbackFor = Exception.class)
    @DeleteMapping("/{id}")
    public ResponseEntity<Offer> deleteOffer(@PathVariable String id) throws Exception {
        Long parsedId;
        try {
            parsedId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Bad information about offer id in resource \'/rest/offers/{id}\'. " +
                    "Must be Long type");
        }
        Optional<Offer> optionalOffer;
        try {
            optionalOffer = offerRepository.findById(parsedId);
        } catch (Exception e) {
            throw new EntityNotFoundException("Can not get offer to be deleted from DB, " + e.getCause());
        }
        Offer offer = optionalOffer.orElseThrow(() -> new NoSuchElementException("No offer with such id"));
        try {
            offerRepository.delete(offer);
        } catch (Exception e) {
            throw new EntityNotDeletedException("Offer has not been deleted. " + e.getCause());
        }
        return new ResponseEntity<>(offer, HttpStatus.OK);
    }
}
