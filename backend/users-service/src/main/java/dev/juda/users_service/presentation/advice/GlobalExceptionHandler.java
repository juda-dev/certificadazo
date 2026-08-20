package dev.juda.users_service.presentation.advice;

import dev.juda.users_service.presentation.dto.response.ErrorResponse;
import dev.juda.users_service.service.exception.CommandNotSentException;
import dev.juda.users_service.service.exception.ExistingUserException;
import dev.juda.users_service.service.exception.NonExistentUser;
import dev.juda.users_service.service.exception.TimeoutCommandException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Collections;

import static dev.juda.users_service.util.enums.ErrorCatalog.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NonExistentUser.class)
    public ErrorResponse handleNonExistentUser(NonExistentUser ex) {
        LOG.warn("Non existent user");
        return new ErrorResponse(
                NON_EXISTENT_USER.getCode(),
                HttpStatus.NOT_FOUND,
                NON_EXISTENT_USER.getMessage(),
                null,
                LocalDateTime.now()
        );
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ExistingUserException.class)
    public ErrorResponse handleExistingUserException(ExistingUserException ex) {
        LOG.warn("Existing user");
        return new ErrorResponse(
                EXISTING_USER.getCode(),
                HttpStatus.CONFLICT,
                EXISTING_USER.getMessage() + ex.getField(),
                null,
                LocalDateTime.now()
        );
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(CommandNotSentException.class)
    public ErrorResponse handleCommandNotSentException(CommandNotSentException ex) {
        LOG.error("Command Not Sent Exception");
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
    public ErrorResponse handleTimeoutCommandException(TimeoutCommandException ex) {
        LOG.error("Command Timeout");
        return new ErrorResponse(
                TIMEOUT_COMMAND.getCode(),
                HttpStatus.GATEWAY_TIMEOUT,
                TIMEOUT_COMMAND.getMessage() + ex.getMessage(),
                null,
                LocalDateTime.now()
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handlerMethodArgumentNotValidException(MethodArgumentNotValidException ex) {

        BindingResult result = ex.getBindingResult();

        LOG.warn("Invalid arguments in the form: {}", result);

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
        LOG.error("Error processing request", ex);

        return new ErrorResponse(
                GENERIC_ERROR.getCode(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                GENERIC_ERROR.getMessage(),
                Collections.singletonList(ex.getMessage()),
                LocalDateTime.now());
    }
}
