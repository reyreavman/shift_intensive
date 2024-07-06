package ru.cft.template.core.exception.handler;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.cft.template.core.exception.EntityNotFoundException;
import ru.cft.template.core.exception.common.Violation;
import ru.cft.template.core.exception.responseWrapper.EntityNotFoundErrorResponse;
import ru.cft.template.core.exception.responseWrapper.ValidationErrorResponse;

import java.time.format.DateTimeParseException;
import java.util.List;

@RestControllerAdvice
public class ValidationExceptionHandler {
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ValidationErrorResponse> handleConstraintViolationException(ConstraintViolationException e) {
        final List<Violation> violationList = e.getConstraintViolations().stream()
                .map(violation -> new Violation(violation.getPropertyPath().toString(), violation.getMessage())).toList();
        return ResponseEntity.badRequest().body(new ValidationErrorResponse(HttpStatus.BAD_REQUEST, violationList));
    }

    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<ValidationErrorResponse> handleDateTimeParseException(DateTimeParseException e) {
        return ResponseEntity.badRequest().body(new ValidationErrorResponse(HttpStatus.BAD_REQUEST, new Violation("birthdate", e.getLocalizedMessage())));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<EntityNotFoundErrorResponse> handleNoSuchElementException(EntityNotFoundException e) {
        return ResponseEntity.badRequest().body(new EntityNotFoundErrorResponse(HttpStatus.BAD_REQUEST, "По данным параметрам ничего не найдено.", e.getEntityClassName(), e.getSelectedParamsValues()));
    }
}
