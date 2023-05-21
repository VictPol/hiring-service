package com.hirix.controller.requests.create;

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
public class CompanyCreateRequest {
    @NotNull
    @Size(min = 2, max = 50)
    private String fullTitle;

    @NotNull
    @Size(min = 2, max = 20)
    private String shortTitle;

    @NotNull
    @Size(min = 2, max = 20)
    private String regNumber;

    @NotNull
    @Size(min = 2, max = 20)
    private String orgType;

    @NotNull
    @NumberFormat(style = NumberFormat.Style.NUMBER, pattern = "1")
    @Min(value = 1L)
    @Max(value = 9223372036854775807L)
    private Long userId;

    @NotNull
    @NumberFormat(style = NumberFormat.Style.NUMBER, pattern = "1")
    @Min(value = 1L)
    @Max(value = 9223372036854775807L)
    private Long locationId;
}
