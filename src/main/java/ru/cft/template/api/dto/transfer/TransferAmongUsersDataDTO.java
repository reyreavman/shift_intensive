package ru.cft.template.api.dto.transfer;

import ru.cft.template.core.entity.transfer.TransferInitiationType;
import ru.cft.template.core.entity.transfer.TransferStatus;

import java.time.LocalDateTime;

public record TransferAmongUsersDataDTO(
        Long id,
        Long senderId,
        Long recipientId,
        Integer amount,
        TransferInitiationType type,
        TransferStatus status,
        LocalDateTime dateTime
) {
}
