package com.hirix.controller.controllers;

import com.hirix.controller.requests.create.CompanyCreateRequest;
import com.hirix.controller.requests.create.SkillCreateRequest;
import com.hirix.controller.requests.search.CompanySearchCriteria;
import com.hirix.controller.requests.search.SkillSearchCriteria;
import com.hirix.controller.requests.update.SkillUpdateRequest;
import com.hirix.domain.Company;
import com.hirix.domain.Employee;
import com.hirix.domain.Industry;
import com.hirix.domain.LinkSkillsLocations;
import com.hirix.domain.LinkUsersRoles;
import com.hirix.domain.Location;
import com.hirix.domain.Position;
import com.hirix.domain.Profession;
import com.hirix.domain.Rank;
import com.hirix.domain.Skill;
import com.hirix.domain.Specialization;
import com.hirix.domain.User;
import com.hirix.repository.EmployeeRepository;
import com.hirix.repository.IndustryRepository;
import com.hirix.repository.LinkSkillsLocationsRepository;
import com.hirix.repository.PositionRepository;
import com.hirix.repository.ProfessionRepository;
import com.hirix.repository.RankRepository;
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
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/skills")
@RequiredArgsConstructor
public class SkillController {
    private final SkillRepository skillRepository;
    private final EmployeeRepository employeeRepository;
    private final IndustryRepository industryRepository;
    private final ProfessionRepository professionRepository;
    private final SpecializationRepository specializationRepository;
    private final RankRepository rankRepository;
    private final PositionRepository positionRepository;
    private final LinkSkillsLocationsRepository linkSkillsLocationsRepository;

    @GetMapping
    public ResponseEntity<List<Skill>> getAllSkills() {
        List<Skill> skills = skillRepository.findAll();
        return new ResponseEntity<>(skills, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Skill> getSkillById(@PathVariable String id) {
        Long parsedId = Long.parseLong(id);
        Optional<Skill> skill = skillRepository.findById(parsedId);
        return new ResponseEntity<>(skill.get(), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<Map<String, List<Skill>>> searchSkillsByEquipmentsLike
            (@ModelAttribute SkillSearchCriteria criteria) {
        List<Skill> skills = skillRepository.findCompaniesByEquipmentsLike("%" + criteria.getQuery() + "%");
        return new ResponseEntity<>(Collections.singletonMap("skills", skills), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Skill> createSkill(@RequestBody SkillCreateRequest request) {
//        if (result.hasErrors()) {
//            throw new IllegalRequestException(result);
//        }
        Skill skill = new Skill();
        skill.setExperience(request.getExperience());
        skill.setActive(request.isActive());
        skill.setRecommendations(request.getRecommendations());
        skill.setEquipments(request.getEquipments());
        skill.setSalaryMin(request.getSalaryMin());
        skill.setSalaryMax(request.getSalaryMax());
        skill.setTermMin(request.getTermMin());
        skill.setTermMax(request.getTermMax());
        skill.setCreated(Timestamp.valueOf(LocalDateTime.now()));
        skill.setChanged(Timestamp.valueOf(LocalDateTime.now()));
        Optional<Employee> optionalEmployee = employeeRepository.findById(request.getEmployeeId());
        Employee employee = optionalEmployee.get();
        skill.setEmployee(employee);
        Optional<Industry> optionalIndustry = industryRepository.findById(request.getIndustryId());
        Industry industry = optionalIndustry.get();
        skill.setIndustry(industry);
        Optional<Profession> optionalProfession = professionRepository.findById(request.getProfessionId());
        Profession profession = optionalProfession.get();
        skill.setProfession(profession);
        Optional<Specialization> optionalSpecialization = specializationRepository.findById(request.getSpecializationId());
        Specialization specialization = optionalSpecialization.get();
        skill.setSpecialization(specialization);
        Optional<Rank> optionalRank = rankRepository.findById(request.getRankId());
        Rank rank = optionalRank.get();
        skill.setRank(rank);
        Optional<Position> optionalPosition = positionRepository.findById(request.getPositionId());
        Position position = optionalPosition.get();
        skill.setPosition(position);
        skill = skillRepository.save(skill);
        return new ResponseEntity<>(skill, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Skill> updateSkill(@RequestBody SkillUpdateRequest request) {
//        if (result.hasErrors()) {
//            throw new IllegalRequestException(result);
//        }
        Optional<Skill> optionalSkill = skillRepository.findById(request.getId());
        Skill skill = optionalSkill.get();
        skill.setExperience(request.getExperience());
        skill.setActive(request.isActive());
        skill.setRecommendations(request.getRecommendations());
        skill.setEquipments(request.getEquipments());
        skill.setSalaryMin(request.getSalaryMin());
        skill.setSalaryMax(request.getSalaryMax());
        skill.setTermMin(request.getTermMin());
        skill.setTermMax(request.getTermMax());
//        skill.setCreated(Timestamp.valueOf(LocalDateTime.now()));
        skill.setChanged(Timestamp.valueOf(LocalDateTime.now()));
//        Optional<Employee> optionalEmployee = employeeRepository.findById(request.getEmployeeId());
//        Employee employee = optionalEmployee.get();
//        skill.setEmployee(employee);
        Optional<Industry> optionalIndustry = industryRepository.findById(request.getIndustryId());
        Industry industry = optionalIndustry.get();
        skill.setIndustry(industry);
        Optional<Profession> optionalProfession = professionRepository.findById(request.getProfessionId());
        Profession profession = optionalProfession.get();
        skill.setProfession(profession);
        Optional<Specialization> optionalSpecialization = specializationRepository.findById(request.getSpecializationId());
        Specialization specialization = optionalSpecialization.get();
        skill.setSpecialization(specialization);
        Optional<Rank> optionalRank = rankRepository.findById(request.getRankId());
        Rank rank = optionalRank.get();
        skill.setRank(rank);
        Optional<Position> optionalPosition = positionRepository.findById(request.getPositionId());
        Position position = optionalPosition.get();
        skill.setPosition(position);
        skill = skillRepository.save(skill);
        return new ResponseEntity<>(skill, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Skill> deleteSkill(@PathVariable Long id) {
//        if (result.hasErrors()) {
//            throw new IllegalRequestException(result);
//        }
        Optional<Skill> optionalSkill = skillRepository.findById(id);
        Skill skill = optionalSkill.get();
        skillRepository.delete(skill);
        return new ResponseEntity<>(skill, HttpStatus.OK);
    }

    @PostMapping("/add_location/{skill_id}/{location_id}")
    public ResponseEntity<LinkSkillsLocations> addLocationToSkill
            (@PathVariable Long skill_id, @PathVariable Long location_id) {
//        if (result.hasErrors()) {
//            throw new IllegalRequestException(result);
//        }
        LinkSkillsLocations link = new LinkSkillsLocations();
        link.setSkillId(skill_id);
        link.setLocationId(location_id);
        link.setCreated(Timestamp.valueOf(LocalDateTime.now()));
        link.setChanged(Timestamp.valueOf(LocalDateTime.now()));
        link = linkSkillsLocationsRepository.save(link);
        return new ResponseEntity<>(link, HttpStatus.CREATED);
    }

}
