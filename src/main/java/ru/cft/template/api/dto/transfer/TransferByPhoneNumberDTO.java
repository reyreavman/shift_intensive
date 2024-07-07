package ru.cft.template.api.dto.transfer;

import lombok.Builder;
import ru.cft.template.core.entity.transfer.TransferStatus;

import java.time.LocalDateTime;

@Builder
public record TransferByPhoneNumberDTO(
        Long id,
        Long senderId,
        String recipientPhoneNumber,
        Long amount,
        LocalDateTime dateTime,
        TransferStatus status
) {
}
