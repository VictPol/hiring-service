package com.hirix.controller.requests.patch;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;

import javax.persistence.ElementCollection;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Validated
public class CompanyPatchRequest {
    @NotNull
//    @ElementCollection(targetClass = Long.class)
    @NumberFormat(style = NumberFormat.Style.NUMBER, pattern = "1")
    @Min(value = 1L)
    @Max(value = 9223372036854775807L)
    private Long id;
//    @ElementCollection(targetClass = String.class)
    @Size(min = 2, max = 50)
    private String fullTitle;
//    @ElementCollection(targetClass = String.class)
    @Size(min = 2, max = 20)
    private String shortTitle;
//    @ElementCollection(targetClass = String.class)
    @Size(min = 2, max = 20)
    private String regNumber;
//    @ElementCollection(targetClass = String.class)
    @Size(min = 2, max = 20)
    private String orgType;
//    @ElementCollection(targetClass = Long.class)
    @NumberFormat(style = NumberFormat.Style.NUMBER, pattern = "1")
    @Min(value = 1L)
    @Max(value = 9223372036854775807L)
    private Long userId;
//    @ElementCollection(targetClass = Long.class)
    @NumberFormat(style = NumberFormat.Style.NUMBER, pattern = "1")
    @Min(value = 1L)
    @Max(value = 9223372036854775807L)
    private Long locationId;
}
