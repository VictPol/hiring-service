package com.hirix.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
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
        "company", "industry", "profession", "specialization",
        "rank", "position", "locationOffered", "offers"
})
@ToString(exclude = {
        "company", "industry", "profession", "specialization",
        "rank", "position", "locationOffered", "offers"
})
@Entity
@Table(name = "requirements")
public class Requirement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Integer experience;

    @Column(name = "must_be_active")
    private boolean active = true;

    @Column
    private Integer recommendations;

    @Column
    private String equipments;

    @Column
    private Integer salary;

    @Column
    private Integer term;

    @Column
    @JsonIgnore
    private Timestamp created;

    @Column
    @JsonIgnore
    private Timestamp changed;

    @Column(name = "is_deleted")
    @JsonIgnore
    private boolean deleted;

    @ManyToOne
    @JoinColumn(name = "company_id")
//    @JsonBackReference
//    @JsonManagedReference
    @JsonIgnoreProperties("requirements")
    private Company company;

    @ManyToOne
    @JoinColumn(name = "industry_id")
//    @JsonBackReference
    @JsonManagedReference
//    @JsonIgnoreProperties("requirements")
    private Industry industry;

    @ManyToOne
    @JoinColumn(name = "profession_id")
//    @JsonBackReference
    @JsonManagedReference
//    @JsonIgnoreProperties("requirements")
    private Profession profession;

    @ManyToOne
    @JoinColumn(name = "specialization_id")
//    @JsonBackReference
    @JsonManagedReference
//    @JsonIgnoreProperties("requirements")
    private Specialization specialization;

    @ManyToOne
    @JoinColumn(name = "rank_id")
//    @JsonBackReference
    @JsonManagedReference
//    @JsonIgnoreProperties("requirements")
    private Rank rank;

    @ManyToOne
    @JoinColumn(name = "position_id")
//    @JsonBackReference
    @JsonManagedReference
//    @JsonIgnoreProperties("requirements")
    private Position position;

    @ManyToOne
    @JoinColumn(name = "location_id")
    @JsonManagedReference
//    @JsonBackReference
//    @JsonIgnoreProperties("requirements")
    private Location locationOffered;

    @OneToMany(mappedBy = "requirement", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = false)
//    @JsonBackReference
//    @JsonManagedReference
    @JsonIgnoreProperties("requirement")
    private Set<Offer> offers = Collections.emptySet();
}

