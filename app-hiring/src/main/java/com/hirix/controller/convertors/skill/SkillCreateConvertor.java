package com.hirix.controller.convertors.skill;

import com.hirix.controller.requests.create.SkillCreateRequest;
import com.hirix.domain.Skill;
import com.hirix.exception.PoorInfoInRequestToCreateUpdateEntity;
import com.hirix.repository.EmployeeRepository;
import com.hirix.repository.IndustryRepository;
import com.hirix.repository.PositionRepository;
import com.hirix.repository.ProfessionRepository;
import com.hirix.repository.RankRepository;
import com.hirix.repository.SpecializationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class SkillCreateConvertor extends SkillBaseConvertor<SkillCreateRequest, Skill> {

    private final EmployeeRepository employeeRepository;
    private final IndustryRepository industryRepository;
    private final ProfessionRepository professionRepository;
    private final SpecializationRepository specializationRepository;
    private final RankRepository rankRepository;
    private final PositionRepository positionRepository;

    @Override
    public Skill convert(SkillCreateRequest request) {
        Skill skill = new Skill();
        Long employeeId;
        try {
            employeeId = request.getEmployeeId();
        } catch (Exception e) {
            throw new PoorInfoInRequestToCreateUpdateEntity
                    ("Poor information about employee id in request body to create skill. Must be Long type. " +
                            e.getCause());
        }
        if (employeeId < 1L) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Poor information in request body to create skill. " +
                    "Employee id must be more than 0L");
        }
        SkillUpdateConvertor.setEmployeeToSkill(skill, employeeId, employeeRepository);

        Long industryId;
        try {
            industryId = request.getIndustryId();
        } catch (Exception e) {
            throw new PoorInfoInRequestToCreateUpdateEntity
                    ("Poor information about industry id in request body to create skill. Must be Long type. " +
                            e.getCause());
        }
        if (industryId < 1L) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Poor information in request body to create skill. " +
                    "Industry id must be more than 0L");
        }
        SkillUpdateConvertor.setIndustryToSkill(skill, industryId, industryRepository);

        Long professionId;
        try {
            professionId = request.getProfessionId();
        } catch (Exception e) {
            throw new PoorInfoInRequestToCreateUpdateEntity
                    ("Poor information about profession id in request body to create skill. Must be Long type. " +
                            e.getCause());
        }
        if (professionId < 1L) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Poor information in request body to create skill. " +
                    "Profession id must be more than 0L");
        }
        SkillUpdateConvertor.setProfessionToSkill(skill, professionId, professionRepository);

        Long specializationId;
        try {
            specializationId = request.getSpecializationId();
        } catch (Exception e) {
            throw new PoorInfoInRequestToCreateUpdateEntity
                    ("Poor information about specialization id in request body to create skill. Must be Long type. " +
                            e.getCause());
        }
        if (specializationId < 1L) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Poor information in request body to create skill. " +
                    "Specialization id must be more than 0L");
        }
        SkillUpdateConvertor.setSpecializationToSkill(skill, specializationId, specializationRepository);

        Long rankId;
        try {
            rankId = request.getRankId();
        } catch (Exception e) {
            throw new PoorInfoInRequestToCreateUpdateEntity
                    ("Poor information about rank id in request body to create skill. Must be Long type. " +
                            e.getCause());
        }
        if (rankId < 1L) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Poor information in request body to create skill. " +
                    "Rank id must be more than 0L");
        }
        SkillUpdateConvertor.setRankToSkill(skill, rankId, rankRepository);

        Long positionId;
        try {
            positionId = request.getPositionId();
        } catch (Exception e) {
            throw new PoorInfoInRequestToCreateUpdateEntity
                    ("Poor information about position id in request body to create skill. Must be Long type. " +
                            e.getCause());
        }
        if (positionId < 1L) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Poor information in request body to create skill. " +
                    "Position id must be more than 0L");
        }
        SkillUpdateConvertor.setPositionToSkill(skill, positionId, positionRepository);

        skill.setCreated(Timestamp.valueOf(LocalDateTime.now()));
        return doConvert(request, skill);
    }
}
