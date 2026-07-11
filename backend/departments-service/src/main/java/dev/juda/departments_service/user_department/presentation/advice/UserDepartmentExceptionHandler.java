package dev.juda.departments_service.user_department.presentation.advice;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import dev.juda.departments_service.shared.dto.ErrorResponse;
import dev.juda.departments_service.user_department.service.exception.NonExistsUserException;
import dev.juda.departments_service.user_department.service.exception.UserDepartmentAlreadyExistsException;
import dev.juda.departments_service.user_department.service.exception.UserDepartmentNotFoundException;

import static dev.juda.departments_service.user_department.util.enums.UserDepartmentErrorCatalog.*;

@RestControllerAdvice
public class UserDepartmentExceptionHandler {

    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    @ExceptionHandler(NonExistsUserException.class)
    public ErrorResponse handleNonExistsUserException(
            NonExistsUserException ex) {
        return new ErrorResponse(
                NON_EXISTS_USER.getCode(),
                HttpStatus.NOT_FOUND,
                NON_EXISTS_USER.getMessage(),
                null,
                LocalDateTime.now());
    }

    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserDepartmentNotFoundException.class)
    public ErrorResponse handleUserDepartmentNotFoundException(UserDepartmentNotFoundException ex) {
        return new ErrorResponse(
                USER_DEPARTMENT_NOT_FOUND.getCode(),
                HttpStatus.NOT_FOUND,
                USER_DEPARTMENT_NOT_FOUND.getMessage(),
                null,
                LocalDateTime.now());
    }

    @ResponseStatus(code = HttpStatus.CONFLICT)
    @ExceptionHandler(UserDepartmentAlreadyExistsException.class)
    public ErrorResponse handleUserDepartmentAlreadyExistsException(UserDepartmentAlreadyExistsException ex) {
        return new ErrorResponse(
                USER_DEPARTMENT_ALREADY_EXISTS.getCode(),
                HttpStatus.CONFLICT,
                USER_DEPARTMENT_ALREADY_EXISTS.getMessage(),
                null,
                LocalDateTime.now());
    }
}
