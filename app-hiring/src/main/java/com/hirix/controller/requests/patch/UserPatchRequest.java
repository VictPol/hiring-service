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
public class UserPatchRequest {
    @NotNull
    @NumberFormat(style = NumberFormat.Style.NUMBER, pattern = "1")
    @Min(value = 1L)
    @Max(value = 9223372036854775807L)
    private Long id;

    @Size(min = 10, max = 360)
    private String email;

    @Size(min = 6, max = 25)
    private String password;

    @Size(min = 2, max = 50)
    private String nickName;
}

