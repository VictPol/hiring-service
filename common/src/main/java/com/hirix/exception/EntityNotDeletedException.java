package com.hirix.exception;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EntityNotDeletedException extends Exception {

    public EntityNotDeletedException(String message) {
        super(message);
    }
}
