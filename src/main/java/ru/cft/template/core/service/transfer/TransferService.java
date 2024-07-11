package ru.cft.template.core.service.transfer;

import ru.cft.template.api.dto.transfer.CreateTransferByEmailDTO;
import ru.cft.template.api.dto.transfer.CreateTransferByPhoneNumberDTO;
import ru.cft.template.api.dto.transfer.TransferByEmailDTO;
import ru.cft.template.api.dto.transfer.TransferByPhoneNumberDTO;
import ru.cft.template.api.dto.transfer.TransferDataDTO;
import ru.cft.template.core.entity.transfer.Transfer;
import ru.cft.template.core.entity.transfer.TransferDirectionType;
import ru.cft.template.core.entity.transfer.TransferStatus;

import java.util.List;

public interface TransferService {
    Transfer createTransfer(Long senderId, Long recipientId, Long amount);

    TransferByEmailDTO createTransferToUserByEmail(Long senderId, CreateTransferByEmailDTO transferPayload);

    TransferByPhoneNumberDTO createTransferToUserByPhoneNumber(Long senderId, CreateTransferByPhoneNumberDTO transferPayload);

//    List<TransferDataDTO> findAllTransfersByUserId(Long userId);

    TransferDataDTO findTransferById(Long id);

//    List<TransferDataDTO> findTransfersByStatus(Long userId, TransferStatus status);

//    List<TransferDataDTO> findTransfersByDirectionType(Long userId, TransferDirectionType type);

    List<TransferDataDTO> findTransfersByDirectionTypeAndStatus(Long userId, TransferDirectionType type, TransferStatus status);
}
