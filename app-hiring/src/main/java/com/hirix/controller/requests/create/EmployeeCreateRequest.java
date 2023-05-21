package com.hirix.controller.requests.create;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class EmployeeCreateRequest {
    private String fullName;

    private String birthday;

    private String education;

    private String health;

    private String gender;

    private Long userId;

    private Long locationId;
}
