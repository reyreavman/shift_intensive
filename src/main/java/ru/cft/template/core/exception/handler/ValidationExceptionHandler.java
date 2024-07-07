package ru.cft.template.core.exception.handler;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.cft.template.core.exception.EntityNotFoundException;
import ru.cft.template.core.exception.NotEnoughMoneyException;
import ru.cft.template.core.exception.SelfTransferException;
import ru.cft.template.core.exception.common.Violation;
import ru.cft.template.core.exception.responseWrapper.EntityNotFoundErrorResponse;
import ru.cft.template.core.exception.responseWrapper.NotEnoughMoneyErrorResponse;
import ru.cft.template.core.exception.responseWrapper.SelfTransferErrorResponse;
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
    public ResponseEntity<EntityNotFoundErrorResponse> handleEntityNotFoundException(EntityNotFoundException e) {
        return ResponseEntity.badRequest().body(new EntityNotFoundErrorResponse(HttpStatus.BAD_REQUEST, "Ничего не найдено для данных параметров", e.getEntityClassName(), e.getSelectedParamsValues()));
    }

    @ExceptionHandler(SelfTransferException.class)
    public ResponseEntity<SelfTransferErrorResponse> handleSelfTransferException(SelfTransferException e) {
        return ResponseEntity.badRequest().body(new SelfTransferErrorResponse(HttpStatus.BAD_REQUEST, "Попытка перевода денег на свой счет.", e.getSenderId(), e.getRecipientId()));
    }

    @ExceptionHandler(NotEnoughMoneyException.class)
    public ResponseEntity<NotEnoughMoneyErrorResponse> handleNotEnoughMoneyException(NotEnoughMoneyException e) {
        return ResponseEntity.badRequest().body(new NotEnoughMoneyErrorResponse(HttpStatus.BAD_REQUEST, "Ошибка перевода - недостаточно средств.", e.getSenderId(), e.getBalance(), e.getTransferAmount()));
    }
}
