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
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter

@EqualsAndHashCode(exclude = {
        "employee"
})
@ToString(exclude = {
        "employee"
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
    private String password;
    @Column
    private Timestamp created;
    @Column
    private Timestamp changed;
    @Column (name = "is_deleted")
    private boolean isDeleted;
    @Column (name = "is_visible")
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
    @JsonManagedReference
    private Employee employee;

}
