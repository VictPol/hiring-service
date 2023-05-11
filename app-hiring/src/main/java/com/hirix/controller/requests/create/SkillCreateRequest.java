package com.hirix.controller.requests.create;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.hirix.domain.Employee;
import com.hirix.domain.Industry;
import com.hirix.domain.Location;
import com.hirix.domain.Offer;
import com.hirix.domain.Position;
import com.hirix.domain.Profession;
import com.hirix.domain.Rank;
import com.hirix.domain.Specialization;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.Set;

@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class SkillCreateRequest {
    private Integer experience;
    private boolean active;
    private Integer recommendations;
    private String equipments;
    private Integer salaryMin;
    private Integer salaryMax;
    private Integer termMin;
    private Integer termMax;
    private Long employeeId;
    private Long industryId;
    private Long professionId;
    private Long specializationId;
    private Long rankId;
    private Long positionId;
}
