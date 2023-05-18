package com.hirix.controller.controllers;

import com.hirix.controller.requests.create.RequirementCreateRequest;
import com.hirix.controller.requests.create.SkillCreateRequest;
import com.hirix.controller.requests.search.RequirementSearchCriteria;
import com.hirix.controller.requests.search.SkillSearchCriteria;
import com.hirix.controller.requests.update.RequirementUpdateRequest;
import com.hirix.domain.Company;
import com.hirix.domain.Employee;
import com.hirix.domain.Industry;
import com.hirix.domain.Location;
import com.hirix.domain.Position;
import com.hirix.domain.Profession;
import com.hirix.domain.Rank;
import com.hirix.domain.Requirement;
import com.hirix.domain.Skill;
import com.hirix.domain.Specialization;
import com.hirix.exception.EntityNotFoundException;
import com.hirix.exception.PoorInfoInRequestToCreateUpdateEntity;
import com.hirix.repository.CompanyRepository;
import com.hirix.repository.EmployeeRepository;
import com.hirix.repository.IndustryRepository;
import com.hirix.repository.LocationRepository;
import com.hirix.repository.PositionRepository;
import com.hirix.repository.ProfessionRepository;
import com.hirix.repository.RankRepository;
import com.hirix.repository.RequirementRepository;
import com.hirix.repository.SkillRepository;
import com.hirix.repository.SpecializationRepository;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("rest/requirements")
@RequiredArgsConstructor
public class RequirementController {
    private final RequirementRepository requirementRepository;
    private final SkillRepository skillRepository;
    private final CompanyRepository companyRepository;
    private final IndustryRepository industryRepository;
    private final ProfessionRepository professionRepository;
    private final SpecializationRepository specializationRepository;
    private final RankRepository rankRepository;
    private final PositionRepository positionRepository;
    private final LocationRepository locationRepository;

