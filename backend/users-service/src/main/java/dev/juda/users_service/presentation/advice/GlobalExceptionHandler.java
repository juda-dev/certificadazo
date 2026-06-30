package dev.juda.users_service.presentation.advice;

import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import dev.juda.users_service.presentation.dto.response.ErrorResponse;
import dev.juda.users_service.service.exception.CommandNotSentException;
import dev.juda.users_service.service.exception.TimeoutCommandException;

import static dev.juda.users_service.util.enums.ErrorCatalog.*;

import java.time.LocalDateTime;
import java.util.Collections;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(CommandNotSentException.class)
    public ErrorResponse handleCommandNotSentException(CommandNotSentException ex){
        return new ErrorResponse(
            COMMAND_NOT_SENT.getCode(),
            HttpStatus.INTERNAL_SERVER_ERROR,
            COMMAND_NOT_SENT.getMessage(),
            null,
            LocalDateTime.now()
        );
    }

    @ResponseStatus(HttpStatus.GATEWAY_TIMEOUT)
    @ExceptionHandler(TimeoutCommandException.class)
    public ErrorResponse handleTimeoutCommandException(TimeoutCommandException ex){
        return new ErrorResponse(
            TIMEOUT_COMMAND.getCode(),
            HttpStatus.GATEWAY_TIMEOUT,
            TIMEOUT_COMMAND.getMessage(),
            null,
            LocalDateTime.now()
        );
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
