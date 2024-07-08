package ru.cft.template.core.converter.transfer;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.cft.template.api.dto.transfer.TransferByPhoneNumberDTO;
import ru.cft.template.core.entity.transfer.Transfer;

@Component
public class TransferEntityToPhoneNumberDTOConverter implements Converter<Transfer, TransferByPhoneNumberDTO> {
    @Override
    public TransferByPhoneNumberDTO convert(Transfer transfer) {
        return TransferByPhoneNumberDTO.builder()
                .id(transfer.getId())
                .senderId(transfer.getId())
                .recipientPhoneNumber(transfer.getRecipientPhoneNumber())
                .amount(transfer.getAmount())
                .dateTime(transfer.getDateTime())
                .status(transfer.getStatus())
                .build();
    }
}
