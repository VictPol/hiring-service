package com.hirix.exception;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PoorInfoInRequestToCreateEntity extends RuntimeException {
    public PoorInfoInRequestToCreateEntity() {
    }

    public PoorInfoInRequestToCreateEntity(String message) {
        super(message);
    }

    public PoorInfoInRequestToCreateEntity(String message, Throwable cause) {
        super(message, cause);
    }
}
