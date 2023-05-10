package com.hirix.controller.requests.update;

import com.hirix.controller.requests.create.RoleCreateRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class RoleUpdateRequest extends RoleCreateRequest {
    private Long id;
}

