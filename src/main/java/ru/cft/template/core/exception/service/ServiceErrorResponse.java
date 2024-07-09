package ru.cft.template.core.exception.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class ServiceErrorResponse {
    private final HttpStatus status;
    private final String message;
    private final LocalDateTime creationDateTime = LocalDateTime.now();
}
