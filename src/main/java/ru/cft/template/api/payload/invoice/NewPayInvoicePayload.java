package ru.cft.template.api.payload.invoice;

import java.util.UUID;

public record NewPayInvoicePayload(
        UUID invoiceId,
        Long amount
) {
}
