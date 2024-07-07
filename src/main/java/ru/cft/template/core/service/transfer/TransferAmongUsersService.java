package ru.cft.template.core.service.transfer;

import jakarta.validation.Valid;
import ru.cft.template.api.dto.transfer.TransferAmongUsersDataDTO;
import ru.cft.template.api.dto.transfer.TransferByEmailDTO;
import ru.cft.template.api.dto.transfer.TransferByPhoneNumberDTO;
import ru.cft.template.api.payload.transfer.NewTransferByEmailPayload;
import ru.cft.template.api.payload.transfer.NewTransferByPhoneNumberPayload;
import ru.cft.template.core.entity.transfer.TransferDirectionType;
import ru.cft.template.core.entity.transfer.TransferStatus;

import java.util.List;

public interface TransferAmongUsersService {
    TransferByEmailDTO createTransferToUserByEmail(Long senderId, @Valid NewTransferByEmailPayload transferPayload);

    TransferByPhoneNumberDTO createTransferToUserByPhoneNumber(Long senderId, @Valid NewTransferByPhoneNumberPayload transferPayload);

    List<TransferAmongUsersDataDTO> findAllTransfersByUserId(Long userId);

    TransferAmongUsersDataDTO findTransferById(Long id);

    List<TransferAmongUsersDataDTO> findTransfersByStatus(TransferStatus status);

    List<TransferAmongUsersDataDTO> findTransfersByType(TransferDirectionType type);

    List<TransferAmongUsersDataDTO> findTransfersByTypeAndStatus(TransferDirectionType type, TransferStatus status);
}
