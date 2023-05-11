package com.hirix.controller.requests.update;

import com.hirix.controller.requests.create.SkillCreateRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class SkillUpdateRequest extends SkillCreateRequest {
    private Long id;
}
