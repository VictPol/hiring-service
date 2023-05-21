package com.hirix.controller.convertors.requirement;

import com.hirix.controller.requests.patch.RequirementPatchRequest;
import com.hirix.domain.Requirement;
import com.hirix.exception.EntityNotFoundException;
import com.hirix.exception.PoorInfoInRequestToCreateUpdateEntity;
import com.hirix.repository.IndustryRepository;
import com.hirix.repository.LocationRepository;
import com.hirix.repository.PositionRepository;
import com.hirix.repository.ProfessionRepository;
import com.hirix.repository.RankRepository;
import com.hirix.repository.RequirementRepository;
import com.hirix.repository.SpecializationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

import static com.hirix.controller.convertors.requirement.RequirementUpdateConvertor.setIndustryToRequirement;
import static com.hirix.controller.convertors.requirement.RequirementUpdateConvertor.setLocationToRequirement;
import static com.hirix.controller.convertors.requirement.RequirementUpdateConvertor.setPositionToRequirement;
import static com.hirix.controller.convertors.requirement.RequirementUpdateConvertor.setProfessionToRequirement;
import static com.hirix.controller.convertors.requirement.RequirementUpdateConvertor.setRankToRequirement;
import static com.hirix.controller.convertors.requirement.RequirementUpdateConvertor.setSpecializationToRequirement;

@Component
@RequiredArgsConstructor
public class RequirementPatchConvertor implements Converter<RequirementPatchRequest, Requirement> {
    private final RequirementRepository requirementRepository;
    private final IndustryRepository industryRepository;
    private final ProfessionRepository professionRepository;
    private final SpecializationRepository specializationRepository;
    private final RankRepository rankRepository;
    private final PositionRepository positionRepository;
    private final LocationRepository locationRepository;
    @Override
    public Requirement convert(RequirementPatchRequest request) {
        Long id;
        try {
            id = request.getId();
        } catch (Exception e) {
            throw new PoorInfoInRequestToCreateUpdateEntity
                    ("Poor information about skill id in request body to patch update requirement. Must be Long type. " + e.getCause());
        }
        if (id < 1L) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Poor information in request body to patch update requirement. " +
                    "Skill id must be more than 0L");
        }
        Optional<Requirement> optionalRequirement;
        try {
            optionalRequirement = requirementRepository.findById(id);
        } catch (Exception e) {
            throw new EntityNotFoundException("Can not get requirement by id from DB. " + e.getCause());
        }
        Requirement requirement = optionalRequirement.orElseThrow(() -> new NoSuchElementException("No requirement with such id"));
        try {
            if (request.getExperience() != null && request.getExperience() >= 0) {
                requirement.setExperience(request.getExperience());
            }
            requirement.setActive(request.isActive());
            if (request.getRecommendations() != null && request.getRecommendations() >= 0) {
                requirement.setRecommendations(request.getRecommendations());
            }
            if (request.getEquipments() != null) {
                requirement.setEquipments(request.getEquipments());
            }
            if (request.getSalary() != null && request.getSalary() >= 0) {
                requirement.setSalary(request.getSalary());
            }
            if (request.getTerm() != null && request.getTerm() >= 0) {
                requirement.setTerm(request.getTerm());
            }
        } catch (Exception e) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Poor information in request body to patch update requirement. " +
                    e.getCause());
        }

        Long industryId;
        try {
            industryId = request.getIndustryId();
        } catch (Exception e) {
            throw new PoorInfoInRequestToCreateUpdateEntity
                    ("Poor information about requirement industry id in request body to patch update requirement. Must be Long type. " +
                            e.getCause());
        }
        if (industryId != null && industryId > 0L && !industryId.equals(requirement.getIndustry().getId())) {
            setIndustryToRequirement(requirement, industryId, industryRepository);
        }
        if (industryId != null && industryId < 1L) {
            throw new PoorInfoInRequestToCreateUpdateEntity
                    ("Poor information about requirement industry id in request body to patch update requirement. Must be more than 1L.");
        }

        Long professionId;
        try {
            professionId = request.getProfessionId();
        } catch (Exception e) {
            throw new PoorInfoInRequestToCreateUpdateEntity
                    ("Poor information about requirement profession id in request body to patch update requirement. Must be Long type. " +
                            e.getCause());
        }
        if (professionId != null && professionId > 0L && !professionId.equals(requirement.getProfession().getId())) {
            setProfessionToRequirement(requirement, professionId, professionRepository);
        }
        if (professionId != null && professionId < 1L) {
            throw new PoorInfoInRequestToCreateUpdateEntity
                    ("Poor information about requirement profession id in request body to patch update requirement. Must be more than 1L.");
        }

        Long specializationId;
        try {
            specializationId = request.getSpecializationId();
        } catch (Exception e) {
            throw new PoorInfoInRequestToCreateUpdateEntity
                    ("Poor information about requirement specialization id in request body to patch update requirement. Must be Long type. " +
                            e.getCause());
        }
        if (specializationId != null && specializationId > 0L && !specializationId.equals(requirement.getSpecialization().getId())) {
            setSpecializationToRequirement(requirement, specializationId, specializationRepository);
        }
        if (specializationId != null && specializationId < 1L) {
            throw new PoorInfoInRequestToCreateUpdateEntity
                    ("Poor information about requirement specialization id in request body to patch update requirement. Must be more than 1L.");
        }

        Long rankId;
        try {
            rankId = request.getRankId();
        } catch (Exception e) {
            throw new PoorInfoInRequestToCreateUpdateEntity
                    ("Poor information about requirement rank id in request body to patch update requirement. Must be Long type. " +
                            e.getCause());
        }
        if (rankId != null && rankId > 0L && !rankId.equals(requirement.getRank().getId())) {
            setRankToRequirement(requirement, rankId, rankRepository);
        }
        if (rankId != null && rankId < 1L) {
            throw new PoorInfoInRequestToCreateUpdateEntity
                    ("Poor information about requirement rank id in request body to patch update requirement. Must be more than 1L.");
        }

        Long positionId;
        try {
            positionId = request.getPositionId();
        } catch (Exception e) {
            throw new PoorInfoInRequestToCreateUpdateEntity
                    ("Poor information about requirement position id in request body to patch update requirement. Must be Long type. " +
                            e.getCause());
        }
        if (positionId != null && positionId > 0L && !positionId.equals(requirement.getPosition().getId())) {
            setPositionToRequirement(requirement, positionId, positionRepository);
        }
        if (positionId != null && positionId < 1L) {
            throw new PoorInfoInRequestToCreateUpdateEntity
                    ("Poor information about requirement position id in request body to patch update requirement. Must be more than 1L.");
        }

        Long locationOfferedId;
        try {
            locationOfferedId = request.getLocationOfferedId();
        } catch (Exception e) {
            throw new PoorInfoInRequestToCreateUpdateEntity
                    ("Poor information about requirement location id in request body to patch update requirement. Must be Long type. " +
                            e.getCause());
        }
        if (locationOfferedId != null && locationOfferedId > 0L && !locationOfferedId.equals(requirement.getLocationOffered().getId())) {
            setLocationToRequirement(requirement, locationOfferedId, locationRepository);
        }
        if (locationOfferedId != null && locationOfferedId < 1L) {
            throw new PoorInfoInRequestToCreateUpdateEntity
                    ("Poor information about requirement location id in request body to patch update requirement. Must be more than 1L.");
        }

        requirement.setChanged(Timestamp.valueOf(LocalDateTime.now()));

        return requirement;
    }
}

