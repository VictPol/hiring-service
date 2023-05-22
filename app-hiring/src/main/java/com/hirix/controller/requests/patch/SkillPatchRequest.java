package com.hirix.controller.requests.patch;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Validated
public class SkillPatchRequest {
    @NotNull
    @NumberFormat(style = NumberFormat.Style.NUMBER, pattern = "1")
    @Min(value = 1L)
    @Max(value = 9223372036854775807L)
    private Long id;

    @NumberFormat(style = NumberFormat.Style.NUMBER, pattern = "1")
    @Min(value = 0)
    @Max(value = 100)
    private Integer experience;

    private boolean active;

    @NumberFormat(style = NumberFormat.Style.NUMBER, pattern = "1")
    @Min(value = 0)
    @Max(value = 100)
    private Integer recommendations;

    @Size(min = 2, max = 250)
    private String equipments;

    @NumberFormat(style = NumberFormat.Style.NUMBER, pattern = "1")
    @Min(value = 1)
    @Max(value = 10000000)
    private Integer salaryMin;

    @NumberFormat(style = NumberFormat.Style.NUMBER, pattern = "1")
    @Min(value = 1)
    @Max(value = 10000000)
    private Integer salaryMax;

    @NumberFormat(style = NumberFormat.Style.NUMBER, pattern = "1")
    @Min(value = 1)
    @Max(value = 1000)
    private Integer termMin;

    @NumberFormat(style = NumberFormat.Style.NUMBER, pattern = "1")
    @Min(value = 1)
    @Max(value = 1000)
    private Integer termMax;

    @NumberFormat(style = NumberFormat.Style.NUMBER, pattern = "1")
    @Min(value = 1L)
    @Max(value = 9223372036854775807L)
    private Long industryId;

    @NumberFormat(style = NumberFormat.Style.NUMBER, pattern = "1")
    @Min(value = 1L)
    @Max(value = 9223372036854775807L)
    private Long professionId;

    @NumberFormat(style = NumberFormat.Style.NUMBER, pattern = "1")
    @Min(value = 1L)
    @Max(value = 9223372036854775807L)
    private Long specializationId;

    @NumberFormat(style = NumberFormat.Style.NUMBER, pattern = "1")
    @Min(value = 1L)
    @Max(value = 9223372036854775807L)
    private Long rankId;

    @NumberFormat(style = NumberFormat.Style.NUMBER, pattern = "1")
    @Min(value = 1L)
    @Max(value = 9223372036854775807L)
    private Long positionId;
}
