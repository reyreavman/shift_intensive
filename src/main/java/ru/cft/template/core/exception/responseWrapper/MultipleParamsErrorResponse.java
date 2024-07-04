package ru.cft.template.core.exception.responseWrapper;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.cft.template.core.exception.common.Param;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class MultipleParamsErrorResponse {
    private final String message;
    private final List<Param> multipleParamsList;

    public MultipleParamsErrorResponse(String messageParam, Param... param) {
        this.message = messageParam;
        this.multipleParamsList = List.of(param);
    }
}
