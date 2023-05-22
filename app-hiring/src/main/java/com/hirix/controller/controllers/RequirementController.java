package com.hirix.controller.controllers;


import com.hirix.controller.requests.create.RequirementCreateRequest;
import com.hirix.controller.requests.patch.RequirementPatchRequest;
import com.hirix.controller.requests.search.RequirementSearchCriteria;
import com.hirix.controller.requests.update.RequirementUpdateRequest;
import com.hirix.domain.Requirement;
import com.hirix.domain.Skill;
import com.hirix.exception.ConvertRequestToEntityException;
import com.hirix.exception.EntityNotCreatedOrNotUpdatedException;
import com.hirix.exception.EntityNotDeletedException;
import com.hirix.exception.EntityNotFoundException;
import com.hirix.exception.IllegalRequestException;
import com.hirix.exception.PoorInfoInRequestToCreateUpdateEntity;
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
@RequestMapping("rest/requirements")
@RequiredArgsConstructor
public class RequirementController {

    private final RequirementRepository requirementRepository;
    private final ConversionService conversionService;
    private final SkillRepository skillRepository;

    @GetMapping
    public ResponseEntity<List<Requirement>> getAllRequirements() {
        List<Requirement> requirements;
        try {
            requirements = requirementRepository.findAll();
        } catch (Exception e) {
            throw new EntityNotFoundException
                    ("Can not get requirements from from required resource \'rest/requirements\', " + e.getCause());
        }
        return new ResponseEntity<>(requirements, HttpStatus.OK);
    }

