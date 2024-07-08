package ru.cft.template.api.payload.invoice;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record PayInvoicePayload(
        @NotNull(message = "{wallet.invoices.pay.errors.invoiceId_is_null}")
        UUID invoiceId,
        @NotNull(message = "{wallet.invoices.pay.errors.amount_is_null}")
        Long amount
) {
}
