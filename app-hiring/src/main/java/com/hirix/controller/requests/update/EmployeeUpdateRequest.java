package com.hirix.controller.requests.update;

import com.hirix.controller.requests.create.EmployeeCreateRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class EmployeeUpdateRequest extends EmployeeCreateRequest {
    private Long id;
}
