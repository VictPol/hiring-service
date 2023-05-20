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
public class EmployeePatchRequest {
    @NotNull
    @NumberFormat(style = NumberFormat.Style.NUMBER, pattern = "1")
    @Min(value = 1L)
    @Max(value = 9223372036854775807L)
    private Long id;
    @Size(min = 2, max = 50)
    private String fullName;
    @Size(min = 19, max = 25)
    private String birthday;
    @Size(min = 2, max = 20)
    private String education;
    @Size(min = 2, max = 20)
    private String health;
    @Size(min = 2, max = 20)
    private String gender;
//    @NumberFormat(style = NumberFormat.Style.NUMBER, pattern = "1")
//    @Min(value = 1L)
//    @Max(value = 9223372036854775807L)
//    private Long userId;
    @NumberFormat(style = NumberFormat.Style.NUMBER, pattern = "1")
    @Min(value = 1L)
    @Max(value = 9223372036854775807L)
    private Long locationId;
}
