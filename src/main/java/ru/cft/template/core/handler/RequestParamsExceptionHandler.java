package ru.cft.template.core.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.cft.template.core.exception.MultipleParamsException;
import ru.cft.template.core.exception.common.Param;
import ru.cft.template.core.exception.responseWrapper.MultipleParamsErrorResponse;

import java.util.List;

@RestControllerAdvice
public class RequestParamsExceptionHandler {
    @ExceptionHandler(MultipleParamsException.class)
    public ResponseEntity<MultipleParamsErrorResponse> handleMultipleParamsException(MultipleParamsException e) {
        final List<Param> paramList = e.getParamsList().stream()
                .map(Param::new).toList();
        return ResponseEntity.badRequest().body(new MultipleParamsErrorResponse("Передано избыточное количество параметров. Передайте только один из параметров.", paramList));
    }
}
