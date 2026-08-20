package dev.juda.departments_service.department.presentation.advice;

import dev.juda.departments_service.department.service.exception.DepartmentAlreadyExistsException;
import dev.juda.departments_service.department.service.exception.DepartmentNotFoundException;
import dev.juda.departments_service.shared.dto.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

import static dev.juda.departments_service.department.util.enums.DepartmentErrorCatalog.DEPARTMENT_ALREADY_EXISTS;
import static dev.juda.departments_service.department.util.enums.DepartmentErrorCatalog.DEPARTMENT_NOT_FOUND;

@RestControllerAdvice
public class DepartmentExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(DepartmentExceptionHandler.class);

    @ResponseStatus(code = HttpStatus.CONFLICT)
    @ExceptionHandler(DepartmentAlreadyExistsException.class)
    public ErrorResponse handleDepartmentAlreadyExistsException(
            DepartmentAlreadyExistsException ex) {
        LOG.warn("There is already a department with this name.");
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
        LOG.warn("No department found");
        return new ErrorResponse(
                DEPARTMENT_NOT_FOUND.getCode(),
                HttpStatus.NOT_FOUND,
                DEPARTMENT_NOT_FOUND.getMessage(),
                null,
                LocalDateTime.now());
    }
}
