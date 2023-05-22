package com.hirix.controller.exceptions;


import com.hirix.exception.ConvertRequestToEntityException;
import com.hirix.exception.EntityNotCreatedOrNotUpdatedException;
import com.hirix.exception.EntityNotDeletedException;
import com.hirix.exception.EntityNotFoundException;
import com.hirix.exception.ErrorMessage;
import com.hirix.exception.IllegalRequestException;
import com.hirix.exception.PoorInfoInRequestToCreateUpdateEntity;
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

import static com.hirix.exception.ApplicationErrorCodes.BAD_ARGUMENTS_IN_SEARCH_PATH;
import static com.hirix.exception.ApplicationErrorCodes.BAD_REQUEST_SEE_DETAILS;
import static com.hirix.exception.ApplicationErrorCodes.CAN_NOT_CONVERT_REQUEST_TO_ENTITY;
import static com.hirix.exception.ApplicationErrorCodes.ENTITY_NOT_CREATED_OR_NOT_UPDATED;
import static com.hirix.exception.ApplicationErrorCodes.ENTITY_NOT_DELETED;
import static com.hirix.exception.ApplicationErrorCodes.ENTITY_NOT_FOUND;
import static com.hirix.exception.ApplicationErrorCodes.FATAL_ERROR;
import static com.hirix.exception.ApplicationErrorCodes.ID_IS_NOT_REQUIRED_NUMBER_FORMAT;
import static com.hirix.exception.ApplicationErrorCodes.NO_ENTITY_WITH_SUCH_ID;
import static com.hirix.exception.ApplicationErrorCodes.NULL_POINTER;
import static com.hirix.exception.ApplicationErrorCodes.POOR_INFORMATION_TO_CREATE_UPDATE_ENTITY;
import static com.hirix.exception.ApplicationErrorCodes.RUNTIME_EXCEPTION;

@ControllerAdvice
@RequiredArgsConstructor
public class DefaultExceptionHandler {

