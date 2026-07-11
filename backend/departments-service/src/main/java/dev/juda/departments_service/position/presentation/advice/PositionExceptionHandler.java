package dev.juda.departments_service.position.presentation.advice;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import dev.juda.departments_service.department.service.exception.DepartmentAlreadyExistsException;
import dev.juda.departments_service.department.service.exception.DepartmentNotFoundException;
import dev.juda.departments_service.position.service.exception.PositionAlreadyExistsException;
import dev.juda.departments_service.position.service.exception.PositionNotFoundException;
import dev.juda.departments_service.shared.dto.ErrorResponse;

import static dev.juda.departments_service.position.util.enums.PositionErrorCatalog.*;

@RestControllerAdvice
public class PositionExceptionHandler {
    
    @ResponseStatus(code = HttpStatus.CONFLICT)
    @ExceptionHandler(PositionAlreadyExistsException.class)
    public ErrorResponse handlePositionAlreadyExistsException(
            PositionAlreadyExistsException ex) {
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
        return new ErrorResponse(
                POSITION_NOT_FOUND.getCode(),
                HttpStatus.NOT_FOUND,
                POSITION_NOT_FOUND.getMessage(),
                null,
                LocalDateTime.now());
    }
}
