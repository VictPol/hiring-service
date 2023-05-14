package com.hirix.controller.convertors;

import com.hirix.controller.requests.create.CompanyCreateRequest;
import com.hirix.domain.Company;
import com.hirix.exception.PoorInfoInRequestToCreateUpdateEntity;
import org.springframework.core.convert.converter.Converter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public abstract class CompanyBaseConvertor<S, T> implements Converter<S, T> {
    public Company doConvert(CompanyCreateRequest request, Company companyForSave) {
        try {
            companyForSave.setFullTitle(request.getFullTitle());
            companyForSave.setShortTitle(request.getShortTitle());
            companyForSave.setRegNumber(request.getRegNumber());
            companyForSave.setOrgType(request.getOrgType());
        } catch (Exception e) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Poor information in request body to create company",
                    e.getCause());
        }
        if (companyForSave.getFullTitle() == null ||
            companyForSave.getShortTitle() == null ||
            companyForSave.getRegNumber() == null ||
            companyForSave.getOrgType() == null) {
            throw new PoorInfoInRequestToCreateUpdateEntity("Poor information in request body to create company");
        }
        companyForSave.setChanged(Timestamp.valueOf(LocalDateTime.now()));
        return companyForSave;
    }

}
