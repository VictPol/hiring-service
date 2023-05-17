package com.hirix.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter

@Entity
@Table(name = "l_skills_locations")
public class LinkSkillsLocations {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "skill_id")
    private Long skillId;
    @Column(name = "location_id")
    private Long locationId;
    @Column
    @JsonIgnore
    private Timestamp created;
    @Column
    @JsonIgnore
    private Timestamp changed;
    @Column(name = "is_deleted")
    @JsonIgnore
    private boolean deleted;

//    @ManyToOne
//    @JoinColumn(name = "skill_id")
//    @JsonBackReference
////    @JsonManagedReference
////    @JsonIgnoreProperties("skills")
//    private Skill skill;
}

