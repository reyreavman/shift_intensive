package ru.cft.template.core.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@Getter
@RequiredArgsConstructor
public class EntityNotFoundException extends RuntimeException {
    private final String entityClassName;
    private final Map<String, String> selectedParamsValues;
}
