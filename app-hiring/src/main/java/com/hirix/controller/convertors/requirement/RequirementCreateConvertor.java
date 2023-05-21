package com.hirix.controller.convertors.requirement;

import com.hirix.controller.requests.create.RequirementCreateRequest;
import com.hirix.domain.Company;
import com.hirix.domain.Requirement;
import com.hirix.exception.EntityNotFoundException;
import com.hirix.exception.PoorInfoInRequestToCreateUpdateEntity;
import com.hirix.repository.CompanyRepository;
import com.hirix.repository.IndustryRepository;
import com.hirix.repository.LocationRepository;
import com.hirix.repository.PositionRepository;
import com.hirix.repository.ProfessionRepository;
import com.hirix.repository.RankRepository;
import com.hirix.repository.SpecializationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RequirementCreateConvertor extends RequirementBaseConvertor<RequirementCreateRequest, Requirement> {
    private final CompanyRepository companyRepository;
    private final IndustryRepository industryRepository;
    private final ProfessionRepository professionRepository;
    private final SpecializationRepository specializationRepository;
    private final RankRepository rankRepository;
    private final PositionRepository positionRepository;
    private final LocationRepository locationRepository;

    @Override
    public Requirement convert(RequirementCreateRequest request) {
        Requirement requirement = new Requirement();
        Long companyId;
        try {
            companyId = request.getCompanyId();
        } catch (Exception e) {
            throw new PoorInfoInRequestToCreateUpdateEntity
                    ("Poor information about company id in request body to create requirement. Must be Long type. " +
                            e.getCause());
        }
        if (companyId < 1L) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Poor information in request body to create requirement. " +
                    "Company id must be more than 0L");
        }
        setCompanyToRequirement(requirement, companyId, companyRepository);

        Long industryId;
        try {
            industryId = request.getIndustryId();
        } catch (Exception e) {
            throw new PoorInfoInRequestToCreateUpdateEntity
                    ("Poor information about industry id in request body to create requirement. Must be Long type. " +
                            e.getCause());
        }
        if (industryId < 1L) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Poor information in request body to create requirement. " +
                    "Industry id must be more than 0L");
        }
        RequirementUpdateConvertor.setIndustryToRequirement(requirement, industryId, industryRepository);

        Long professionId;
        try {
            professionId = request.getProfessionId();
        } catch (Exception e) {
            throw new PoorInfoInRequestToCreateUpdateEntity
                    ("Poor information about profession id in request body to create requirement. Must be Long type. " +
                            e.getCause());
        }
        if (professionId < 1L) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Poor information in request body to create requirement. " +
                    "Profession id must be more than 0L");
        }
        RequirementUpdateConvertor.setProfessionToRequirement(requirement, professionId, professionRepository);

        Long specializationId;
        try {
            specializationId = request.getSpecializationId();
        } catch (Exception e) {
            throw new PoorInfoInRequestToCreateUpdateEntity
                    ("Poor information about specialization id in request body to create requirement. Must be Long type. " +
                            e.getCause());
        }
        if (specializationId < 1L) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Poor information in request body to create requirement. " +
                    "Specialization id must be more than 0L");
        }
        RequirementUpdateConvertor.setSpecializationToRequirement(requirement, specializationId, specializationRepository);

        Long rankId;
        try {
            rankId = request.getRankId();
        } catch (Exception e) {
            throw new PoorInfoInRequestToCreateUpdateEntity
                    ("Poor information about rank id in request body to create requirement. Must be Long type. " +
                            e.getCause());
        }
        if (rankId < 1L) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Poor information in request body to create requirement. " +
                    "Rank id must be more than 0L");
        }
        RequirementUpdateConvertor.setRankToRequirement(requirement, rankId, rankRepository);

        Long positionId;
        try {
            positionId = request.getPositionId();
        } catch (Exception e) {
            throw new PoorInfoInRequestToCreateUpdateEntity
                    ("Poor information about position id in request body to create requirement. Must be Long type. " +
                            e.getCause());
        }
        if (positionId < 1L) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Poor information in request body to create requirement. " +
                    "Position id must be more than 0L");
        }
        RequirementUpdateConvertor.setPositionToRequirement(requirement, positionId, positionRepository);

        Long locationOfferedId;
        try {
            locationOfferedId = request.getLocationOfferedId();
        } catch (Exception e) {
            throw new PoorInfoInRequestToCreateUpdateEntity
                    ("Poor information about location id in request body to create requirement. Must be Long type. " +
                            e.getCause());
        }
        if (locationOfferedId < 1L) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Poor information in request body to create requirement. " +
                    "Location id must be more than 0L");
        }
        RequirementUpdateConvertor.setLocationToRequirement(requirement, positionId, locationRepository);

        requirement.setCreated(Timestamp.valueOf(LocalDateTime.now()));

        return doConvert(request, requirement);
    }

    static void setCompanyToRequirement(Requirement requirement, Long companyId, CompanyRepository companyRepository) {
        Optional<Company> optionalCompany;
        try {
            optionalCompany = companyRepository.findById(companyId);
        } catch (Exception e) {
            throw new EntityNotFoundException("Can not get company by id from DB. " + e.getCause());
        }
        Company company = optionalCompany.orElseThrow(() -> new NoSuchElementException("No company with such id"));
        requirement.setCompany(company);
    }
}

