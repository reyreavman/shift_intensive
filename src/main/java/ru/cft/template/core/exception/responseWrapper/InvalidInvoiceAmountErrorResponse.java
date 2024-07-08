package ru.cft.template.core.exception.responseWrapper;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class InvalidInvoiceAmountErrorResponse {
    private final HttpStatus status;
    private final String message;
    private final Long amountInInvoice;
    private final Long amountInPayload;
    private final LocalDateTime dateTime = LocalDateTime.now();
}
