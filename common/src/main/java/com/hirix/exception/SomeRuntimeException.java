package com.hirix.exception;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SomeRuntimeException extends RuntimeException {

    public SomeRuntimeException() {
    }

    public SomeRuntimeException(String message) {
        super(message);
    }

    public SomeRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public SomeRuntimeException(Throwable cause) {
        super(cause);
    }

    public SomeRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