    @GetMapping
    public ResponseEntity<List<Requirement>> getAllRequirements() {
        List<Requirement> requirements = requirementRepository.findAll();
        return new ResponseEntity<>(requirements, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Requirement> getRequirementById(@PathVariable String id) {
        Long parsedId = Long.parseLong(id);
        Optional<Requirement> requirement = requirementRepository.findById(parsedId);
        return new ResponseEntity<>(requirement.get(), HttpStatus.OK);
    }

    @GetMapping("/skill/{id}")
    public ResponseEntity<List<Requirement>> getRequirementsMatchingToSkillId(@PathVariable String id) {
        Long parsedId;
        try {
            parsedId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Bad requirement {id} in resource path \'rest/requirements/skill/{id}\'. " +
                "Must be Long type");
        }
        if (parsedId < 1L) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Bad requirement {id} in resource path \'rest/requirements/skill/{id}\'. " +
                "Id must be more than 0L");
        }
        Optional<Skill> optionalSkill;
        try {
            optionalSkill = skillRepository.findById(parsedId);
        }  catch (Exception e) {
            throw new EntityNotFoundException
                ("Can not get skill by id from from required resource \'rest/requirements/skill/{id}\', " + e.getCause());
        }
        Skill skill = optionalSkill.orElseThrow(() -> new NoSuchElementException("No skill with such id"));
        List<Requirement> requirements = findRequirementsBySkillIdQuery(skill);
        return new ResponseEntity<>(requirements, HttpStatus.OK);
    }

    @GetMapping("/skill/{id}/equipment/{equipment}")
    public ResponseEntity<List<Requirement>> getRequirementsMatchingToSkillIdAndEquipmentLike(@PathVariable String id,
                                                                                              @PathVariable String equipment) {
        Long parsedId;
        try {
            parsedId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Bad requirement {id} in resource path \'rest/requirements/skill/{id}/equipment/{equipment}\'. " +
                    "Must be Long type");
        }
        if (parsedId < 1L) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Bad requirement {id} in resource path \'rest/requirements/skill/{id}/equipment/{equipment}\'. " +
                    "Id must be more than 0L");
        }
        Optional<Skill> optionalSkill;
        try {
            optionalSkill = skillRepository.findById(parsedId);
        }  catch (Exception e) {
            throw new EntityNotFoundException
                    ("Can not get skill by id from from required resource \'rest/requirements/skill/{id}/equipment/{equipment}\', " + e.getCause());
        }
        Skill skill = optionalSkill.orElseThrow(() -> new NoSuchElementException("No skill with such id"));
        List<Requirement> requirements = findRequirementsBySkillIdAndEquipmentLikeQuery(skill, equipment);
        return new ResponseEntity<>(requirements, HttpStatus.OK);
    }

    @GetMapping("/skill/{id}/company_location/{location_id}")
    public ResponseEntity<List<Requirement>> getRequirementsMatchingToSkillIdAndCompanyLocationId
            (@PathVariable String id,
             @PathVariable (name = "location_id") String locationId) {
        Long parsedLocationId;
        try {
            parsedLocationId = Long.parseLong(locationId);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Bad requirement {locationId} in resource path \'rest/requirements/skill/{id}/company_location/{location_id}\'. " +
                "Must be Long type");
        }
        if (parsedLocationId < 1L) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Bad requirement {locationId} in resource path \'rest/requirements/skill/{id}/company_location/{location_id}\'. " +
                "Id must be more than 0L");
        }
        Long parsedId;
        try {
            parsedId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Bad requirement {id} in resource path \'rest/requirements/skill/{id}/company_location/{location_id}\'. " +
                "Must be Long type");
        }
        if (parsedId < 1L) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Bad requirement {id} in resource path \'rest/requirements/skill/{id}/company_location/{location_id}\'. " +
                "Id must be more than 0L");
        }
        Optional<Skill> optionalSkill;
        try {
            optionalSkill = skillRepository.findById(parsedId);
        }  catch (Exception e) {
            throw new EntityNotFoundException
                    ("Can not get skill by id from from required resource \'rest/requirements/skill/{id}\', " + e.getCause());
        }
        Skill skill = optionalSkill.orElseThrow(() -> new NoSuchElementException("No skill with such id"));
        List<Requirement> requirements = findRequirementsBySkillIdAndCompanyLocationIdQuery(skill, parsedLocationId);
        return new ResponseEntity<>(requirements, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<Map<String, List<Requirement>>> searchRequirementsByEquipmentsLike
            (@ModelAttribute RequirementSearchCriteria criteria) {
        List<Requirement> requirements = requirementRepository.findRequirementsByEquipmentsLike("%" + criteria.getQuery() + "%");
        return new ResponseEntity<>(Collections.singletonMap("requirements", requirements), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Requirement> createRequirement(@RequestBody RequirementCreateRequest request) {
//        if (result.hasErrors()) {
//            throw new IllegalRequestException(result);
//        }
        Requirement requirement = new Requirement();
        requirement.setExperience(request.getExperience());
        requirement.setActive(request.isActive());
        requirement.setRecommendations(request.getRecommendations());
        requirement.setEquipments(request.getEquipments());
        requirement.setSalary(request.getSalary());
        requirement.setTerm(request.getTerm());
        requirement.setCreated(Timestamp.valueOf(LocalDateTime.now()));
        requirement.setChanged(Timestamp.valueOf(LocalDateTime.now()));
        Optional<Company> optionalCompany = companyRepository.findById(request.getCompanyId());
        Company company = optionalCompany.get();
        requirement.setCompany(company);
        Optional<Industry> optionalIndustry = industryRepository.findById(request.getIndustryId());
        Industry industry = optionalIndustry.get();
        requirement.setIndustry(industry);
        Optional<Profession> optionalProfession = professionRepository.findById(request.getProfessionId());
        Profession profession = optionalProfession.get();
        requirement.setProfession(profession);
        Optional<Specialization> optionalSpecialization = specializationRepository.findById(request.getSpecializationId());
        Specialization specialization = optionalSpecialization.get();
        requirement.setSpecialization(specialization);
        Optional<Rank> optionalRank = rankRepository.findById(request.getRankId());
        Rank rank = optionalRank.get();
        requirement.setRank(rank);
        Optional<Position> optionalPosition = positionRepository.findById(request.getPositionId());
        Position position = optionalPosition.get();
        requirement.setPosition(position);
        Optional<Location> locationOfferedOptional = locationRepository.findById(request.getLocationOfferedId());
        Location locationOffered = locationOfferedOptional.get();
        requirement.setLocationOffered(locationOffered);
        requirement = requirementRepository.save(requirement);
        return new ResponseEntity<>(requirement, HttpStatus.CREATED);
    }


    @PutMapping
    public ResponseEntity<Requirement> updateRequirement(@RequestBody RequirementUpdateRequest request) {
//        if (result.hasErrors()) {
//            throw new IllegalRequestException(result);
//        }
        Optional<Requirement> optionalRequirement = requirementRepository.findById(request.getId());
        Requirement requirement = optionalRequirement.get();
        requirement.setExperience(request.getExperience());
        requirement.setActive(request.isActive());
        requirement.setRecommendations(request.getRecommendations());
        requirement.setEquipments(request.getEquipments());
        requirement.setSalary(request.getSalary());
        requirement.setTerm(request.getTerm());
//        requirement.setCreated(Timestamp.valueOf(LocalDateTime.now()));
        requirement.setChanged(Timestamp.valueOf(LocalDateTime.now()));
//        Optional<Company> optionalCompany = companyRepository.findById(request.getCompanyId());
//        Company company = optionalCompany.get();
//        requirement.setCompany(company);
        Optional<Industry> optionalIndustry = industryRepository.findById(request.getIndustryId());
        Industry industry = optionalIndustry.get();
        requirement.setIndustry(industry);
        Optional<Profession> optionalProfession = professionRepository.findById(request.getProfessionId());
        Profession profession = optionalProfession.get();
        requirement.setProfession(profession);
        Optional<Specialization> optionalSpecialization = specializationRepository.findById(request.getSpecializationId());
        Specialization specialization = optionalSpecialization.get();
        requirement.setSpecialization(specialization);
        Optional<Rank> optionalRank = rankRepository.findById(request.getRankId());
        Rank rank = optionalRank.get();
        requirement.setRank(rank);
        Optional<Position> optionalPosition = positionRepository.findById(request.getPositionId());
        Position position = optionalPosition.get();
        requirement.setPosition(position);
        Optional<Location> locationOfferedOptional = locationRepository.findById(request.getLocationOfferedId());
        Location locationOffered = locationOfferedOptional.get();
        requirement.setLocationOffered(locationOffered);
        requirement = requirementRepository.save(requirement);
        return new ResponseEntity<>(requirement, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Requirement> deleteRequirement(@PathVariable Long id) {
//        if (result.hasErrors()) {
//            throw new IllegalRequestException(result);
//        }
        Optional<Requirement> optionalRequirement = requirementRepository.findById(id);
        Requirement requirement = optionalRequirement.get();
        requirementRepository.delete(requirement);
        return new ResponseEntity<>(requirement, HttpStatus.OK);
    }


    private List<Requirement> findRequirementsBySkillIdQuery(Skill skill) {
        List<Requirement> requirements;
        try {
            requirements  = requirementRepository.findRequirementsBySkillId(skill.getExperience(), skill.isActive(),
                    skill.getRecommendations(), skill.getSalaryMin(), skill.getSalaryMax(), skill.getTermMin(),
                    skill.getTermMax(), skill.getIndustry(), skill.getProfession(), skill.getSpecialization(),
                    skill.getRank(), skill.getPosition(), skill.getId());
        } catch (Exception e) {
            throw new EntityNotFoundException
                ("Can not find requirements by skill from required resource \'rest/requirements/skill/{id}\', " +
                    e.getCause());
        }
        return requirements;
    }

    private List<Requirement> findRequirementsBySkillIdAndEquipmentLikeQuery(Skill skill, String equipment) {
        List<Requirement> requirements;
        try {
            requirements  = requirementRepository.findRequirementsBySkillIdAndEquipmentLike(skill.getExperience(), skill.isActive(),
                    skill.getRecommendations(), skill.getSalaryMin(), skill.getSalaryMax(), skill.getTermMin(),
                    skill.getTermMax(), skill.getIndustry(), skill.getProfession(), skill.getSpecialization(),
                    skill.getRank(), skill.getPosition(), skill.getId(), "%" + equipment.toLowerCase() + "%");
        } catch (Exception e) {
            throw new EntityNotFoundException
                ("Can not find requirements by skill from required resource \'rest/requirements/skill/{id}/equipment/{equipment}\', " +
                    e.getCause());
        }
        return requirements;
    }

    private List<Requirement> findRequirementsBySkillIdAndCompanyLocationIdQuery(Skill skill, Long companyLocationId) {
        List<Requirement> requirements;
        try {
            requirements  = requirementRepository.findRequirementsBySkillIdAndCompanyLocationId(skill.getExperience(), skill.isActive(),
                    skill.getRecommendations(), skill.getSalaryMin(), skill.getSalaryMax(), skill.getTermMin(),
                    skill.getTermMax(), skill.getIndustry(), skill.getProfession(), skill.getSpecialization(),
                    skill.getRank(), skill.getPosition(), skill.getId(), companyLocationId);
        } catch (Exception e) {
            throw new EntityNotFoundException
                ("Can not find requirements by skill from required resource \'rest/requirements/skill/{id}/company_location/{location_id}\', " +
                    e.getCause());
        }
        return requirements;
    }

}
