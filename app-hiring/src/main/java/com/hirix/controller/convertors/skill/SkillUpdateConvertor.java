package com.hirix.controller.convertors.skill;

import com.hirix.controller.requests.update.SkillUpdateRequest;
import com.hirix.domain.Employee;
import com.hirix.domain.Industry;
import com.hirix.domain.Position;
import com.hirix.domain.Profession;
import com.hirix.domain.Rank;
import com.hirix.domain.Skill;
import com.hirix.domain.Specialization;
import com.hirix.exception.EntityNotFoundException;
import com.hirix.exception.PoorInfoInRequestToCreateUpdateEntity;
import com.hirix.repository.EmployeeRepository;
import com.hirix.repository.IndustryRepository;
import com.hirix.repository.PositionRepository;
import com.hirix.repository.ProfessionRepository;
import com.hirix.repository.RankRepository;
import com.hirix.repository.SkillRepository;
import com.hirix.repository.SpecializationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SkillUpdateConvertor extends SkillBaseConvertor<SkillUpdateRequest, Skill> {
    private final SkillRepository skillRepository;
    private final IndustryRepository industryRepository;
    private final ProfessionRepository professionRepository;
    private final SpecializationRepository specializationRepository;
    private final RankRepository rankRepository;
    private final PositionRepository positionRepository;

    @Override
    public Skill convert(SkillUpdateRequest request) {
        Long id;
        try {
            id = request.getId();
        } catch (Exception e) {
            throw new PoorInfoInRequestToCreateUpdateEntity
                    ("Poor information about skill id in request body to update skill. Must be Long type. " + e.getCause());
        }
        if (id < 1L) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Poor information in request body to update skill. " +
                    "Skill id must be more than 0L");
        }
        Optional<Skill> optionalSkill;
        try {
            optionalSkill = skillRepository.findById(id);
        } catch (Exception e) {
            throw new EntityNotFoundException("Can not get skill by id from DB. " + e.getCause());
        }
        Skill skill = optionalSkill.orElseThrow(() -> new NoSuchElementException("No skill with such id"));

