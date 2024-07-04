package ru.cft.template.core.exception.responseWrapper;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.cft.template.core.exception.common.Violation;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class ValidationErrorResponse {
    private final List<Violation> violationList;

    public ValidationErrorResponse(Violation... violation) {
        this.violationList = List.of(violation);
    }
}
