package com.hirix.controller.convertors.offer;


import com.hirix.controller.requests.create.OfferCreateRequest;
import com.hirix.domain.Offer;
import com.hirix.domain.Requirement;
import com.hirix.domain.Skill;
import com.hirix.exception.EntityNotFoundException;
import com.hirix.exception.PoorInfoInRequestToCreateUpdateEntity;
import com.hirix.repository.RequirementRepository;
import com.hirix.repository.SkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OfferCreateConvertor implements Converter<OfferCreateRequest, Offer> {
    private final RequirementRepository requirementRepository;
    private final SkillRepository skillRepository;

    @Override
    public Offer convert(OfferCreateRequest request) {
        Offer offer = new Offer();

        Long skillId;
        try {
            skillId = request.getSkillId();
        } catch (Exception e) {
            throw new PoorInfoInRequestToCreateUpdateEntity
                    ("Poor information about skill id in request body to create offer. Must be Long type. " + e.getCause());
        }
        if (skillId < 1L) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Poor information in request body to create offer. " +
                    "Skill id must be more than 0L");
        }
        Optional<Skill> optionalSkill;
        try {
            optionalSkill = skillRepository.findById(skillId);
        } catch (Exception e) {
            throw new EntityNotFoundException("Can not get skill by id from DB. " + e.getCause());
        }
        Skill skill = optionalSkill.orElseThrow(() -> new NoSuchElementException("No skill with such id"));
        offer.setSkill(skill);

        Long requirementId;
        try {
            requirementId = request.getRequirementId();
        } catch (Exception e) {
            throw new PoorInfoInRequestToCreateUpdateEntity
                    ("Poor information about requirement id in request body to create offer. Must be Long type. " + e.getCause());
        }
        if (requirementId < 1L) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Poor information in request body to create offer. " +
                    "Requirement id must be more than 0L");
        }
        Optional<Requirement> optionalRequirement;
        try {
            optionalRequirement = requirementRepository.findById(requirementId);
        } catch (Exception e) {
            throw new EntityNotFoundException("Can not get requirement by id from DB. " + e.getCause());
        }
        Requirement requirement = optionalRequirement.orElseThrow(() -> new NoSuchElementException("No requirement with such id"));
        offer.setRequirement(requirement);

        offer.setCommentsCompany(request.getCommentsCompany());
        return offer;
    }
}
