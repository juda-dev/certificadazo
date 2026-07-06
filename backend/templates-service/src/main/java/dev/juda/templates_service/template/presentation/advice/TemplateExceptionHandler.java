package dev.juda.templates_service.template.presentation.advice;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import dev.juda.templates_service.template.presentation.dto.response.ErrorResponse;
import dev.juda.templates_service.template.presentation.exception.DepartmentNotFoundException;
import dev.juda.templates_service.template.presentation.exception.TemplateNotFoundException;

import static dev.juda.templates_service.template.util.enums.TemplateErrorCatalog.*;

@RestControllerAdvice
public class TemplateExceptionHandler {

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

    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    @ExceptionHandler(TemplateNotFoundException.class)
    public ErrorResponse handleUserDepartmentNotFoundException(TemplateNotFoundException ex) {
        return new ErrorResponse(
                TEMPLATE_NOT_FOUND.getCode(),
                HttpStatus.NOT_FOUND,
                TEMPLATE_NOT_FOUND.getMessage(),
                null,
                LocalDateTime.now());
    }
}
