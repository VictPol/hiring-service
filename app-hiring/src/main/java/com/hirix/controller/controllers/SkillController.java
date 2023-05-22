package com.hirix.controller.controllers;


import com.hirix.controller.requests.create.SkillCreateRequest;
import com.hirix.controller.requests.patch.SkillPatchRequest;
import com.hirix.controller.requests.search.SkillSearchCriteria;
import com.hirix.controller.requests.update.SkillUpdateRequest;
import com.hirix.domain.LinkSkillsLocations;
import com.hirix.domain.Location;
import com.hirix.domain.Requirement;
import com.hirix.domain.Skill;
import com.hirix.exception.ConvertRequestToEntityException;
import com.hirix.exception.EntityNotCreatedOrNotUpdatedException;
import com.hirix.exception.EntityNotDeletedException;
import com.hirix.exception.EntityNotFoundException;
import com.hirix.exception.IllegalRequestException;
import com.hirix.exception.PoorInfoInRequestToCreateUpdateEntity;
import com.hirix.repository.LinkSkillsLocationsRepository;
import com.hirix.repository.LocationRepository;
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
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("rest/skills")
@RequiredArgsConstructor
public class SkillController {

    private final SkillRepository skillRepository;
    private final ConversionService conversionService;
    private final RequirementRepository requirementRepository;
    private final LocationRepository locationRepository;
    private final LinkSkillsLocationsRepository linkSkillsLocationsRepository;

    @GetMapping
    public ResponseEntity<List<Skill>> getAllSkills() {
        List<Skill> skills;
        try {
            skills = skillRepository.findAll();
        } catch (Exception e) {
            throw new EntityNotFoundException("Can not get skills from required resource \'rest/skills\'. " +
                    e.getCause());
        }
        return new ResponseEntity<>(skills, HttpStatus.OK);
    }

