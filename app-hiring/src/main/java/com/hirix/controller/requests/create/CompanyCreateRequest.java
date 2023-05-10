package com.hirix.controller.requests.create;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class CompanyCreateRequest {
    private String fullTitle;
    private String shortTitle;
    private String regNumber;
    private String orgType;
    private Long userId;
    private Long locationId;
}
