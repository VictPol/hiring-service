package com.hirix.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.hirix.domain.enums.Education;
import com.hirix.domain.enums.Gender;
import com.hirix.domain.enums.Health;
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
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
        "user", "location", "skills"
})
@ToString(exclude = {
        "user", "location", "skills"
})
@Entity
@Table(name = "employees")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name")
    private String fullName;

    @Column
    private Timestamp birthday;

    @Column
    @Enumerated(EnumType.STRING)
    private Education education = Education.NOT_SELECTED;

    @Column
    @Enumerated(EnumType.STRING)
    private Health health = Health.NOT_SELECTED;

    @Column
    @Enumerated(EnumType.STRING)
    private Gender gender = Gender.NOT_SELECTED;

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
    @JsonIgnoreProperties("employee")
    @JsonIgnore
    private User user;

    @ManyToOne
    @JoinColumn(name = "location_id")
//    @JsonBackReference
    @JsonManagedReference
//    @JsonIgnoreProperties("employees")
    private Location location;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = false)
//    @JsonBackReference
//    @JsonManagedReference
    @JsonIgnoreProperties("employee")
    private Set<Skill> skills = Collections.emptySet();
}
