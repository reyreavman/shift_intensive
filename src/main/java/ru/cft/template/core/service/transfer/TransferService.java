package ru.cft.template.core.service.transfer;

import ru.cft.template.api.dto.transfer.TransferByEmailDTO;
import ru.cft.template.api.dto.transfer.TransferByPhoneNumberDTO;
import ru.cft.template.api.dto.transfer.TransferDataDTO;
import ru.cft.template.api.payload.transfer.TransferByEmailPayload;
import ru.cft.template.api.payload.transfer.TransferByPhoneNumberPayload;
import ru.cft.template.core.entity.transfer.TransferDirectionType;
import ru.cft.template.core.entity.transfer.TransferStatus;

import java.util.List;

public interface TransferService {
    TransferByEmailDTO createTransferToUserByEmail(Long senderId, TransferByEmailPayload transferPayload);

    TransferByPhoneNumberDTO createTransferToUserByPhoneNumber(Long senderId, TransferByPhoneNumberPayload transferPayload);

    List<TransferDataDTO> findAllTransfersByUserId(Long userId);

    TransferDataDTO findTransferById(Long id);

    List<TransferDataDTO> findTransfersByStatus(Long userId, TransferStatus status);

    List<TransferDataDTO> findTransfersByDirectionType(Long userId, TransferDirectionType type);

    List<TransferDataDTO> findTransfersByDirectionTypeAndStatus(Long userId, TransferDirectionType type, TransferStatus status);
}
