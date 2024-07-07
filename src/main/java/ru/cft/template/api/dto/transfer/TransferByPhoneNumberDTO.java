package ru.cft.template.api.dto.transfer;

import ru.cft.template.core.entity.transfer.TransferStatus;

import java.time.LocalDateTime;

public record TransferByPhoneNumberDTO(
        Long id,
        Long senderId,
        String recipientPhoneNumber,
        Integer amount,
        LocalDateTime dateTime,
        TransferStatus status
) {
}
