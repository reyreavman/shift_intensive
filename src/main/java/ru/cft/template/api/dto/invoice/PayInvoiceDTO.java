package ru.cft.template.api.dto.invoice;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record PayInvoiceDTO(
        @NotNull(message = "{wallet.invoices.pay.errors.invoiceId_is_null}")
        UUID invoiceId,
        @NotNull(message = "{wallet.invoices.pay.errors.amount_is_null}")
        Long amount
) {
}
