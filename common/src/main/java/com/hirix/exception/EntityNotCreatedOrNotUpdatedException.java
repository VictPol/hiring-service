package com.hirix.exception;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EntityNotCreatedOrNotUpdatedException extends Exception {

    public EntityNotCreatedOrNotUpdatedException() {
    }

    public EntityNotCreatedOrNotUpdatedException(String message) {
        super(message);
    }

    public EntityNotCreatedOrNotUpdatedException(String message, Throwable cause) {
        super(message, cause);
    }

    public EntityNotCreatedOrNotUpdatedException(Throwable cause) {
        super(cause);
    }

    public EntityNotCreatedOrNotUpdatedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
