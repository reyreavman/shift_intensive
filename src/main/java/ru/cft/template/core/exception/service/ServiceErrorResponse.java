package ru.cft.template.core.exception.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class ServiceErrorResponse {
    private final String message;
    private final LocalDateTime creationDateTime = LocalDateTime.now();
}
