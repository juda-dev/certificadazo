package dev.juda.departments_service.position.presentation.advice;

import dev.juda.departments_service.position.service.exception.PositionAlreadyExistsException;
import dev.juda.departments_service.position.service.exception.PositionNotFoundException;
import dev.juda.departments_service.shared.dto.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

import static dev.juda.departments_service.position.util.enums.PositionErrorCatalog.POSITION_ALREADY_EXISTS;
import static dev.juda.departments_service.position.util.enums.PositionErrorCatalog.POSITION_NOT_FOUND;

@RestControllerAdvice
public class PositionExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(PositionExceptionHandler.class);
    
    @ResponseStatus(code = HttpStatus.CONFLICT)
    @ExceptionHandler(PositionAlreadyExistsException.class)
    public ErrorResponse handlePositionAlreadyExistsException(
            PositionAlreadyExistsException ex) {
        LOG.warn("A position with this name already exists.");
        return new ErrorResponse(
                POSITION_ALREADY_EXISTS.getCode(),
                HttpStatus.CONFLICT,
                POSITION_ALREADY_EXISTS.getMessage(),
                null,
                LocalDateTime.now());
    }

    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    @ExceptionHandler(PositionNotFoundException.class)
    public ErrorResponse handlePositionNotFoundException(PositionNotFoundException ex) {
        LOG.warn("The position could not be found");
        return new ErrorResponse(
                POSITION_NOT_FOUND.getCode(),
                HttpStatus.NOT_FOUND,
                POSITION_NOT_FOUND.getMessage(),
                null,
                LocalDateTime.now());
    }
}
