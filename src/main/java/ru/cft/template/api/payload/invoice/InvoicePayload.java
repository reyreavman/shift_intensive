package ru.cft.template.api.payload.invoice;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record InvoicePayload(
        @NotNull(message = "{wallet.invoices.create.errors.recipientId_is_null}")
        Long recipientId,
        @NotNull(message = "{wallet.invoices.create.errors.amount_is_null}")
        Long amount,
        @Size(max = 250, message = "{wallet.invoices.create.errors.comment_size_is_invalid}")
        String comment
) {
}
