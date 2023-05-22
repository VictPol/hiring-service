package com.hirix.controller.requests.create;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Validated
public class UserCreateRequest {
    @NotNull
    @Size(min = 10, max = 360)
    private String email;

    @NotNull
    @Size(min = 6, max = 25)
    private String password;

    @NotNull
    @Size(min = 2, max = 50)
    private String nickName;
}
