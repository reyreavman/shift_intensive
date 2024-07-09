package ru.cft.template.core.exception.validation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class ValidationErrorResponse {
    private final List<Violation> violations;
    private final LocalDateTime creationDateTime = LocalDateTime.now();
}
