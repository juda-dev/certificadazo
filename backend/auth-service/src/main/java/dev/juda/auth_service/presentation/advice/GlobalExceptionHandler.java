package dev.juda.auth_service.presentation.advice;

import java.time.LocalDateTime;
import java.util.Collections;

import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import dev.juda.auth_service.presentation.dto.response.ErrorResponse;
import dev.juda.auth_service.service.exception.InvalidCredentialsException;
import dev.juda.auth_service.service.exception.RoleNotFoundException;
import dev.juda.auth_service.service.exception.UserNotCreatedException;
import dev.juda.auth_service.service.exception.UserNotUpdatedException;

import static dev.juda.auth_service.util.enums.ErrorCatalog.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(UserNotUpdatedException.class)
    public ErrorResponse hendleUserNotUpdatedException(UserNotUpdatedException ex) {
        return new ErrorResponse(
                USER_NOT_UPDATED.getCode(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                USER_NOT_UPDATED.getMessage(),
                null,
                LocalDateTime.now());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(UserNotCreatedException.class)
    public ErrorResponse hendleUserNotCreatedException(UserNotCreatedException ex) {
        return new ErrorResponse(
                USER_NOT_CREATED.getCode(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                USER_NOT_CREATED.getMessage(),
                null,
                LocalDateTime.now());
    }
    
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(RoleNotFoundException.class)
    public ErrorResponse hendleRoleNotFoundException(RoleNotFoundException ex) {
        return new ErrorResponse(
                ROLE_NOT_FOUND.getCode(),
                HttpStatus.NOT_FOUND,
                ROLE_NOT_FOUND.getMessage(),
                null,
                LocalDateTime.now());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(InvalidCredentialsException.class)
    public ErrorResponse hendleInvalidCredentialsException(InvalidCredentialsException ex) {
        return new ErrorResponse(
                INVALID_CREDENTIALS.getCode(),
                HttpStatus.UNAUTHORIZED,
                INVALID_CREDENTIALS.getMessage(),
                null,
                LocalDateTime.now());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handlerMethodArgumentNotValidException(MethodArgumentNotValidException ex) {

        BindingResult result = ex.getBindingResult();

        return new ErrorResponse(
                BAD_CREDENTIALS.getCode(),
                HttpStatus.BAD_REQUEST,
                BAD_CREDENTIALS.getMessage(),
                result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList(),
                LocalDateTime.now());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorResponse handlerException(Exception ex) {

        return new ErrorResponse(
                GENERIC_ERROR.getCode(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                GENERIC_ERROR.getMessage(),
                Collections.singletonList(ex.getMessage()),
                LocalDateTime.now());
    }
}
