package com.hirix.exception;

import lombok.Getter;
import lombok.Setter;

import java.util.NoSuchElementException;


@Setter
@Getter
public class NoSuchElementFoundException extends NoSuchElementException {

    public NoSuchElementFoundException() {
    }

    public NoSuchElementFoundException(String s) {
        super(s);
    }
}
