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

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
        "skills", "requirements"
})
@ToString(exclude = {
        "skills", "requirements"
})
@Entity
@Table(name = "industries")
public class Industry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "industry_name")
    private String industryName;

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

    @OneToMany(mappedBy = "industry", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = false)
//    @JsonManagedReference
    @JsonBackReference
//    @JsonIgnoreProperties("industry")
    private Set<Skill> skills = Collections.emptySet();

    @OneToMany(mappedBy = "industry", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = false)
    //    @JsonManagedReference
    @JsonBackReference
//    @JsonIgnoreProperties("industry")
    private Set<Requirement> requirements = Collections.emptySet();
}
