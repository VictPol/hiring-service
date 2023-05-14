package com.hirix.exception;

public class ConvertRequestToEntityException extends RuntimeException {

    public ConvertRequestToEntityException() {
    }

    public ConvertRequestToEntityException(String message) {
        super(message);
    }

    public ConvertRequestToEntityException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConvertRequestToEntityException(Throwable cause) {
        super(cause);
    }

    public ConvertRequestToEntityException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
