package ru.cft.template.api.dto.transfer;

import lombok.Builder;
import ru.cft.template.core.entity.transfer.TransferInitiationType;
import ru.cft.template.core.entity.transfer.TransferStatus;

import java.time.LocalDateTime;

@Builder
public record TransferDataDTO(
        Long id,
        Long senderId,
        Long recipientId,
        Long amount,
        TransferInitiationType type,
        TransferStatus status,
        LocalDateTime dateTime
) {
}
