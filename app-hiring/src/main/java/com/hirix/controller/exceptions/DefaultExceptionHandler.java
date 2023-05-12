package com.hirix.controller.exceptions;


import com.hirix.exception.ErrorMessage;
import com.hirix.exception.IllegalRequestException;
import com.hirix.exception.LongNumberFormatException;
import com.hirix.exception.NoSuchElementFoundException;
import com.hirix.exception.PoorInfoInRequestToCreateUpdateEntity;
import com.hirix.exception.SomeRuntimeException;
import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.hirix.exception.ApplicationErrorCodes.BAD_REQUEST_USER_CREATE;
import static com.hirix.exception.ApplicationErrorCodes.ENTITY_NOT_FOUND_OR_NOT_SAVED;
import static com.hirix.exception.ApplicationErrorCodes.FATAL_ERROR;
import static com.hirix.exception.ApplicationErrorCodes.ID_IS_NOT_LONG;
import static com.hirix.exception.ApplicationErrorCodes.NO_ENTITY_WITH_SUCH_ID;
import static com.hirix.exception.ApplicationErrorCodes.POOR_INFORMATION_TO_CREATE_UPDATE_ENTITY;
import static com.hirix.exception.ApplicationErrorCodes.SOME_RUNTIME_EXCEPTION;

@ControllerAdvice
@RequiredArgsConstructor
public class DefaultExceptionHandler {
    private static final Logger log = Logger.getLogger(DefaultExceptionHandler.class);
//    private final RandomValuesGenerator generator;

    @ExceptionHandler(PoorInfoInRequestToCreateUpdateEntity.class)
    public ResponseEntity<ErrorMessage> handlePoorInfoInRequestToCreateUpdateEntity(PoorInfoInRequestToCreateUpdateEntity e) {
        String exceptionUniqueId = UUID.randomUUID().toString();
        log.error(exceptionUniqueId + " ," + e.getMessage() + "\n", e);
        return new ResponseEntity<>(
                new ErrorMessage(
                        exceptionUniqueId,
                        POOR_INFORMATION_TO_CREATE_UPDATE_ENTITY.getCodeId(),
                        e.getMessage()
                ),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorMessage> handleNoSuchElementException(NoSuchElementException e) {
        String exceptionUniqueId = UUID.randomUUID().toString();
        log.error(exceptionUniqueId + " ," + e.getMessage() + "\n", e);
        return new ResponseEntity<>(
                new ErrorMessage(
                        exceptionUniqueId,
                        NO_ENTITY_WITH_SUCH_ID.getCodeId(),
                        e.getMessage()
                ),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(LongNumberFormatException.class)
    public ResponseEntity<ErrorMessage> handleLongNumberFormatException(LongNumberFormatException e) {
        String exceptionUniqueId = UUID.randomUUID().toString();
        log.error(exceptionUniqueId + " ," + e.getMessage() + "\n", e);
        return new ResponseEntity<>(
                new ErrorMessage(
                        exceptionUniqueId,
                        ID_IS_NOT_LONG.getCodeId(),
                        e.getMessage()
                ),
                HttpStatus.BAD_REQUEST);
    }

@ExceptionHandler(SomeRuntimeException.class)
public ResponseEntity<ErrorMessage> handleSomeRuntimeException(SomeRuntimeException e) {
    String exceptionUniqueId = UUID.randomUUID().toString();
    log.error(exceptionUniqueId + " ," + e.getMessage() + "\n", e);
    return new ResponseEntity<>(
            new ErrorMessage(
                    exceptionUniqueId,
                    ENTITY_NOT_FOUND_OR_NOT_SAVED.getCodeId(),
//                    SOME_RUNTIME_EXCEPTION.getCodeId(),
                    e.getMessage()
            ),
            HttpStatus.UNPROCESSABLE_ENTITY);
}

    @ExceptionHandler(IllegalRequestException.class)
    public ResponseEntity<ErrorMessage> handleIllegalRequestException(IllegalRequestException e) {
//        String exceptionUniqueId = generator.uuidGenerator();
        String exceptionUniqueId = UUID.randomUUID().toString();

        BindingResult bindingResult = e.getBindingResult();
        String collect = bindingResult
                .getAllErrors()
                .stream()
                .map(ObjectError::toString)
                .collect(Collectors.joining(","));

        log.error(exceptionUniqueId + e.getMessage(), e);

        return new ResponseEntity<>(
                new ErrorMessage(
                        exceptionUniqueId,
                        BAD_REQUEST_USER_CREATE.getCodeId(),
                        collect
                ),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorMessage> handleRuntimeException(RuntimeException e) {
        /* Handles all other exceptions. Status code 500. */

        String exceptionUniqueId = UUID.randomUUID().toString();
//        String exceptionUniqueId = generator.uuidGenerator();

        log.error(exceptionUniqueId + e.getMessage() + "\n", e);

        return new ResponseEntity<>(
                new ErrorMessage(
                        exceptionUniqueId,
                        SOME_RUNTIME_EXCEPTION.getCodeId(),
                        e.getMessage()
                ),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> handleOthersException(Exception e) {
        /* Handles all other exceptions. Status code 500. */

//        String exceptionUniqueId = generator.uuidGenerator();
        String exceptionUniqueId = UUID.randomUUID().toString();

        log.error(exceptionUniqueId + e.getMessage() + "\n", e);

        return new ResponseEntity<>(
                new ErrorMessage(
                        exceptionUniqueId,
                        FATAL_ERROR.getCodeId(),
                        e.getMessage()
                ),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
