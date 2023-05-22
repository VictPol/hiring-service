package com.hirix.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.cache.annotation.Cacheable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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
        "employees", "companies", "skills", "requirements"
})
@ToString(exclude = {
        "employees", "companies", "skills", "requirements"
})
@Entity
@Cacheable("locations")
@javax.persistence.Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "locations")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "global_region")
    private String globalRegion;

    @Column
    private String country;

    @Column(name = "local_region")
    private String localRegion;

    @Column
    private String city;

    @Column(name = "is_countryside")
    private boolean locationIsCountryside;

    @Column
    @JsonIgnore
    private Timestamp created;

    @Column
    @JsonIgnore
    private Timestamp changed;

    @Column(name = "is_deleted")
    @JsonIgnore
    private boolean deleted;

    @Column(name = "is_visible")
    @JsonIgnore
    private boolean visible;

    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = false)
    @JsonBackReference
//    @JsonManagedReference
//    @JsonIgnoreProperties("location")
    private Set<Employee> employees = Collections.emptySet();

    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = false)
    @JsonBackReference
//    @JsonManagedReference
//    @JsonIgnoreProperties("location")
    private Set<Company> companies = Collections.emptySet();

    @OneToMany(mappedBy = "locationOffered", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = false)
    @JsonBackReference
//    @JsonManagedReference
//    @JsonIgnoreProperties("locationOffered")
    private Set<Requirement> requirements = Collections.emptySet();

    @ManyToMany
    @JoinTable(name = "l_skills_locations",
            joinColumns = @JoinColumn(name = "location_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    @JsonBackReference
//    @JsonManagedReference
//    @JsonIgnoreProperties("locationsDesired")
    private Set<Skill> skills = Collections.emptySet();
}
