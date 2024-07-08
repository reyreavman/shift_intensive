package ru.cft.template.core.converter.transfer;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.cft.template.api.dto.transfer.TransferByEmailDTO;
import ru.cft.template.core.entity.transfer.Transfer;

@Component
public class TransferEntityToEmailDTOConverter implements Converter<Transfer, TransferByEmailDTO> {
    @Override
    public TransferByEmailDTO convert(Transfer transfer) {
        return TransferByEmailDTO.builder()
                .id(transfer.getId())
                .senderId(transfer.getId())
                .recipientEmail(transfer.getRecipientEmail())
                .amount(transfer.getAmount())
                .dateTime(transfer.getDateTime())
                .status(transfer.getStatus())
                .build();
    }
}
