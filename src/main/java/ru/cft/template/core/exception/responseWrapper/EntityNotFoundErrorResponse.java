package ru.cft.template.core.exception.responseWrapper;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public class EntityNotFoundErrorResponse {
    private final HttpStatus status;
    private final String message;
    private final String entityName;
    private final Map<String, String> selectedParamsValues;
    private final LocalDateTime dateTime = LocalDateTime.now();
}