    @GetMapping("/page_one_skill/{page}")
    public ResponseEntity<Map<String, Page<Skill>>> findAllShowPageWithOneSkill(@PathVariable String page) {
        Integer parsedPage;
        try {
            parsedPage = Integer.parseInt(page);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Bad {page} in resource path \'/rest/skills/page_one_skill/{page}\'. " +
                    "Must be Integer type");
        }
        if (parsedPage < 0) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Bad {page} in resource path \'/rest/skills/page_one_skill/{page}\'. " +
                    "Id must be not less than 0L");
        }
        Page<Skill> skills;
        try {
            skills = skillRepository.findAll(PageRequest.of(parsedPage, 1, Sort.by("salaryMin").ascending()));
        } catch (Exception e) {
            throw new EntityNotFoundException
                    ("Can not get skills from required resource \'/rest/skills/page_one_skill/page/{page}/{size}\', " + e.getCause());
        }
        return new ResponseEntity<>(Collections.singletonMap("page #" + parsedPage, skills), HttpStatus.OK);
    }

    @GetMapping("/page_size_skills/{page}/{size}")
    public ResponseEntity<Map<String, Page<Skill>>> findAllShowPageBySize(@PathVariable String page, @PathVariable String size) {
        Integer parsedPage;
        try {
            parsedPage = Integer.parseInt(page);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Bad {page} in resource path \'/rest/skills/page_size_skills/{page}/{size}\'. " +
                    "Must be Integer type");
        }
        if (parsedPage < 0) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Bad {page} in resource path \'/rest/skills/page_size_skills/{page}/{size}\'. " +
                    "Id must be not less than 0L");
        }
        Integer parsedSize;
        try {
            parsedSize = Integer.parseInt(size);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Bad {size} in resource path \'/rest/skills/page_size_skills/{page}/{size}\'. " +
                    "Must be Integer type");
        }
        if (parsedSize < 1) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Bad {size} in resource path \'/rest/skills/page_size_skills/{page}/{size}\'. " +
                    "Id must be more than 0L");
        }
        Page<Skill> skills;
        try {
            skills = skillRepository.findAll(PageRequest.of(parsedPage, parsedSize, Sort.by("salaryMin").ascending()));
        } catch (Exception e) {
            throw new EntityNotFoundException
                    ("Can not get skills from required resource \'/rest/skills/page_size_skills/{page}/{size}\', " + e.getCause());
        }
        return new ResponseEntity<>(Collections.singletonMap("page #" + parsedPage, skills), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Skill> getSkillById(@PathVariable String id) {
        Long parsedId;
        try {
            parsedId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Bad skill {id} in resource path \'/rest/skills/{id}\'. Must be Long type");
        }
        if (parsedId < 1L) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Bad skill {id} in resource path \'/rest/skills/{id}\'. " +
                    "Id must be more than 0L");
        }
        Optional<Skill> optionalSkill;
        try {
            optionalSkill = skillRepository.findById(parsedId);
        } catch (Exception e) {
            throw new EntityNotFoundException
                    ("Can not get skill by id from from required resource \'/rest/skills/{id}\'. " + e.getCause());
        }
        Skill skill = optionalSkill.orElseThrow(() -> new NoSuchElementException("No skill with such id"));
        return new ResponseEntity<>(skill, HttpStatus.OK);
    }

    @GetMapping("/requirement/{id}")
    public ResponseEntity<List<Skill>> getSkillsMatchingToRequirementId(@PathVariable String id) {
        Long parsedId;
        try {
            parsedId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Bad requirement {id} in resource path \'/skills/requirement/{id}\'. " +
                    "Must be Long type");
        }
        if (parsedId < 1L) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Bad requirement {id} in resource path \'/rest/companies/{id}\'. " +
                    "Id must be more than 0L");
        }
        Optional<Requirement> optionalRequirement;
        try {
            optionalRequirement = requirementRepository.findById(parsedId);
        } catch (Exception e) {
            throw new EntityNotFoundException
                    ("Can not get requirement by id from from required resource \'/skills/requirement/{id}\', " + e.getCause());
        }
        Requirement req = optionalRequirement.orElseThrow(() -> new NoSuchElementException("No requirement with such id"));
        List<Skill> skills = findSkillsByRequirementIdQuery(req);
        return new ResponseEntity<>(skills, HttpStatus.OK);
    }

    @GetMapping("/requirement/{id}/salary_min")
    public ResponseEntity<List<Skill>> getSkillsMatchingToRequirementIdWithMinSalary(@PathVariable String id) {
        Long parsedId;
        try {
            parsedId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Bad requirement {id} in resource path \'/skills/requirement/salary_min/{id}\'. " +
                    "Must be Long type");
        }
        if (parsedId < 1L) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Bad requirement {id} in resource path \'/skills/requirement/salary_min/{id}\'. " +
                    "Id must be more than 0L");
        }
        Optional<Requirement> optionalRequirement;
        try {
            optionalRequirement = requirementRepository.findById(parsedId);
        } catch (Exception e) {
            throw new EntityNotFoundException
                    ("Can not get requirement by id from from required resource \'/skills/requirement/salary_min/{id}\', " + e.getCause());
        }
        Requirement req = optionalRequirement.orElseThrow(() -> new NoSuchElementException("No requirement with such id"));
        List<Skill> skills = findSkillsByRequirementIdWithMinSalaryQuery(req);
        return new ResponseEntity<>(skills, HttpStatus.OK);
    }

    @GetMapping("/requirement/{id}/equipment/{equipment}")
    public ResponseEntity<List<Skill>> getSkillsMatchingToRequirementIdAndEquipmentLike(@PathVariable String id,
                                                                                        @PathVariable String equipment) {
        Long parsedId;
        try {
            parsedId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Bad requirement {id} in resource path \'/requirement/{id}/equipment/{equipment}\'. " +
                    "Must be Long type");
        }
        if (parsedId < 1L) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Bad requirement {id} in resource path \'/requirement/{id}/equipment/{equipment}\'. " +
                    "Id must be more than 0L");
        }
        Optional<Requirement> optionalRequirement;
        try {
            optionalRequirement = requirementRepository.findById(parsedId);
        } catch (Exception e) {
            throw new EntityNotFoundException
                    ("Can not get requirement by id from from required resource \'/requirement/{id}/equipment/{equipment}\', ", e.getCause());
        }
        Requirement req = optionalRequirement.orElseThrow(() -> new NoSuchElementException("No requirement with such id"));
        List<Skill> skills = findSkillsByRequirementIdQueryAndEquipmentLike(req, equipment);
        return new ResponseEntity<>(skills, HttpStatus.OK);
    }

    @GetMapping("/requirement/{id}/employee_location/{location_id}")
    public ResponseEntity<List<Skill>> getSkillsMatchingToRequirementIdAndEmployeeLocationId
            (@PathVariable String id,
             @PathVariable(name = "location_id") String locationId) {
        Long parsedLocationId;
        try {
            parsedLocationId = Long.parseLong(locationId);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Bad employee_location {locationId} in resource path \'rest/skills/requirement/{id}/employee_location/{location_id}\'. " +
                    "Must be Long type");
        }
        if (parsedLocationId < 1L) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Bad employee_location {locationId} in resource path \'rest/skills/requirement/{id}/employee_location/{location_id}\'. " +
                    "Id must be more than 0L");
        }
        Long parsedId;
        try {
            parsedId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Bad requirement {id} in resource path \'/requirement/{id}/employee_location/{location_id}\'. " +
                    "Must be Long type");
        }
        if (parsedId < 1L) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Bad requirement {id} in resource path \'/skills/requirement/{id}/{equipment}\'. " +
                    "Id must be more than 0L");
        }
        Optional<Requirement> optionalRequirement;
        try {
            optionalRequirement = requirementRepository.findById(parsedId);
        } catch (Exception e) {
            throw new EntityNotFoundException
                    ("Can not get requirement by id from from required resource \'/requirement/{id}/employee_location/{location_id}\', ", e.getCause());
        }
        Requirement req = optionalRequirement.orElseThrow(() -> new NoSuchElementException("No requirement with such id"));
        List<Skill> skills = findSkillsByRequirementIdQueryAndEmployeeLocationId(req, parsedLocationId);
        return new ResponseEntity<>(skills, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<Map<String, List<Skill>>> searchSkillsByEquipmentsLike
            (@Valid @ModelAttribute SkillSearchCriteria criteria, BindingResult result) {
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
        List<Skill> skills;
        try {
            skills = skillRepository.findSkillsByEquipmentsLike("%" + criteria.getQuery() + "%");
        } catch (Exception e) {
            throw new EntityNotFoundException
                    ("Can not search skills from required resource \'/rest/skills/search?query=\'" +
                            criteria.getQuery() + ". " + e.getCause());
        }
        return new ResponseEntity<>(Collections.singletonMap("skills", skills), HttpStatus.OK);
    }

    @Transactional(propagation = Propagation.REQUIRED, timeout = 3, rollbackFor = Exception.class)
    @PostMapping
    public ResponseEntity<Skill> createSkill(@Valid @RequestBody SkillCreateRequest request, BindingResult result)
            throws Exception {
        if (result.hasErrors()) {
            throw new IllegalRequestException("Poor information in request body to create skill", result);
        }
        Skill skill;
        try {
            skill = conversionService.convert(request, Skill.class);
        } catch (Exception e) {
            throw new ConvertRequestToEntityException("Can not convert create request to skill, because of: " +
                    e.getCause());
        }
        if (skill == null) {
            throw new NullPointerException("Skill has not created, check request body");
        }
        try {
            skill = skillRepository.save(skill);
        } catch (Exception e) {
            throw new EntityNotCreatedOrNotUpdatedException("Skill has not created and saved to DB, because of: " + e.getCause());
        }
        return new ResponseEntity<>(skill, HttpStatus.CREATED);
    }

    @Transactional(propagation = Propagation.REQUIRED, timeout = 3, rollbackFor = Exception.class)
    @PutMapping
    public ResponseEntity<Skill> updateSkill(@Valid @RequestBody SkillUpdateRequest request, BindingResult result)
            throws Exception {
        if (result.hasErrors()) {
            throw new IllegalRequestException("Poor information in request body to update skill", result);
        }
        Skill skill;
        try {
            skill = conversionService.convert(request, Skill.class);
        } catch (Exception e) {
            throw new ConvertRequestToEntityException("Can not convert update request to skill, because of: " +
                    e.getCause());
        }
        try {
            skill = skillRepository.save(skill);
        } catch (Exception e) {
            throw new EntityNotCreatedOrNotUpdatedException
                    ("Skill has not been updated and saved to DB, " + e.getCause());
        }
        return new ResponseEntity<>(skill, HttpStatus.OK);
    }

    @Transactional(propagation = Propagation.REQUIRED, timeout = 3, rollbackFor = Exception.class)
    @PatchMapping
    public ResponseEntity<Skill> patchUpdateSkill(@Valid @RequestBody SkillPatchRequest request, BindingResult result)
            throws Exception {
        if (result.hasErrors()) {
            throw new IllegalRequestException("Poor information in request body to patch update skill", result);
        }
        Skill skill;
        try {
            skill = conversionService.convert(request, Skill.class);
        } catch (Exception e) {
            throw new ConvertRequestToEntityException("Can not convert patch request to skill. " + e.getCause());
        }
        try {
            skill = skillRepository.save(skill);
        } catch (Exception e) {
            throw new EntityNotCreatedOrNotUpdatedException
                    ("Skill has not been patch updated and saved to DB. " + e.getCause());
        }
        return new ResponseEntity<>(skill, HttpStatus.OK);
    }

    @Transactional(propagation = Propagation.REQUIRED, timeout = 3, rollbackFor = Exception.class)
    @PostMapping("/add_location/{skill_id}/{location_id}")
    public ResponseEntity<LinkSkillsLocations> addLocationToSkill(@PathVariable(name = "skill_id") String skillId,
                                                                  @PathVariable(name = "location_id") String locationId)
            throws Exception {
        Long parsedSkillId;
        try {
            parsedSkillId = Long.parseLong(skillId);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Bad information about skill id in resource \'/add_location/{skill_id}/{location_id}\'. " +
                    "Must be Long type");
        }
        Optional<Skill> optionalSkill;
        try {
            optionalSkill = skillRepository.findById(parsedSkillId);
        } catch (Exception e) {
            throw new EntityNotFoundException("Can not get skill to add location to skill from DB. " + e.getCause());
        }
        Skill skill = optionalSkill.orElseThrow(() -> new NoSuchElementException("No skill with such id"));
        Long parsedlocationId;
        try {
            parsedlocationId = Long.parseLong(locationId);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Bad information about location id in resource \'/add_location/{skill_id}/{location_id}\'. " +
                    "Must be Long type");
        }
        Optional<Location> optionalLocation;
        try {
            optionalLocation = locationRepository.findById(parsedlocationId);
        } catch (Exception e) {
            throw new EntityNotFoundException("Can not get location to add location to skill from DB. " + e.getCause());
        }
        Location location = optionalLocation.orElseThrow(() -> new NoSuchElementException("No location with such id"));
        LinkSkillsLocations link = new LinkSkillsLocations();
        try {
            link.setSkillId(skill.getId());
            link.setLocationId(location.getId());
            link.setCreated(Timestamp.valueOf(LocalDateTime.now()));
            link.setChanged(Timestamp.valueOf(LocalDateTime.now()));
        } catch (Exception e) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Poor information in resource to add location to skill. " +
                    e.getCause());
        }
        try {
            link = linkSkillsLocationsRepository.save(link);
        } catch (Exception e) {
            throw new EntityNotCreatedOrNotUpdatedException("New location has not been added to skill. " + e.getCause());
        }
        return new ResponseEntity<>(link, HttpStatus.CREATED);
    }

    @Transactional(propagation = Propagation.REQUIRED, timeout = 3, rollbackFor = Exception.class)
    @DeleteMapping("/{id}")
    public ResponseEntity<Skill> deleteSkill(@PathVariable String id) throws Exception {
        Long parsedId;
        try {
            parsedId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Bad information about skill id in resource \'/rest/skills/{id}\'. " +
                    "Must be Long type");
        }
        Optional<Skill> optionalSkill;
        try {
            optionalSkill = skillRepository.findById(parsedId);
        } catch (Exception e) {
            throw new EntityNotFoundException("Can not get skill to be deleted from DB, " + e.getCause());
        }
        Skill skill = optionalSkill.orElseThrow(() -> new NoSuchElementException("No skill with such id"));
        try {
            skillRepository.delete(skill);
        } catch (Exception e) {
            throw new EntityNotDeletedException("Skill has not been deleted. " + e.getCause());
        }
        return new ResponseEntity<>(skill, HttpStatus.OK);
    }

    private List<Skill> findSkillsByRequirementIdQuery(Requirement req) {
        List<Skill> skills;
        try {
            skills = skillRepository.findSkillsByRequirementId(req.getExperience(), req.isActive(),
                    req.getRecommendations(), req.getSalary(), req.getTerm(), req.getIndustry(), req.getProfession(),
                    req.getSpecialization(), req.getRank(), req.getPosition(), req.getLocationOffered().getId());
        } catch (Exception e) {
            throw new EntityNotFoundException
                    ("Can not find skills by requirement from required resource \'/skills/requirement/{id}\', " + e.getCause());
        }
        return skills;
    }

    private List<Skill> findSkillsByRequirementIdWithMinSalaryQuery(Requirement req) {
        List<Skill> skills;
        try {
            skills = skillRepository.findSkillsByRequirementIdWithMinSalary(req.getExperience(), req.isActive(),
                    req.getRecommendations(), req.getSalary(), req.getTerm(), req.getIndustry().getId(), req.getProfession().getId(),
                    req.getSpecialization().getId(), req.getRank().getId(), req.getPosition().getId(), req.getLocationOffered().getId());
        } catch (Exception e) {
            throw new EntityNotFoundException
                    ("Can not find skills by requirement from required resource \'/skills/requirement/{id}\', " + e.getCause());
        }
        return skills;
    }


    private List<Skill> findSkillsByRequirementIdQueryAndEquipmentLike(Requirement req, String equipment) {
        List<Skill> skills;
        try {
            skills = skillRepository.findSkillsByRequirementIdAndEquipmentLike(req.getExperience(), req.isActive(),
                    req.getRecommendations(), req.getSalary(), req.getTerm(), req.getIndustry(), req.getProfession(),
                    req.getSpecialization(), req.getRank(), req.getPosition(), req.getLocationOffered().getId(),
                    "%" + equipment.toLowerCase() + "%");
        } catch (Exception e) {
            throw new EntityNotFoundException
                    ("Can not find skills by requirement and equipment like from required resource \'/requirement/{id}/" +
                            "equipment/{equipment}\', " + e.getCause());
        }
        return skills;
    }

    private List<Skill> findSkillsByRequirementIdQueryAndEmployeeLocationId(Requirement req, Long employeeLocationId) {
        List<Skill> skills;
        try {
            skills = skillRepository.findSkillsByRequirementIdAndEmployeeLocationId(req.getExperience(), req.isActive(),
                    req.getRecommendations(), req.getSalary(), req.getTerm(), req.getIndustry().getId(), req.getProfession().getId(),
                    req.getSpecialization().getId(), req.getRank().getId(), req.getPosition().getId(), req.getLocationOffered().getId(),
                    employeeLocationId);
        } catch (Exception e) {
            throw new EntityNotFoundException
                    ("Can not find skills by requirement from required resource \'/requirement/{id}/employee_location/{location_id}\', " + e.getCause());
        }
        return skills;
    }
}
