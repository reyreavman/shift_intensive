package ru.cft.template.core.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class NotInvoiceOwnerException extends RuntimeException {
    private final Long userId;
    private final UUID invoiceID;
}
