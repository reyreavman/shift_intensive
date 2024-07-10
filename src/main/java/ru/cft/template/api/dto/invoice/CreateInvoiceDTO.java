package ru.cft.template.api.dto.invoice;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record CreateInvoiceDTO(
        @NotNull(message = "{wallet.invoices.create.errors.recipientId_is_null}")
        Long recipientId,
        @NotNull(message = "{wallet.invoices.create.errors.amount_is_null}")
        @Positive(message = "{wallet.invoices.create.errors.amount_is_negative}")
        Long amount,
        @Size(max = 250, message = "{wallet.invoices.create.errors.comment_size_is_invalid}")
        String comment
) {
}
