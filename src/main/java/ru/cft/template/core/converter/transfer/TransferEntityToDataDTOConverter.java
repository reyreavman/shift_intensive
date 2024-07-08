package ru.cft.template.core.converter.transfer;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.cft.template.api.dto.transfer.TransferDataDTO;
import ru.cft.template.core.entity.transfer.Transfer;
import ru.cft.template.core.entity.transfer.TransferInitiationType;

@Component
public class TransferEntityToDataDTOConverter implements Converter<Transfer, TransferDataDTO> {
    @Override
    public TransferDataDTO convert(Transfer transfer) {
        return TransferDataDTO.builder()
                .id(transfer.getId())
                .senderId(transfer.getSenderWallet().getId())
                .recipientId(transfer.getRecipientWallet().getId())
                .amount(transfer.getAmount())
                .type(TransferInitiationType.USER_INITIATED)
                .status(transfer.getStatus())
                .dateTime(transfer.getDateTime())
                .build();
    }
}
