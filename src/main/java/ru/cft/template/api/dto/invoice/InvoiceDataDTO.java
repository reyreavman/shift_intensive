package ru.cft.template.api.dto.invoice;

import lombok.Builder;
import ru.cft.template.api.dto.invoice.common.InvoiceStatus;
import ru.cft.template.core.entity.transfer.Transfer;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record InvoiceDataDTO(
        UUID invoiceId,
        Long senderId,
        Long recipientId,
        Long amount,
        String comment,
        InvoiceStatus status,
        Transfer transfer,
        LocalDateTime createdDateTime
) {
}
