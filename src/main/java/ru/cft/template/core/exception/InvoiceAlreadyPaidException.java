package ru.cft.template.core.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class InvoiceAlreadyPaidException extends RuntimeException {
    private final UUID invoiceID;
}
