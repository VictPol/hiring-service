package com.hirix.exception;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LongNumberFormatException extends NumberFormatException {

    public LongNumberFormatException() {
    }

    public LongNumberFormatException(String s) {
        super(s);
    }
}
