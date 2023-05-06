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
        "company", "industry", "profession", "specialization",
        "rank", "position", "location", "offers"
})
@ToString(exclude = {
        "company", "industry", "profession", "specialization",
        "rank", "position", "location", "offers"
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
    private boolean active;
    @Column
    private Integer recommendations;
    @Column
    private String equipments;
    @Column
    private Integer salary;
    @Column
    private Integer term;
    @Column
    private Timestamp created;
    @Column
    private Timestamp changed;
    @Column(name = "is_deleted")
    private boolean deleted;
    @ManyToOne
    @JoinColumn(name = "company_id")
    @JsonManagedReference
    private Company company;
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
    @ManyToOne
    @JoinColumn(name = "location_id")
    @JsonManagedReference
    private Location locationOffered;
    @OneToMany(mappedBy = "requirement", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = false)
    @JsonBackReference
    private Set<Offer> offers = Collections.emptySet();

}

