package dev.juda.departments_service.department.presentation.advice;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import dev.juda.departments_service.department.presentation.exception.DepartmentAlreadyExistsException;
import dev.juda.departments_service.department.presentation.exception.DepartmentNotFoundException;
import dev.juda.departments_service.shared.dto.ErrorResponse;

import static dev.juda.departments_service.department.util.enums.DepartmentErrorCatalog.*;

@RestControllerAdvice
public class DepartmentExceptionHandler {

    @ResponseStatus(code = HttpStatus.CONFLICT)
    @ExceptionHandler(DepartmentAlreadyExistsException.class)
    public ErrorResponse handleDepartmentAlreadyExistsException(
            DepartmentAlreadyExistsException ex) {
        return new ErrorResponse(
                DEPARTMENT_ALREADY_EXISTS.getCode(),
                HttpStatus.CONFLICT,
                DEPARTMENT_ALREADY_EXISTS.getMessage(),
                null,
                LocalDateTime.now());
    }

    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    @ExceptionHandler(DepartmentNotFoundException.class)
    public ErrorResponse handleDepartmentNotFoundException(DepartmentNotFoundException ex) {
        return new ErrorResponse(
                DEPARTMENT_NOT_FOUND.getCode(),
                HttpStatus.NOT_FOUND,
                DEPARTMENT_NOT_FOUND.getMessage(),
                null,
                LocalDateTime.now());
    }
}
