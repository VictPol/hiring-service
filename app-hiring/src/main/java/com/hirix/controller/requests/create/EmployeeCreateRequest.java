package com.hirix.controller.requests.create;

import com.hirix.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import java.sql.Timestamp;

@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class EmployeeCreateRequest {
    private String fullName;
    private Timestamp birthday;
    private String education;
    private String health;
    private String gender;
    private Timestamp created;
    private Timestamp changed;
    private boolean deleted;
//    private UserCreateRequest user;
//    private Location location;
//    private Set<Skill> skills = Collections.emptySet();
}
