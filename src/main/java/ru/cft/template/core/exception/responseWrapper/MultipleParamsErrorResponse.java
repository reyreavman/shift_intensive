package ru.cft.template.core.exception.responseWrapper;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import ru.cft.template.core.exception.common.Param;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class MultipleParamsErrorResponse {
    private final HttpStatus status;
    private final String message;
    private final List<Param> multipleParamsList;
    private final LocalDateTime dateTime = LocalDateTime.now();
}
