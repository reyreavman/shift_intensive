package ru.cft.template.core.exception.handler;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.cft.template.core.exception.EntityNotFoundException;
import ru.cft.template.core.exception.InvalidInvoiceAmountException;
import ru.cft.template.core.exception.InvalidInvoicePayerException;
import ru.cft.template.core.exception.InvoiceAlreadyPaidException;
import ru.cft.template.core.exception.NotEnoughMoneyException;
import ru.cft.template.core.exception.NotInvoiceOwnerException;
import ru.cft.template.core.exception.SelfTransferException;
import ru.cft.template.core.exception.common.Violation;
import ru.cft.template.core.exception.responseWrapper.EntityNotFoundErrorResponse;
import ru.cft.template.core.exception.responseWrapper.InvalidInvoiceAmountErrorResponse;
import ru.cft.template.core.exception.responseWrapper.InvalidInvoicePayerErrorResponse;
import ru.cft.template.core.exception.responseWrapper.InvoiceAlreadyPaidErrorResponse;
import ru.cft.template.core.exception.responseWrapper.NotEnoughMoneyErrorResponse;
import ru.cft.template.core.exception.responseWrapper.NotInvoiceOwnerErrorResponse;
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
        return ResponseEntity.badRequest().body(new EntityNotFoundErrorResponse(HttpStatus.BAD_REQUEST, "Объект не найден", e.getEntityClassName(), e.getSelectedParamsValues()));
    }

    @ExceptionHandler(SelfTransferException.class)
    public ResponseEntity<SelfTransferErrorResponse> handleSelfTransferException(SelfTransferException e) {
        return ResponseEntity.badRequest().body(new SelfTransferErrorResponse(HttpStatus.BAD_REQUEST, "Попытка перевода денег на свой счет.", e.getSenderId(), e.getRecipientId()));
    }

    @ExceptionHandler(NotEnoughMoneyException.class)
    public ResponseEntity<NotEnoughMoneyErrorResponse> handleNotEnoughMoneyException(NotEnoughMoneyException e) {
        return ResponseEntity.badRequest().body(new NotEnoughMoneyErrorResponse(HttpStatus.BAD_REQUEST, "Ошибка перевода - на счету недостаточно средств.", e.getUserId(), e.getBalance(), e.getTransferAmount()));
    }

    @ExceptionHandler(InvalidInvoicePayerException.class)
    public ResponseEntity<InvalidInvoicePayerErrorResponse> handleInvalidInvoicePayerException(InvalidInvoicePayerException e) {
        return ResponseEntity.badRequest().body(new InvalidInvoicePayerErrorResponse(HttpStatus.BAD_REQUEST, "Пользователь не может оплатить данный счет.", e.getUserId(), e.getInvoiceId()));
    }

    @ExceptionHandler(InvoiceAlreadyPaidException.class)
    public ResponseEntity<InvoiceAlreadyPaidErrorResponse> handleInvoiceAlreadyPaidException(InvoiceAlreadyPaidException e) {
        return ResponseEntity.badRequest().body(new InvoiceAlreadyPaidErrorResponse(HttpStatus.BAD_REQUEST, "Счет уже оплачен", e.getInvoiceID()));
    }

    @ExceptionHandler(NotInvoiceOwnerException.class)
    public ResponseEntity<NotInvoiceOwnerErrorResponse> handleNotInvoiceOwnerException(NotInvoiceOwnerException e) {
        return ResponseEntity.badRequest().body(new NotInvoiceOwnerErrorResponse(HttpStatus.BAD_REQUEST, "Отмена чужого счета.", e.getUserId(), e.getInvoiceID()));
    }

    @ExceptionHandler(InvalidInvoiceAmountException.class)
    public ResponseEntity<InvalidInvoiceAmountErrorResponse> handleInvalidInvoiceAmountException(InvalidInvoiceAmountException e) {
        return ResponseEntity.badRequest().body(new InvalidInvoiceAmountErrorResponse(HttpStatus.BAD_REQUEST, "Невозможно оплатить часть счета.", e.getAmountInInvoice(), e.getAmountInPayload()));
    }
}
