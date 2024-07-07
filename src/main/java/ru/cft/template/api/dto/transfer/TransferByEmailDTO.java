package ru.cft.template.api.dto.transfer;

import lombok.Builder;
import ru.cft.template.core.entity.transfer.TransferStatus;

import java.time.LocalDateTime;

@Builder
public record TransferByEmailDTO(
        Long id,
        Long senderId,
        String recipientEmail,
        Long amount,
        LocalDateTime dateTime,
        TransferStatus status
) {
}
