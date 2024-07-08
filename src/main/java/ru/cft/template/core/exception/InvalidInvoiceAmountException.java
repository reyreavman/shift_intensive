package ru.cft.template.core.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class InvalidInvoiceAmountException extends RuntimeException {
    private final Long amountInInvoice;
    private final Long amountInPayload;
}
