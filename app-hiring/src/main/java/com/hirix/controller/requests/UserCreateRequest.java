package com.hirix.controller.requests;

import javax.persistence.Column;
import java.sql.Timestamp;

public class UserCreateRequest {
    private String email;
    @Column
    private String password;
    @Column
    private Timestamp created;
    @Column
    private Timestamp changed;
}
