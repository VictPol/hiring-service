package com.hirix.controller.controllers;

import com.hirix.controller.requests.create.OfferCreateRequest;
import com.hirix.controller.requests.create.UserCreateRequest;
import com.hirix.controller.requests.update.OfferUpdateRequestCompany;
import com.hirix.controller.requests.update.OfferUpdateRequestEmployee;
import com.hirix.domain.Offer;
import com.hirix.domain.Requirement;
import com.hirix.domain.Skill;
import com.hirix.domain.User;
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
import java.util.Optional;

@RestController
@RequestMapping("/offers")
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
        Optional<Requirement> optionalRequirement = requirementRepository.findById(request.getRequirementId());
        Requirement requirement = optionalRequirement.get();
        offer.setRequirement(requirement);
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
