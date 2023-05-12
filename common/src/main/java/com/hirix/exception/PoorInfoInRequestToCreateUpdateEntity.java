package com.hirix.exception;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PoorInfoInRequestToCreateUpdateEntity extends RuntimeException {
    public PoorInfoInRequestToCreateUpdateEntity() {
    }

    public PoorInfoInRequestToCreateUpdateEntity(String message) {
        super(message);
    }

    public PoorInfoInRequestToCreateUpdateEntity(String message, Throwable cause) {
        super(message, cause);
    }
}
