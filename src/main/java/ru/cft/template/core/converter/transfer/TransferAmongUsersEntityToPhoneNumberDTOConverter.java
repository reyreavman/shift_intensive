package ru.cft.template.core.converter.transfer;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.cft.template.api.dto.transfer.TransferByPhoneNumberDTO;
import ru.cft.template.core.entity.transfer.TransferAmongUsers;

@Component
public class TransferAmongUsersEntityToPhoneNumberDTOConverter implements Converter<TransferAmongUsers, TransferByPhoneNumberDTO> {
    @Override
    public TransferByPhoneNumberDTO convert(TransferAmongUsers transfer) {
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
