package com.hirix.controller.convertors.skill;

import com.hirix.controller.requests.create.SkillCreateRequest;
import com.hirix.domain.Skill;
import com.hirix.exception.PoorInfoInRequestToCreateUpdateEntity;
import org.springframework.core.convert.converter.Converter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public abstract class SkillBaseConvertor<S, T> implements Converter<S, T> {
    public Skill doConvert(SkillCreateRequest request, Skill skillForSave) {
        try {
            skillForSave.setExperience(request.getExperience());
            skillForSave.setActive(request.isActive());
            skillForSave.setRecommendations(request.getRecommendations());
            skillForSave.setEquipments(request.getEquipments());
            skillForSave.setSalaryMin(request.getSalaryMin());
            skillForSave.setSalaryMax(request.getSalaryMax());
            skillForSave.setTermMin(request.getTermMin());
            skillForSave.setTermMax(request.getTermMax());
        } catch (Exception e) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Poor information in request body to create skill. " +
                    e.getCause());
        }
        if (skillForSave.getSalaryMin() == 0 ||
            skillForSave.getSalaryMax() == 0 ||
            skillForSave.getTermMin() == 0 ||
            skillForSave.getTermMax() == 0) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Poor information in request body to create skill");
        }
        skillForSave.setChanged(Timestamp.valueOf(LocalDateTime.now()));

        return skillForSave;
    }
}
