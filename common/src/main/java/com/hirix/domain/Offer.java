package com.hirix.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.sql.Timestamp;

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
    @JsonIgnore
    private Timestamp created;

    @Column
    @JsonIgnore
    private Timestamp changed;

    @Column(name = "is_deleted")
    @JsonIgnore
    private boolean deleted;

    @Column(name = "is_accepted")
    @JsonIgnore
    private boolean accepted;

    @Column(name = "is_contracted")
    private boolean contracted;

    @Column(name = "comments_employee")
    private String commentsEmployee = "NO_COMMENTS";

    @Column(name = "comments_company")
    private String commentsCompany = "NO_COMMENTS";

    @ManyToOne
    @JoinColumn(name = "skill_id")
//    @JsonManagedReference
//    @JsonBackReference
    @JsonIgnoreProperties("offers")
    private Skill skill;

    @ManyToOne
    @JoinColumn(name = "requirement_id")
//    @JsonManagedReference
//    @JsonBackReference
    @JsonIgnoreProperties("offers")
    private Requirement requirement;
}
