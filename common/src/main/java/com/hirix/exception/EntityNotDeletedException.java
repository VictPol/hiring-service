package com.hirix.exception;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EntityNotDeletedException extends Exception {

    public EntityNotDeletedException() {
    }

    public EntityNotDeletedException(String message) {
        super(message);
    }

    public EntityNotDeletedException(String message, Throwable cause) {
        super(message, cause);
    }

    public EntityNotDeletedException(Throwable cause) {
        super(cause);
    }

    public EntityNotDeletedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
