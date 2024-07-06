package ru.cft.template.core.exception.responseWrapper;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class MissingRequestHeaderErrorResponse {
    private final HttpStatus status;
    private final String header;
    private final String message;
    private final LocalDateTime localDateTime = LocalDateTime.now();
}
