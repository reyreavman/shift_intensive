package ru.cft.template.core.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.cft.template.core.exception.service.ServiceErrorResponse;
import ru.cft.template.core.exception.service.ServiceException;
import ru.cft.template.core.exception.validation.ValidationErrorResponse;
import ru.cft.template.core.exception.validation.Violation;

import java.util.Arrays;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class ExceptionsHandler {
    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ServiceErrorResponse handleServiceException(ServiceException e) {
        return new ServiceErrorResponse(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<Violation> violations = e.getBindingResult().getFieldErrors()
                .stream()
                .map(error -> new Violation(error.getField(), error.getDefaultMessage()))
                .toList();
        return new ValidationErrorResponse(violations);
    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ServiceErrorResponse handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        List<String> keys = Arrays.stream(e.getMostSpecificCause().getMessage().split(":")).toList();
        return new ServiceErrorResponse(keys.get(keys.size() - 1).trim());
    }

}
