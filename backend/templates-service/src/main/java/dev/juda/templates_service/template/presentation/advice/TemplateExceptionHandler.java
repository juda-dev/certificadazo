package dev.juda.templates_service.template.presentation.advice;

import dev.juda.templates_service.template.presentation.dto.response.ErrorResponse;
import dev.juda.templates_service.template.service.exception.DepartmentNotFoundException;
import dev.juda.templates_service.template.service.exception.TemplateNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

import static dev.juda.templates_service.template.util.enums.TemplateErrorCatalog.DEPARTMENT_NOT_FOUND;
import static dev.juda.templates_service.template.util.enums.TemplateErrorCatalog.TEMPLATE_NOT_FOUND;

@RestControllerAdvice
public class TemplateExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(TemplateExceptionHandler.class);

    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    @ExceptionHandler(DepartmentNotFoundException.class)
    public ErrorResponse handleDepartmentNotFoundException(DepartmentNotFoundException ex) {
        LOG.warn("Department not found");
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
        LOG.warn("User department not found");
        return new ErrorResponse(
                TEMPLATE_NOT_FOUND.getCode(),
                HttpStatus.NOT_FOUND,
                TEMPLATE_NOT_FOUND.getMessage(),
                null,
                LocalDateTime.now());
    }
}
