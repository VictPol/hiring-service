package com.hirix.exception;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EntityNotCreatedOrNotUpdatedException extends Exception {

    public EntityNotCreatedOrNotUpdatedException(String message) {
        super(message);
    }
}