    private static final Logger log = Logger.getLogger(DefaultExceptionHandler.class);

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorMessage> handleIllegalArgumentException(IllegalArgumentException e) {
        String exceptionUniqueId = UUID.randomUUID().toString();
        log.error(exceptionUniqueId + ", " + e.getMessage(), e);
        return new ResponseEntity<>(
                new ErrorMessage(
                        exceptionUniqueId,
                        BAD_ARGUMENTS_IN_SEARCH_PATH.getCodeId(),
                        e.getMessage() + ". " + e.getClass().getName()
                ),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PoorInfoInRequestToCreateUpdateEntity.class)
    public ResponseEntity<ErrorMessage> handlePoorInfoInRequestToCreateUpdateEntity(PoorInfoInRequestToCreateUpdateEntity e) {
        String exceptionUniqueId = UUID.randomUUID().toString();
        log.error(exceptionUniqueId + ", " + e.getMessage(), e);
        return new ResponseEntity<>(
                new ErrorMessage(
                        exceptionUniqueId,
                        POOR_INFORMATION_TO_CREATE_UPDATE_ENTITY.getCodeId(),
                        e.getMessage() + ". " + e.getClass().getName()
                ),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleEntityNotFoundException(EntityNotFoundException e) {
        String exceptionUniqueId = UUID.randomUUID().toString();
        log.error(exceptionUniqueId + ", " + e.getMessage(), e);
        return new ResponseEntity<>(
                new ErrorMessage(
                        exceptionUniqueId,
                        ENTITY_NOT_FOUND.getCodeId(),
                        e.getMessage() + ". " + e.getClass().getName()
                ),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorMessage> handleNoSuchElementException(NoSuchElementException e) {
        String exceptionUniqueId = UUID.randomUUID().toString();
        log.error(exceptionUniqueId + ", " + e.getMessage(), e);
        return new ResponseEntity<>(
                new ErrorMessage(
                        exceptionUniqueId,
                        NO_ENTITY_WITH_SUCH_ID.getCodeId(),
                        e.getMessage() + ". " + e.getClass().getName()
                ),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotCreatedOrNotUpdatedException.class)
    public ResponseEntity<ErrorMessage> handleEntityNotCreatedOrNotUpdatedException(EntityNotCreatedOrNotUpdatedException e) {
        String exceptionUniqueId = UUID.randomUUID().toString();
        log.error(exceptionUniqueId + ", " + e.getMessage(), e);
        return new ResponseEntity<>(
                new ErrorMessage(
                        exceptionUniqueId,
                        ENTITY_NOT_CREATED_OR_NOT_UPDATED.getCodeId(),
                        e.getMessage() + ". " + e.getClass().getName()
                ),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(EntityNotDeletedException.class)
    public ResponseEntity<ErrorMessage> handleEntityNotDeletedException(EntityNotDeletedException e) {
        String exceptionUniqueId = UUID.randomUUID().toString();
        log.error(exceptionUniqueId + ", " + e.getMessage(), e);
        return new ResponseEntity<>(
                new ErrorMessage(
                        exceptionUniqueId,
                        ENTITY_NOT_DELETED.getCodeId(),
                        e.getMessage() + ". " + e.getClass().getName()
                ),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<ErrorMessage> handleNumberFormatException(NumberFormatException e) {
        String exceptionUniqueId = UUID.randomUUID().toString();
        log.error(exceptionUniqueId + ", " + e.getMessage(), e);
        return new ResponseEntity<>(
                new ErrorMessage(
                        exceptionUniqueId,
                        ID_IS_NOT_REQUIRED_NUMBER_FORMAT.getCodeId(),
                        e.getMessage() + ". " + e.getClass().getName()
                ),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalRequestException.class)
    public ResponseEntity<ErrorMessage> handleIllegalRequestException(IllegalRequestException e) {
        String exceptionUniqueId = UUID.randomUUID().toString();
        log.error(exceptionUniqueId + ", " + e.getMessage(), e);
        BindingResult bindingResult = e.getBindingResult();
        String collect = bindingResult
                .getAllErrors()
                .stream()
                .map(ObjectError::toString)
                .collect(Collectors.joining(", "));
        log.error(exceptionUniqueId + e.getMessage(), e);
        return new ResponseEntity<>(
                new ErrorMessage(
                        exceptionUniqueId,
                        BAD_REQUEST_SEE_DETAILS.getCodeId(),
                        collect
                ),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConvertRequestToEntityException.class)
    public ResponseEntity<ErrorMessage> handleConvertRequestToEntityException(ConvertRequestToEntityException e) {
        String exceptionUniqueId = UUID.randomUUID().toString();
        log.error(exceptionUniqueId + ", " + e.getMessage(), e);
        return new ResponseEntity<>(
                new ErrorMessage(
                        exceptionUniqueId,
                        CAN_NOT_CONVERT_REQUEST_TO_ENTITY.getCodeId(),
                        e.getMessage() + ". " + e.getClass().getName()
                ),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorMessage> handleNullPointerException(NullPointerException e) {
        String exceptionUniqueId = UUID.randomUUID().toString();
        log.error(exceptionUniqueId + ", " + e.getMessage(), e);
        return new ResponseEntity<>(
                new ErrorMessage(
                        exceptionUniqueId,
                        NULL_POINTER.getCodeId(),
                        e.getMessage() + ". " + e.getClass().getName()
                ),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorMessage> handleRuntimeException(RuntimeException e) {
        /* Handles all other unchecked exceptions. Status code 500. */
        String exceptionUniqueId = UUID.randomUUID().toString();
        log.error(exceptionUniqueId + ", " + e.getMessage(), e);
        return new ResponseEntity<>(
                new ErrorMessage(
                        exceptionUniqueId,
                        RUNTIME_EXCEPTION.getCodeId(),
                        e.getMessage() + ". " + e.getClass().getName()
                ),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> handleOthersException(Exception e) {
        /* Handles all other checked exceptions. Status code 500. */
        String exceptionUniqueId = UUID.randomUUID().toString();
        log.error(exceptionUniqueId + ", " + e.getMessage(), e);
        return new ResponseEntity<>(
                new ErrorMessage(
                        exceptionUniqueId,
                        FATAL_ERROR.getCodeId(),
                        e.getMessage() + ". " + e.getClass().getName()
                ),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
