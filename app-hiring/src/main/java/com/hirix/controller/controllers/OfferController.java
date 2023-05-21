package com.hirix.controller.controllers;

import com.hirix.controller.requests.create.OfferCreateRequest;
import com.hirix.controller.requests.create.UserCreateRequest;
import com.hirix.controller.requests.update.OfferUpdateRequestCompany;
import com.hirix.controller.requests.update.OfferUpdateRequestEmployee;
import com.hirix.domain.Offer;
import com.hirix.domain.Requirement;
import com.hirix.domain.Skill;
import com.hirix.domain.User;
import com.hirix.exception.EntityNotFoundException;
import com.hirix.exception.PoorInfoInRequestToCreateUpdateEntity;
import com.hirix.repository.OfferRepository;
import com.hirix.repository.RequirementRepository;
import com.hirix.repository.SkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("rest/offers")
@RequiredArgsConstructor
public class OfferController {
    private final OfferRepository offerRepository;
    private final SkillRepository skillRepository;
    private final RequirementRepository requirementRepository;

    @GetMapping
    public ResponseEntity<List<Offer>> getAllOffers() {
        List<Offer> offers = offerRepository.findAll();
        return new ResponseEntity<>(offers, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Offer> getOfferById(@PathVariable String id) {
        Long parsedId = Long.parseLong(id);
        Optional<Offer> offer = offerRepository.findById(parsedId);
        return new ResponseEntity<>(offer.get(), HttpStatus.OK);
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

    @PostMapping
    public ResponseEntity<Offer> createOffer(@RequestBody OfferCreateRequest request) {
//        if (result.hasErrors()) {
//            throw new IllegalRequestException(result);
//        }
        Offer offer = new Offer();
        offer.setCommentsCompany(request.getCommentsCompany());
        Optional<Skill> optionalSkill = skillRepository.findById(request.getSkillId());
        Skill skill = optionalSkill.get();
        offer.setSkill(skill);
        Optional<Requirement> optionalRequirement = requirementRepository.findById(request.getRequirementId());
        Requirement requirement = optionalRequirement.get();
        offer.setRequirement(requirement);
        offer.setCreated(Timestamp.valueOf(LocalDateTime.now()));
        offer.setChanged(Timestamp.valueOf(LocalDateTime.now()));
        offer = offerRepository.save(offer);
        return new ResponseEntity<>(offer, HttpStatus.CREATED);
    }

    @PutMapping("/company")
    public ResponseEntity<Offer> updateOfferByCompany(@RequestBody OfferUpdateRequestCompany request) {
//        if (result.hasErrors()) {
//            throw new IllegalRequestException(result);
//        }
        Optional<Offer> optionalOffer = offerRepository.findById(request.getId());
        Offer offer = optionalOffer.get();
        offer.setContracted(request.isContracted());
        offer.setCommentsCompany(request.getCommentsCompany());
//        Optional<Skill> optionalSkill = skillRepository.findById(request.getSkillId());
//        Skill skill = optionalSkill.get();
//        offer.setSkill(skill);
//        Optional<Requirement> optionalRequirement = requirementRepository.findById(request.getRequirementId());
//        Requirement requirement = optionalRequirement.get();
//        offer.setRequirement(requirement);
//        offer.setCreated(Timestamp.valueOf(LocalDateTime.now()));
        offer.setChanged(Timestamp.valueOf(LocalDateTime.now()));
        offer = offerRepository.save(offer);
        return new ResponseEntity<>(offer, HttpStatus.OK);
    }

    @PutMapping("/employee")
    public ResponseEntity<Offer> updateOfferByEmployee(@RequestBody OfferUpdateRequestEmployee request) {
//        if (result.hasErrors()) {
//            throw new IllegalRequestException(result);
//        }
        Optional<Offer> optionalOffer = offerRepository.findById(request.getId());
        Offer offer = optionalOffer.get();
        offer.setAccepted(request.isAccepted());
        offer.setCommentsEmployee(request.getCommentsEmployee());
//        Optional<Skill> optionalSkill = skillRepository.findById(request.getSkillId());
//        Skill skill = optionalSkill.get();
//        offer.setSkill(skill);
//        Optional<Requirement> optionalRequirement = requirementRepository.findById(request.getRequirementId());
//        Requirement requirement = optionalRequirement.get();
//        offer.setRequirement(requirement);
//        offer.setCreated(Timestamp.valueOf(LocalDateTime.now()));
        offer.setChanged(Timestamp.valueOf(LocalDateTime.now()));
        offer = offerRepository.save(offer);
        return new ResponseEntity<>(offer, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Offer> deleteOffer(@PathVariable Long id) {
//        if (result.hasErrors()) {
//            throw new IllegalRequestException(result);
//        }
        Optional<Offer> optionalOffer = offerRepository.findById(id);
        Offer offer = optionalOffer.get();
        offerRepository.delete(offer);
        return new ResponseEntity<>(offer, HttpStatus.OK);
    }

}
