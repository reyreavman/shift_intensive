package ru.cft.template.core.converter.transfer;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.cft.template.api.dto.transfer.TransferAmongUsersDataDTO;
import ru.cft.template.core.entity.transfer.TransferAmongUsers;
import ru.cft.template.core.entity.transfer.TransferInitiationType;

@Component
public class TransferAmongUsersEntityToDataDTOConverter implements Converter<TransferAmongUsers, TransferAmongUsersDataDTO> {
    @Override
    public TransferAmongUsersDataDTO convert(TransferAmongUsers transfer) {
        return TransferAmongUsersDataDTO.builder()
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
