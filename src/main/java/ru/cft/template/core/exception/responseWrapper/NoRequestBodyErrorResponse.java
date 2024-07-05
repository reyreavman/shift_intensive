package ru.cft.template.core.exception.responseWrapper;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class NoRequestBodyErrorResponse {
    private final HttpStatus status;
    private final String message;
    private final LocalDateTime dateTime = LocalDateTime.now();
}