//        Long employeeId;
//        try {
//            employeeId = request.getEmployeeId();
//        } catch (Exception e) {
//            throw new PoorInfoInRequestToCreateUpdateEntity
//                    ("Poor information about employee id in request body to update skill. Must be Long type. " +
//                            e.getCause());
//        }
//        if (employeeId < 1L) {
//            throw new PoorInfoInRequestToCreateUpdateEntity("Poor information in request body to update skill. " +
//                    "Employee id must be more than 0L");
//        }
//        if (!employeeId.equals(skill.getEmployee().getId())) {
//            setEmployeeToSkill(skill, employeeId, employeeRepository);
//        }

        Long industryId;
        try {
            industryId = request.getIndustryId();
        } catch (Exception e) {
            throw new PoorInfoInRequestToCreateUpdateEntity
                    ("Poor information about industry id in request body to update skill. Must be Long type. " +
                            e.getCause());
        }
        if (industryId < 1L) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Poor information in request body to update skill. " +
                    "Industry id must be more than 0L");
        }
        if(!industryId.equals(skill.getIndustry().getId())) {
            setIndustryToSkill(skill, industryId, industryRepository);
        }

        Long professionId;
        try {
            professionId = request.getProfessionId();
        } catch (Exception e) {
            throw new PoorInfoInRequestToCreateUpdateEntity
                    ("Poor information about profession id in request body to update skill. Must be Long type. " +
                            e.getCause());
        }
        if (professionId < 1L) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Poor information in request body to update skill. " +
                    "Profession id must be more than 0L");
        }
        if(!professionId.equals(skill.getProfession().getId())) {
            setProfessionToSkill(skill, professionId, professionRepository);
        }

        Long specializationId;
        try {
            specializationId = request.getSpecializationId();
        } catch (Exception e) {
            throw new PoorInfoInRequestToCreateUpdateEntity
                    ("Poor information about specialization id in request body to update skill. Must be Long type. " +
                            e.getCause());
        }
        if (specializationId < 1L) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Poor information in request body to update skill. " +
                    "Specialization id must be more than 0L");
        }
        if (!specializationId.equals(skill.getSpecialization().getId())) {
            setSpecializationToSkill(skill, specializationId, specializationRepository);
        }

        Long rankId;
        try {
            rankId = request.getRankId();
        } catch (Exception e) {
            throw new PoorInfoInRequestToCreateUpdateEntity
                    ("Poor information about rank id in request body to update skill. Must be Long type. " +
                            e.getCause());
        }
        if (rankId < 1L) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Poor information in request body to update skill. " +
                    "Rank id must be more than 0L");
        }
        if (!rankId.equals(skill.getRank().getId())) {
            setRankToSkill(skill, rankId, rankRepository);
        }

        Long positionId;
        try {
            positionId = request.getPositionId();
        } catch (Exception e) {
            throw new PoorInfoInRequestToCreateUpdateEntity
                    ("Poor information about position id in request body to update skill. Must be Long type. " +
                            e.getCause());
        }
        if (positionId < 1L) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Poor information in request body to update skill. " +
                    "Position id must be more than 0L");
        }
        if(!positionId.equals(skill.getPosition().getId())) {
            setPositionToSkill(skill, positionId, positionRepository);
        }

        return doConvert(request, skill);
    }

    static void setPositionToSkill(Skill skill, Long positionId, PositionRepository positionRepository) {
        Optional<Position> optionalPosition;
        try {
            optionalPosition = positionRepository.findById(positionId);
        } catch (Exception e) {
            throw new EntityNotFoundException("Can not get position by id from DB. " + e.getCause());
        }
        Position position = optionalPosition.orElseThrow(() -> new NoSuchElementException("No position with such id"));
        skill.setPosition(position);
    }

    static void setRankToSkill(Skill skill, Long rankId, RankRepository rankRepository) {
        Optional<Rank> optionalRank;
        try {
            optionalRank = rankRepository.findById(rankId);
        } catch (Exception e) {
            throw new EntityNotFoundException("Can not get rank by id from DB. " + e.getCause());
        }
        Rank rank = optionalRank.orElseThrow(() -> new NoSuchElementException("No rank with such id"));
        skill.setRank(rank);
    }

    static void setSpecializationToSkill(Skill skill, Long specializationId, SpecializationRepository specializationRepository) {
        Optional<Specialization> optionalSpecialization;
        try {
            optionalSpecialization = specializationRepository.findById(specializationId);
        } catch (Exception e) {
            throw new EntityNotFoundException("Can not get specialization by id from DB. " + e.getCause());
        }
        Specialization specialization = optionalSpecialization.orElseThrow(() -> new NoSuchElementException("No specialization with such id"));
        skill.setSpecialization(specialization);
    }

    static void setProfessionToSkill(Skill skill, Long professionId, ProfessionRepository professionRepository) {
        Optional<Profession> optionalProfession;
        try {
            optionalProfession = professionRepository.findById(professionId);
        } catch (Exception e) {
            throw new EntityNotFoundException("Can not get profession by id from DB. " + e.getCause());
        }
        Profession profession = optionalProfession.orElseThrow(() -> new NoSuchElementException("No profession with such id"));
        skill.setProfession(profession);
    }

    static void setIndustryToSkill(Skill skill, Long industryId, IndustryRepository industryRepository) {
        Optional<Industry> optionalIndustry;
        try {
            optionalIndustry = industryRepository.findById(industryId);
        } catch (Exception e) {
            throw new EntityNotFoundException("Can not get industry by id from DB. " + e.getCause());
        }
        Industry industry = optionalIndustry.orElseThrow(() -> new NoSuchElementException("No industry with such id"));
        skill.setIndustry(industry);
    }

    static void setEmployeeToSkill(Skill skill, Long employeeId, EmployeeRepository employeeRepository) {
        Optional<Employee> optionalEmployee;
        try {
            optionalEmployee = employeeRepository.findById(employeeId);
        } catch (Exception e) {
            throw new EntityNotFoundException("Can not get employee by id from DB. " + e.getCause());
        }
        Employee employee = optionalEmployee.orElseThrow(() -> new NoSuchElementException("No employee with such id"));
        skill.setEmployee(employee);
    }
}
