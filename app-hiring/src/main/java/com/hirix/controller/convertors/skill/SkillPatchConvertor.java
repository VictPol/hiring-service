package com.hirix.controller.convertors.skill;

import com.hirix.controller.requests.patch.SkillPatchRequest;
import com.hirix.domain.Skill;
import com.hirix.exception.EntityNotFoundException;
import com.hirix.exception.PoorInfoInRequestToCreateUpdateEntity;
import com.hirix.repository.IndustryRepository;
import com.hirix.repository.PositionRepository;
import com.hirix.repository.ProfessionRepository;
import com.hirix.repository.RankRepository;
import com.hirix.repository.SkillRepository;
import com.hirix.repository.SpecializationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

import static com.hirix.controller.convertors.skill.SkillUpdateConvertor.setIndustryToSkill;
import static com.hirix.controller.convertors.skill.SkillUpdateConvertor.setPositionToSkill;
import static com.hirix.controller.convertors.skill.SkillUpdateConvertor.setProfessionToSkill;
import static com.hirix.controller.convertors.skill.SkillUpdateConvertor.setRankToSkill;
import static com.hirix.controller.convertors.skill.SkillUpdateConvertor.setSpecializationToSkill;

@Component
@RequiredArgsConstructor
public class SkillPatchConvertor implements Converter<SkillPatchRequest, Skill> {
    private final SkillRepository skillRepository;
    private final IndustryRepository industryRepository;
    private final ProfessionRepository professionRepository;
    private final SpecializationRepository specializationRepository;
    private final RankRepository rankRepository;
    private final PositionRepository positionRepository;
    @Override
    public Skill convert(SkillPatchRequest request) {
        Long id;
        try {
            id = request.getId();
        } catch (Exception e) {
            throw new PoorInfoInRequestToCreateUpdateEntity
                    ("Poor information about skill id in request body to patch update skill. Must be Long type. " + e.getCause());
        }
        if (id < 1L) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Poor information in request body to patch update skill. " +
                    "Skill id must be more than 0L");
        }
        Optional<Skill> optionalSkill;
        try {
            optionalSkill = skillRepository.findById(id);
        } catch (Exception e) {
            throw new EntityNotFoundException("Can not get skill by id from DB. " + e.getCause());
        }
        Skill skill = optionalSkill.orElseThrow(() -> new NoSuchElementException("No skill with such id"));
        try {
            if (request.getExperience() != null && request.getExperience() >= 0) {
                skill.setExperience(request.getExperience());
            }
            request.setActive(request.isActive());
            if (request.getRecommendations() != null && request.getRecommendations() >= 0) {
                skill.setRecommendations(request.getRecommendations());
            }
            if (request.getEquipments() != null) {
                skill.setEquipments(request.getEquipments());
            }
            if (request.getSalaryMin() != null && request.getSalaryMin() >= 0) {
                skill.setSalaryMin(request.getSalaryMin());
            }
            if (request.getSalaryMax() != null && request.getSalaryMax() >= 0) {
                skill.setSalaryMax(request.getSalaryMax());
            }
            if (request.getTermMin() != null && request.getTermMin() >= 0) {
                skill.setTermMin(request.getTermMin());
            }
            if (request.getTermMax() != null && request.getTermMax() >= 0) {
                skill.setTermMax(request.getTermMax());
            }
        } catch (Exception e) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Poor information in request body to patch update skill. " +
                    e.getCause());
        }

        Long industryId;
        try {
            industryId = request.getIndustryId();
        } catch (Exception e) {
            throw new PoorInfoInRequestToCreateUpdateEntity
                    ("Poor information about skill industry id in request body to patch update skill. Must be Long type. " +
                            e.getCause());
        }
        if (industryId != null && industryId > 0L && !industryId.equals(skill.getIndustry().getId())) {
            setIndustryToSkill(skill, industryId, industryRepository);
        }
        if (industryId != null && industryId < 1L) {
            throw new PoorInfoInRequestToCreateUpdateEntity
                    ("Poor information about skill industry id in request body to patch update skill. Must be more than 1L.");
        }

        Long professionId;
        try {
            professionId = request.getProfessionId();
        } catch (Exception e) {
            throw new PoorInfoInRequestToCreateUpdateEntity
                    ("Poor information about skill profession id in request body to patch update skill. Must be Long type. " +
                            e.getCause());
        }
        if (professionId != null && professionId > 0L && !professionId.equals(skill.getProfession().getId())) {
            setProfessionToSkill(skill, professionId, professionRepository);
        }
        if (professionId != null && professionId < 1L) {
            throw new PoorInfoInRequestToCreateUpdateEntity
                    ("Poor information about skill profession id in request body to patch update skill. Must be more than 1L.");
        }

        Long specializationId;
        try {
            specializationId = request.getSpecializationId();
        } catch (Exception e) {
            throw new PoorInfoInRequestToCreateUpdateEntity
                    ("Poor information about skill specialization id in request body to patch update skill. Must be Long type. " +
                            e.getCause());
        }
        if (specializationId != null && specializationId > 0L && !specializationId.equals(skill.getSpecialization().getId())) {
            setSpecializationToSkill(skill, specializationId, specializationRepository);
        }
        if (specializationId != null && specializationId < 1L) {
            throw new PoorInfoInRequestToCreateUpdateEntity
                    ("Poor information about skill specialization id in request body to patch update skill. Must be more than 1L.");
        }

        Long rankId;
        try {
            rankId = request.getRankId();
        } catch (Exception e) {
            throw new PoorInfoInRequestToCreateUpdateEntity
                    ("Poor information about skill rank id in request body to patch update skill. Must be Long type. " +
                            e.getCause());
        }
        if (rankId != null && rankId > 0L && !rankId.equals(skill.getRank().getId())) {
            setRankToSkill(skill, rankId, rankRepository);
        }
        if (rankId != null && rankId < 1L) {
            throw new PoorInfoInRequestToCreateUpdateEntity
                    ("Poor information about skill rank id in request body to patch update skill. Must be more than 1L.");
        }

        Long positionId;
        try {
            positionId = request.getPositionId();
        } catch (Exception e) {
            throw new PoorInfoInRequestToCreateUpdateEntity
                    ("Poor information about skill position id in request body to patch update skill. Must be Long type. " +
                            e.getCause());
        }
        if (positionId != null && positionId > 0L && !positionId.equals(skill.getPosition().getId())) {
            setPositionToSkill(skill, positionId, positionRepository);
        }
        if (positionId != null && positionId < 1L) {
            throw new PoorInfoInRequestToCreateUpdateEntity
                    ("Poor information about skill position id in request body to patch update skill. Must be more than 1L.");
        }

        skill.setChanged(Timestamp.valueOf(LocalDateTime.now()));

        return skill;
    }
}
