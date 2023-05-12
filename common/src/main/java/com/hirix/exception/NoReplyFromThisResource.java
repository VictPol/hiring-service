package com.hirix.exception;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class NoReplyFromThisResource extends RuntimeException {

    public NoReplyFromThisResource() {
    }

    public NoReplyFromThisResource(String message) {
        super(message);
    }

    public NoReplyFromThisResource(String message, Throwable cause) {
        super(message, cause);
    }

    public NoReplyFromThisResource(Throwable cause) {
        super(cause);
    }

    public NoReplyFromThisResource(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
