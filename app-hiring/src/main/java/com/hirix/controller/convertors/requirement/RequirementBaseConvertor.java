package com.hirix.controller.convertors.requirement;

import com.hirix.controller.requests.create.RequirementCreateRequest;
import com.hirix.domain.Requirement;
import com.hirix.exception.PoorInfoInRequestToCreateUpdateEntity;
import org.springframework.core.convert.converter.Converter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public abstract class RequirementBaseConvertor <S, T> implements Converter<S, T> {
    public Requirement doConvert(RequirementCreateRequest request, Requirement skillForSave) {
        try {
            skillForSave.setExperience(request.getExperience());
            skillForSave.setActive(request.isActive());
            skillForSave.setRecommendations(request.getRecommendations());
            skillForSave.setEquipments(request.getEquipments());
            skillForSave.setSalary(request.getSalary());
            skillForSave.setTerm(request.getTerm());
        } catch (Exception e) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Poor information in request body to create requirement. " +
                    e.getCause());
        }
        if (skillForSave.getSalary() == null || skillForSave.getSalary() < 0 ||
            skillForSave.getTerm() == null || skillForSave.getTerm() < 0) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Poor information in request body to create requirement");
        }
        skillForSave.setChanged(Timestamp.valueOf(LocalDateTime.now()));

        return skillForSave;
    }
}
