package com.hirix.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
import javax.persistence.OneToOne;
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
        "user", "location", "requirements"
})
@ToString(exclude = {
        "user", "location", "requirements"
})
@Entity
@Table(name = "companies")
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "full_title")
    private String fullTitle;
    @Column(name = "short_title")
    private String shortTitle;
    @Column(name = "reg_number")
    private String regNumber;
    @Column(name = "org_type")
    private String orgType;
    @Column
    @JsonIgnore
    private Timestamp created;
    @Column
    @JsonIgnore
    private Timestamp changed;
    @Column(name = "is_deleted")
    @JsonIgnore
    private boolean deleted;
    @OneToOne
    @JoinColumn(name = "user_id")
//    @JsonBackReference
//    @JsonManagedReference
    @JsonIgnoreProperties("company")
    @JsonIgnore
    private User user;
    @ManyToOne
    @JoinColumn(name = "location_id")
//    @JsonBackReference
    @JsonManagedReference
//    @JsonIgnoreProperties("company")
    private Location location;
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = false)
//    @JsonBackReference
//    @JsonManagedReference
    @JsonIgnoreProperties("company")
    private Set<Requirement> requirements = Collections.emptySet();
}
