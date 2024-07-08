package ru.cft.template.core.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.cft.template.core.exception.MultipleParamsException;
import ru.cft.template.core.exception.NoRequestBodyException;
import ru.cft.template.core.exception.common.Param;
import ru.cft.template.core.exception.responseWrapper.MissingRequestHeaderErrorResponse;
import ru.cft.template.core.exception.responseWrapper.MultipleParamsErrorResponse;
import ru.cft.template.core.exception.responseWrapper.NoRequestBodyErrorResponse;

import java.util.List;

@RestControllerAdvice
public class RequestExceptionHandler {
    @ExceptionHandler(MultipleParamsException.class)
    public ResponseEntity<MultipleParamsErrorResponse> handleMultipleParamsException(MultipleParamsException e) {
        final List<Param> paramList = e.getParamsList().stream()
                .map(Param::new).toList();
        return ResponseEntity.badRequest().body(new MultipleParamsErrorResponse(HttpStatus.BAD_REQUEST, "Передано избыточное количество параметров.", paramList));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<NoRequestBodyErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return ResponseEntity.badRequest().body(new NoRequestBodyErrorResponse(HttpStatus.BAD_REQUEST, "Запрос не может быть обработан. Тело запроса не передано."));
    }

    @ExceptionHandler(NoRequestBodyException.class)
    public ResponseEntity<NoRequestBodyErrorResponse> handleNoRequestBodyException(NoRequestBodyException e) {
        return ResponseEntity.badRequest().body(new NoRequestBodyErrorResponse(HttpStatus.BAD_REQUEST, "Запрос не может быть обработан. Тело запроса не передано."));
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<MissingRequestHeaderErrorResponse> handleMissingRequestHeaderException(MissingRequestHeaderException e) {
        return ResponseEntity.badRequest().body(new MissingRequestHeaderErrorResponse(HttpStatus.BAD_REQUEST, e.getHeaderName(), e.getMessage()));
    }
}
