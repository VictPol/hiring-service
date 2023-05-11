package com.hirix.controller.requests.create;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hirix.domain.Requirement;
import com.hirix.domain.Skill;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class OfferCreateRequest {
    private String commentsCompany;
    private Long skillId;
    private Long requirementId;
}