    @GetMapping("/page_one_requirement/{page}")
    public ResponseEntity<Map<String, Page<Requirement>>> findAllShowPageWithOneSkill(@PathVariable String page) {
        Integer parsedPage;
        try {
            parsedPage = Integer.parseInt(page);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Bad {page} in resource path \'/rest/requirements/page_one_requirement/{page}\'. " +
                    "Must be Integer type");
        }
        if (parsedPage < 0) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Bad {page} in resource path \'/rest/requirements/page_one_requirement/{page}\'. " +
                    "Id must be not less than 0L");
        }
        Page<Requirement> requirements;
        try {
            requirements = requirementRepository.findAll(PageRequest.of(parsedPage, 1, Sort.by("salary").ascending()));
        } catch (Exception e) {
            throw new EntityNotFoundException
                    ("Can not get requirements from required resource \'/rest/requirements/page_one_requirement/{page}\', " + e.getCause());
        }
        return new ResponseEntity<>(Collections.singletonMap("page #" + parsedPage, requirements), HttpStatus.OK);
    }

    @GetMapping("/page_size_requirements/{page}/{size}")
    public ResponseEntity<Map<String, Page<Requirement>>> findAllShowPageBySize(@PathVariable String page, @PathVariable String size) {
        Integer parsedPage;
        try {
            parsedPage = Integer.parseInt(page);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Bad {page} in resource path \'/rest/requirements/page_size_requirements/{page}/{size}\'. " +
                    "Must be Integer type");
        }
        if (parsedPage < 0) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Bad {page} in resource path \'/rest/requirements/page_size_requirements/{page}/{size}\'. " +
                    "Id must be not less than 0L");
        }
        Integer parsedSize;
        try {
            parsedSize = Integer.parseInt(size);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Bad {size} in resource path \'/rest/requirements/page_size_requirements/{page}/{size}\'. " +
                    "Must be Integer type");
        }
        if (parsedSize < 1) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Bad {size} in resource path \'/rest/requirements/page_size_requirements/{page}/{size}\'. " +
                    "Id must be more than 0L");
        }
        Page<Requirement> requirements;
        try {
            requirements = requirementRepository.findAll(PageRequest.of(parsedPage, parsedSize, Sort.by("salary").ascending()));
        } catch (Exception e) {
            throw new EntityNotFoundException
                    ("Can not get requirements from required resource \'/rest/requirements/page_size_requirements/{page}/{size}\', " + e.getCause());
        }
        return new ResponseEntity<>(Collections.singletonMap("page #" + parsedPage, requirements), HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Requirement> getRequirementById(@PathVariable String id) {
        Long parsedId;
        try {
            parsedId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Bad requirement {id} in resource path \'rest/requirements/{id}\'. " +
                    "Must be Long type");
        }
        if (parsedId < 1L) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Bad requirement {id} in resource path \'rest/requirements/{id}\'. " +
                    "Id must be more than 0L");
        }
        Optional<Requirement> optionalRequirement;
        try {
            optionalRequirement = requirementRepository.findById(parsedId);
        } catch (Exception e) {
            throw new EntityNotFoundException
                    ("Can not get requirement by id from from required resource \'rest/requirements/{id}\', " + e.getCause());
        }
        Requirement requirement = optionalRequirement.orElseThrow(() -> new NoSuchElementException("No requirements with such id"));
        return new ResponseEntity<>(requirement, HttpStatus.OK);
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
        } catch (Exception e) {
            throw new EntityNotFoundException
                    ("Can not get skill by id from from required resource \'rest/requirements/skill/{id}\', " + e.getCause());
        }
        Skill skill = optionalSkill.orElseThrow(() -> new NoSuchElementException("No skill with such id"));
        List<Requirement> requirements = findRequirementsBySkillIdQuery(skill);
        return new ResponseEntity<>(requirements, HttpStatus.OK);
    }

    @GetMapping("/skill/{id}/salary_max")
    public ResponseEntity<List<Requirement>> getRequirementsMatchingToSkillIdAndSalaryMax(@PathVariable String id) {
        Long parsedId;
        try {
            parsedId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Bad requirement {id} in resource path \'rest/requirements/skill/{id}/salary_max\'. " +
                    "Must be Long type");
        }
        if (parsedId < 1L) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Bad requirement {id} in resource path \'rest/requirements/skill/{id}/salary_max\'. " +
                    "Id must be more than 0L");
        }
        Optional<Skill> optionalSkill;
        try {
            optionalSkill = skillRepository.findById(parsedId);
        } catch (Exception e) {
            throw new EntityNotFoundException
                    ("Can not get skill by id from from required resource \'rest/requirements/skill/{id}/salary_max\', " + e.getCause());
        }
        Skill skill = optionalSkill.orElseThrow(() -> new NoSuchElementException("No skill with such id"));
        List<Requirement> requirements = findRequirementsBySkillIdAndSalaryMaxQuery(skill);
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
        } catch (Exception e) {
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
             @PathVariable(name = "location_id") String locationId) {
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
        } catch (Exception e) {
            throw new EntityNotFoundException
                    ("Can not get skill by id from from required resource \'rest/requirements/skill/{id}\', " + e.getCause());
        }
        Skill skill = optionalSkill.orElseThrow(() -> new NoSuchElementException("No skill with such id"));
        List<Requirement> requirements = findRequirementsBySkillIdAndCompanyLocationIdQuery(skill, parsedLocationId);
        return new ResponseEntity<>(requirements, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<Map<String, List<Requirement>>> searchRequirementsByEquipmentsLike
            (@Valid @ModelAttribute RequirementSearchCriteria criteria, BindingResult result) {
        if (result.hasErrors()) {
            throw new IllegalRequestException
                    ("Bad argument in search path, must be: \'search?query=word_like_equipment\'", result);
        }
        String query;
        try {
            query = criteria.getQuery();
        } catch (Exception e) {
            throw new IllegalArgumentException("Can not get query=word_like_equipment from criteria. " + e.getCause());
        }
        if (query == null) {
            throw new IllegalArgumentException
                    ("Bad argument in search path, must be: \'search?query=word_like_equipment\'");
        }
        List<Requirement> requirements;
        try {
            requirements = requirementRepository.findRequirementsByEquipmentsLike("%" + criteria.getQuery() + "%");
        } catch (Exception e) {
            throw new EntityNotFoundException
                    ("Can not search skills from required resource \'/rest/skills/search?query=\'" +
                            criteria.getQuery() + ". " + e.getCause());
        }
        return new ResponseEntity<>(Collections.singletonMap("requirements", requirements), HttpStatus.OK);
    }

    @Transactional(propagation = Propagation.REQUIRED, timeout = 3, rollbackFor = Exception.class)
    @PostMapping
    public ResponseEntity<Requirement> createRequirement(@Valid @RequestBody RequirementCreateRequest request,
                                                         BindingResult result) throws Exception {
        if (result.hasErrors()) {
            throw new IllegalRequestException("Poor information in request body to create requirement", result);
        }
        Requirement requirement;
        try {
            requirement = conversionService.convert(request, Requirement.class);
        } catch (Exception e) {
            throw new ConvertRequestToEntityException("Can not convert create request to requirement, because of: " +
                    e.getCause());
        }
        if (requirement == null) {
            throw new NullPointerException("Requirement has not created, check request body");
        }
        try {
            requirement = requirementRepository.save(requirement);
        } catch (Exception e) {
            throw new EntityNotCreatedOrNotUpdatedException("Requirement has not created and saved to DB, because of: " + e.getCause());
        }
        return new ResponseEntity<>(requirement, HttpStatus.CREATED);
    }

    @Transactional(propagation = Propagation.REQUIRED, timeout = 3, rollbackFor = Exception.class)
    @PutMapping
    public ResponseEntity<Requirement> updateRequirement(@Valid @RequestBody RequirementUpdateRequest request, BindingResult result)
            throws Exception {
        if (result.hasErrors()) {
            throw new IllegalRequestException("Poor information in request body to update requirement", result);
        }
        Requirement requirement;
        try {
            requirement = conversionService.convert(request, Requirement.class);
        } catch (Exception e) {
            throw new ConvertRequestToEntityException("Can not convert update request to requirement, because of: " +
                    e.getCause());
        }
        try {
            requirement = requirementRepository.save(requirement);
        } catch (Exception e) {
            throw new EntityNotCreatedOrNotUpdatedException
                    ("Requirement has not been updated and saved to DB, " + e.getCause());
        }
        return new ResponseEntity<>(requirement, HttpStatus.OK);
    }

    @Transactional(propagation = Propagation.REQUIRED, timeout = 3, rollbackFor = Exception.class)
    @PatchMapping
    public ResponseEntity<Requirement> patchUpdateRequirement(@Valid @RequestBody RequirementPatchRequest request, BindingResult result)
            throws Exception {
        if (result.hasErrors()) {
            throw new IllegalRequestException("Poor information in request body to patch update requirement", result);
        }
        Requirement requirement;
        try {
            requirement = conversionService.convert(request, Requirement.class);
        } catch (Exception e) {
            throw new ConvertRequestToEntityException("Can not convert patch request to requirement. " + e.getCause());
        }
        try {
            requirement = requirementRepository.save(requirement);
        } catch (Exception e) {
            throw new EntityNotCreatedOrNotUpdatedException
                    ("Requirement has not been patch updated and saved to DB. " + e.getCause());
        }
        return new ResponseEntity<>(requirement, HttpStatus.OK);
    }

    @Transactional(propagation = Propagation.REQUIRED, timeout = 3, rollbackFor = Exception.class)
    @DeleteMapping("/{id}")
    public ResponseEntity<Requirement> deleteRequirement(@PathVariable String id) throws Exception {
        Long parsedId;
        try {
            parsedId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Bad information about requirement id in resource \'/rest/requirements/{id}\'. " +
                    "Must be Long type");
        }
        Optional<Requirement> optionalRequirement;
        try {
            optionalRequirement = requirementRepository.findById(parsedId);
        } catch (Exception e) {
            throw new EntityNotFoundException("Can not get requirement to be deleted from DB, " + e.getCause());
        }
        Requirement requirement = optionalRequirement.orElseThrow(() -> new NoSuchElementException("No requirement with such id"));
        try {
            requirementRepository.delete(requirement);
        } catch (Exception e) {
            throw new EntityNotDeletedException("Requirement has not been deleted. " + e.getCause());
        }
        return new ResponseEntity<>(requirement, HttpStatus.OK);
    }

    private List<Requirement> findRequirementsBySkillIdQuery(Skill skill) {
        List<Requirement> requirements;
        try {
            requirements = requirementRepository.findRequirementsBySkillId(skill.getExperience(), skill.isActive(),
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

    private List<Requirement> findRequirementsBySkillIdAndSalaryMaxQuery(Skill skill) {
        List<Requirement> requirements;
        try {
            requirements = requirementRepository.findRequirementsBySkillIdAndSalaryMaxQuery(skill.getExperience(), skill.isActive(),
                    skill.getRecommendations(), skill.getSalaryMin(), skill.getSalaryMax(), skill.getTermMin(),
                    skill.getTermMax(), skill.getIndustry().getId(), skill.getProfession().getId(), skill.getSpecialization().getId(),
                    skill.getRank().getId(), skill.getPosition().getId(), skill.getId());
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
            requirements = requirementRepository.findRequirementsBySkillIdAndEquipmentLike(skill.getExperience(), skill.isActive(),
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
            requirements = requirementRepository.findRequirementsBySkillIdAndCompanyLocationId(skill.getExperience(), skill.isActive(),
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
