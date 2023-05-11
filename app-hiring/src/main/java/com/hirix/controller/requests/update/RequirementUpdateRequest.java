package com.hirix.controller.requests.update;

import com.hirix.controller.requests.create.RequirementCreateRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class RequirementUpdateRequest extends RequirementCreateRequest {
    private Long id;
}
