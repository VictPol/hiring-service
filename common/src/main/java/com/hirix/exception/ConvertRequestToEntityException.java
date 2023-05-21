package com.hirix.exception;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ConvertRequestToEntityException extends RuntimeException {

    public ConvertRequestToEntityException(String message) {
        super(message);
    }
}
