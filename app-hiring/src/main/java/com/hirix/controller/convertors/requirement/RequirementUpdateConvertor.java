package com.hirix.controller.convertors.requirement;

import com.hirix.controller.requests.update.RequirementUpdateRequest;
import com.hirix.domain.Industry;
import com.hirix.domain.Location;
import com.hirix.domain.Position;
import com.hirix.domain.Profession;
import com.hirix.domain.Rank;
import com.hirix.domain.Requirement;
import com.hirix.domain.Specialization;
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
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RequirementUpdateConvertor extends RequirementBaseConvertor<RequirementUpdateRequest, Requirement> {
    private final RequirementRepository requirementRepository;
    private final IndustryRepository industryRepository;
    private final ProfessionRepository professionRepository;
    private final SpecializationRepository specializationRepository;
    private final RankRepository rankRepository;
    private final PositionRepository positionRepository;
    private final LocationRepository locationRepository;

    @Override
    public Requirement convert(RequirementUpdateRequest request) {
        Long id;
        try {
            id = request.getId();
        } catch (Exception e) {
            throw new PoorInfoInRequestToCreateUpdateEntity
                    ("Poor information about requirement id in request body to update requirement. Must be Long type. " + e.getCause());
        }
        if (id < 1L) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Poor information in request body to update requirement. " +
                    "Skill id must be more than 0L");
        }
        Optional<Requirement> optionalRequirement;
        try {
            optionalRequirement = requirementRepository.findById(id);
        } catch (Exception e) {
            throw new EntityNotFoundException("Can not get requirement by id from DB. " + e.getCause());
        }
        Requirement requirement = optionalRequirement.orElseThrow(() -> new NoSuchElementException("No requirement with such id"));

        Long industryId;
        try {
            industryId = request.getIndustryId();
        } catch (Exception e) {
            throw new PoorInfoInRequestToCreateUpdateEntity
                    ("Poor information about industry id in request body to update requirement. Must be Long type. " +
                            e.getCause());
        }
        if (industryId < 1L) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Poor information in request body to update requirement. " +
                    "Industry id must be more than 0L");
        }
        if(!industryId.equals(requirement.getIndustry().getId())) {
            setIndustryToRequirement(requirement, industryId, industryRepository);
        }

        Long professionId;
        try {
            professionId = request.getProfessionId();
        } catch (Exception e) {
            throw new PoorInfoInRequestToCreateUpdateEntity
                    ("Poor information about profession id in request body to update requirement. Must be Long type. " +
                            e.getCause());
        }
        if (professionId < 1L) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Poor information in request body to update requirement. " +
                    "Profession id must be more than 0L");
        }
        if(!professionId.equals(requirement.getProfession().getId())) {
            setProfessionToRequirement(requirement, professionId, professionRepository);
        }

        Long specializationId;
        try {
            specializationId = request.getSpecializationId();
        } catch (Exception e) {
            throw new PoorInfoInRequestToCreateUpdateEntity
                    ("Poor information about specialization id in request body to update requirement. Must be Long type. " +
                            e.getCause());
        }
        if (specializationId < 1L) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Poor information in request body to update requirement. " +
                    "Specialization id must be more than 0L");
        }
        if (!specializationId.equals(requirement.getSpecialization().getId())) {
            setSpecializationToRequirement(requirement, specializationId, specializationRepository);
        }

        Long rankId;
        try {
            rankId = request.getRankId();
        } catch (Exception e) {
            throw new PoorInfoInRequestToCreateUpdateEntity
                    ("Poor information about rank id in request body to update requirement. Must be Long type. " +
                            e.getCause());
        }
        if (rankId < 1L) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Poor information in request body to update requirement. " +
                    "Rank id must be more than 0L");
        }
        if (!rankId.equals(requirement.getRank().getId())) {
            setRankToRequirement(requirement, rankId, rankRepository);
        }

        Long positionId;
        try {
            positionId = request.getPositionId();
        } catch (Exception e) {
            throw new PoorInfoInRequestToCreateUpdateEntity
                    ("Poor information about position id in request body to update requirement. Must be Long type. " +
                            e.getCause());
        }
        if (positionId < 1L) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Poor information in request body to update requirement. " +
                    "Position id must be more than 0L");
        }
        if(!positionId.equals(requirement.getPosition().getId())) {
            setPositionToRequirement(requirement, positionId, positionRepository);
        }

        Long locationOfferedId;
        try {
            locationOfferedId = request.getLocationOfferedId();
        } catch (Exception e) {
            throw new PoorInfoInRequestToCreateUpdateEntity
                    ("Poor information about location id in request body to update requirement. Must be Long type. " +
                            e.getCause());
        }
        if (locationOfferedId < 1L) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Poor information in request body to update requirement. " +
                    "Location id must be more than 0L");
        }
        if(!locationOfferedId.equals(requirement.getLocationOffered().getId())) {
            setLocationToRequirement(requirement, locationOfferedId, locationRepository);
        }

        return doConvert(request, requirement);
    }

    static void setPositionToRequirement(Requirement requirement, Long positionId, PositionRepository positionRepository) {
        Optional<Position> optionalPosition;
        try {
            optionalPosition = positionRepository.findById(positionId);
        } catch (Exception e) {
            throw new EntityNotFoundException("Can not get position by id from DB. " + e.getCause());
        }
        Position position = optionalPosition.orElseThrow(() -> new NoSuchElementException("No position with such id"));
        requirement.setPosition(position);
    }

    static void setRankToRequirement(Requirement requirement, Long rankId, RankRepository rankRepository) {
        Optional<Rank> optionalRank;
        try {
            optionalRank = rankRepository.findById(rankId);
        } catch (Exception e) {
            throw new EntityNotFoundException("Can not get rank by id from DB. " + e.getCause());
        }
        Rank rank = optionalRank.orElseThrow(() -> new NoSuchElementException("No rank with such id"));
        requirement.setRank(rank);
    }

    static void setSpecializationToRequirement(Requirement requirement, Long specializationId, SpecializationRepository specializationRepository) {
        Optional<Specialization> optionalSpecialization;
        try {
            optionalSpecialization = specializationRepository.findById(specializationId);
        } catch (Exception e) {
            throw new EntityNotFoundException("Can not get specialization by id from DB. " + e.getCause());
        }
        Specialization specialization = optionalSpecialization.orElseThrow(() -> new NoSuchElementException("No specialization with such id"));
        requirement.setSpecialization(specialization);
    }

    static void setProfessionToRequirement(Requirement requirement, Long professionId, ProfessionRepository professionRepository) {
        Optional<Profession> optionalProfession;
        try {
            optionalProfession = professionRepository.findById(professionId);
        } catch (Exception e) {
            throw new EntityNotFoundException("Can not get profession by id from DB. " + e.getCause());
        }
        Profession profession = optionalProfession.orElseThrow(() -> new NoSuchElementException("No profession with such id"));
        requirement.setProfession(profession);
    }

    static void setIndustryToRequirement(Requirement requirement, Long industryId, IndustryRepository industryRepository) {
        Optional<Industry> optionalIndustry;
        try {
            optionalIndustry = industryRepository.findById(industryId);
        } catch (Exception e) {
            throw new EntityNotFoundException("Can not get industry by id from DB. " + e.getCause());
        }
        Industry industry = optionalIndustry.orElseThrow(() -> new NoSuchElementException("No industry with such id"));
        requirement.setIndustry(industry);
    }

    static void setLocationToRequirement(Requirement requirement, Long locationOfferedId, LocationRepository locationRepository) {
        Optional<Location> optionalLocation;
        try {
            optionalLocation = locationRepository.findById(locationOfferedId);
        } catch (Exception e) {
            throw new EntityNotFoundException("Can not get location by id from DB. " + e.getCause());
        }
        Location locationOffered = optionalLocation.orElseThrow(() -> new NoSuchElementException("No location with such id"));
        requirement.setLocationOffered(locationOffered);
    }
}

