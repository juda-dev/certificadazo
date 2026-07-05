package dev.juda.departments_service.shared.advice;

import java.time.LocalDateTime;
import java.util.Collections;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import dev.juda.departments_service.shared.dto.ErrorResponse;

import static dev.juda.departments_service.shared.enums.ErrorCatalog.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

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
