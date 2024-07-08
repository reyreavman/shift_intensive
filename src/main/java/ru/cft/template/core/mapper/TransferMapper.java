package ru.cft.template.core.mapper;

import org.springframework.stereotype.Component;
import ru.cft.template.api.dto.transfer.TransferByEmailDTO;
import ru.cft.template.api.dto.transfer.TransferByPhoneNumberDTO;
import ru.cft.template.api.dto.transfer.TransferDataDTO;
import ru.cft.template.core.entity.Wallet;
import ru.cft.template.core.entity.transfer.Transfer;
import ru.cft.template.core.entity.transfer.TransferStatus;

@Component
public class TransferMapper {
    public Transfer mapToTransfer(Long amount, Wallet senderWallet, Wallet recipientWallet, TransferStatus status) {
        return Transfer.builder()
                .id(null)
                .senderWallet(senderWallet)
                .recipientWallet(recipientWallet)
                .amount(amount)
                .status(status)
                .build();
    }

    public TransferDataDTO mapToTransferData(Transfer transfer) {
        return TransferDataDTO.builder()
                .id(transfer.getId())
                .senderId(transfer.getSenderWallet().getId())
                .recipientId(transfer.getRecipientWallet().getId())
                .amount(transfer.getAmount())
                .status(transfer.getStatus())
                .dateTime(transfer.getDateTime())
                .build();
    }

    public TransferByEmailDTO mapToTransferByEmail(Transfer transfer) {
        return TransferByEmailDTO.builder()
                .id(transfer.getId())
                .senderId(transfer.getSenderWallet().getId())
                .recipientEmail(transfer.getRecipientWallet().getUser().getEmail())
                .amount(transfer.getAmount())
                .dateTime(transfer.getDateTime())
                .status(transfer.getStatus())
                .build();
    }

    public TransferByPhoneNumberDTO mapToTransferByPhoneNumber(Transfer transfer) {
        return TransferByPhoneNumberDTO.builder()
                .id(transfer.getId())
                .senderId(transfer.getSenderWallet().getId())
                .recipientPhoneNumber(transfer.getRecipientWallet().getUser().getPhoneNumber())
                .amount(transfer.getAmount())
                .dateTime(transfer.getDateTime())
                .status(transfer.getStatus())
                .build();
    }
}
