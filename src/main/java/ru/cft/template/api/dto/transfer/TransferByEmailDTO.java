package ru.cft.template.api.dto.transfer;

import ru.cft.template.core.entity.transfer.TransferStatus;

import java.time.LocalDateTime;

public record TransferByEmailDTO(
        Long id,
        Long senderId,
        String recipientEmail,
        Integer amount,
        LocalDateTime dateTime,
        TransferStatus status
) {
}
