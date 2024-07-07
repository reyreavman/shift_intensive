package ru.cft.template.core.converter.transfer;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.cft.template.api.dto.transfer.TransferByEmailDTO;
import ru.cft.template.core.entity.transfer.TransferAmongUsers;

@Component
public class TransferAmongUsersEntityToEmailDTOConverter implements Converter<TransferAmongUsers, TransferByEmailDTO> {
    @Override
    public TransferByEmailDTO convert(TransferAmongUsers transfer) {
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
