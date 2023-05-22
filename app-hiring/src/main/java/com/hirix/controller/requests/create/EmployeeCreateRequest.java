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
public class EmployeeCreateRequest {
    @NotNull
    @Size(min = 2, max = 100)
    private String fullName;

    @NotNull
    @Size(min = 18, max = 23)
    private String birthday;

    @NotNull
    @Size(min = 2, max = 25)
    private String education;

    @NotNull
    @Size(min = 2, max = 25)
    private String health;

    @NotNull
    @Size(min = 2, max = 25)
    private String gender;

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
