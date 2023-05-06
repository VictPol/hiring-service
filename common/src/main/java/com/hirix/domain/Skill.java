package com.hirix.domain;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@EqualsAndHashCode(exclude = {
        "employee", "industry", "profession", "specialization", "rank",
        "position", "locationsDesired", "offers"
})
@ToString(exclude = {
        "employee", "industry", "profession", "specialization", "rank",
        "position", "locationsDesired", "offers"
})
@Entity
@Table(name = "skills")
public class Skill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private Integer experience;
    @Column(name = "is_active")
    private boolean active;
    @Column
    private Integer recommendations;
    @Column
    private String equipments;
    @Column(name = "salary_min")
    private Integer salaryMin;
    @Column(name = "salary_max")
    private Integer salaryMax;
    @Column(name = "term_min")
    private Integer termMin;
    @Column(name = "term_max")
    private Integer termMax;
    @Column
    private Timestamp created;
    @Column
    private Timestamp changed;
    @Column(name = "is_deleted")
    private boolean deleted;
    @ManyToOne
    @JoinColumn(name = "employee_id")
    @JsonManagedReference
    private Employee employee;
    @ManyToOne
    @JoinColumn(name = "industry_id")
    @JsonManagedReference
    private Industry industry;
    @ManyToOne
    @JoinColumn(name = "profession_id")
    @JsonManagedReference
    private Profession profession;
    @ManyToOne
    @JoinColumn(name = "specialization_id")
    @JsonManagedReference
    private Specialization specialization;
    @ManyToOne
    @JoinColumn(name = "rank_id")
    @JsonManagedReference
    private Rank rank;
    @ManyToOne
    @JoinColumn(name = "position_id")
    @JsonManagedReference
    private Position position;
    @ManyToMany(mappedBy = "skills", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    private Set<Location> locationsDesired = Collections.emptySet();
    @OneToMany(mappedBy = "skill", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = false)
    @JsonBackReference
    private Set<Offer> offers = Collections.emptySet();

}

