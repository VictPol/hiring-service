package com.hirix.controller.requests.create;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class SkillCreateRequest {
    private Integer experience;
    private boolean active;

    private Integer recommendations;

    private String equipments;

    private Integer salaryMin;

    private Integer salaryMax;

    private Integer termMin;

    private Integer termMax;

    private Long employeeId;

    private Long industryId;

    private Long professionId;

    private Long specializationId;

    private Long rankId;

    private Long positionId;
}
