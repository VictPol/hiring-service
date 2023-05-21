package com.hirix.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.BindingResult;

@Setter
@Getter
public class IllegalRequestException extends RuntimeException {
    private BindingResult bindingResult;

    public IllegalRequestException(BindingResult bindingResult) {
        this.bindingResult = bindingResult;
    }

    public IllegalRequestException(String message, BindingResult bindingResult) {
        super(message);
        this.bindingResult = bindingResult;
    }
}
