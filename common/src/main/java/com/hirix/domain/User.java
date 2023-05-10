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
import javax.persistence.ManyToMany;
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
        "employee", "company", "roles"
})
@ToString(exclude = {
        "employee", "company", "roles"
})
@Entity
@Table (name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String email;
    @Column
    @JsonIgnore
    private String password;
    @Column(name = "nick_name")
    private String nickName;
    @Column
    @JsonIgnore
    private Timestamp created;
    @Column
    @JsonIgnore
    private Timestamp changed;
    @Column (name = "is_deleted")
    @JsonIgnore
    private boolean isDeleted;
    @Column (name = "is_visible")
    @JsonIgnore
    private boolean isVisible;
    @Column (name = "is_locked")
    private boolean isLocked;
    @Column (name = "is_expired")
    private boolean isExpired;
//    @Column (name = "locked")
//    private Timestamp locked;
//    @Column (name = "expired")
//    private Timestamp expired;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = false)
//    @JsonManagedReference
//    @JsonBackReference
    @JsonIgnoreProperties("user")
    private Employee employee;
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = false)
//    @JsonManagedReference
//    @JsonBackReference
    @JsonIgnoreProperties("user")
    private Company company;
    @ManyToMany(mappedBy = "users", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//    @JsonManagedReference
//    @JsonBackReference
    @JsonIgnoreProperties("users")
    private Set<Role> roles = Collections.emptySet();

}
