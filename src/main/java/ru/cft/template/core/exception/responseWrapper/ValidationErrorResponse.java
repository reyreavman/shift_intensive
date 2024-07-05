package ru.cft.template.core.exception.responseWrapper;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import ru.cft.template.core.exception.common.Violation;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class ValidationErrorResponse {
    private final HttpStatus status;
    private final List<Violation> violationList;
    private final LocalDateTime dateTime = LocalDateTime.now();

    public ValidationErrorResponse(HttpStatus status, Violation... violation) {
        this.status = status;
        this.violationList = List.of(violation);
    }
}
