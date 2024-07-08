package ru.cft.template.core.exception.responseWrapper;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class InvoiceAlreadyPaidErrorResponse {
    private final HttpStatus status;
    private final String message;
    private final UUID invoiceId;
    private final LocalDateTime dateTime = LocalDateTime.now();
}