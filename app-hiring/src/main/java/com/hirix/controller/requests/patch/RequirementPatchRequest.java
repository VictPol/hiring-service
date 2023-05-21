package com.hirix.controller.requests.patch;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Validated
public class RequirementPatchRequest {
    private Long id;
    private Integer experience;
    private boolean active;
    private Integer recommendations;
    private String equipments;
    private Integer salary;
    private Integer term;
    //    private Long companyId;
    private Long industryId;
    private Long professionId;
    private Long specializationId;
    private Long rankId;
    private Long positionId;
    private Long locationOfferedId;
}
