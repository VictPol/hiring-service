package com.hirix.controller.requests.update;

import com.hirix.controller.requests.create.CompanyCreateRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class CompanyUpdateRequest extends CompanyCreateRequest {
    private Long id;
}
