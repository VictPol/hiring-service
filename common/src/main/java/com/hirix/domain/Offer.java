package com.hirix.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
        "skill", "requirement"
})
@ToString(exclude = {
        "skill", "requirement"
})
@Entity
@Table(name = "offers")
public class Offer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private Timestamp created;
    @Column
    private Timestamp changed;
    @Column(name = "is_deleted")
    private boolean deleted;
    @Column(name = "is_accepted")
    private boolean accepted;
    @Column(name = "is_contracted")
    private boolean contracted;
    @Column(name = "comments_employee")
    private String commentsEmployee = "NO_COMMENTS";
    @Column(name = "comments_company")
    private String commentsCompany = "NO_COMMENTS";
    @ManyToOne
    @JoinColumn(name = "skills_id")
    @JsonManagedReference
    private Skill skill;
    @ManyToOne
    @JoinColumn(name = "requirements_id")
    @JsonManagedReference
    private Requirement requirement;

}
