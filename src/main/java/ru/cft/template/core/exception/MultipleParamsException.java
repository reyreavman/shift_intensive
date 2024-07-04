package ru.cft.template.core.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class MultipleParamsException extends RuntimeException {
    private final List<String> paramsList;

    public MultipleParamsException(String... params) {
        this.paramsList = List.of(params);
    }
}
