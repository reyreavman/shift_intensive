package ru.cft.template.core.service.transfer;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.cft.template.api.dto.transfer.CreateTransferByEmailDTO;
import ru.cft.template.api.dto.transfer.CreateTransferByPhoneNumberDTO;
import ru.cft.template.api.dto.transfer.TransferByEmailDTO;
import ru.cft.template.api.dto.transfer.TransferByPhoneNumberDTO;
import ru.cft.template.api.dto.transfer.TransferDataDTO;
import ru.cft.template.core.entity.Wallet;
import ru.cft.template.core.entity.transfer.Transfer;
import ru.cft.template.core.entity.transfer.TransferDirectionType;
import ru.cft.template.core.entity.transfer.TransferStatus;
import ru.cft.template.core.exception.service.ServiceException;
import ru.cft.template.core.mapper.TransferMapper;
import ru.cft.template.core.repository.TransferRepository;
import ru.cft.template.core.service.wallet.WalletService;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class DefaultTransferService implements TransferService {
    private final TransferRepository transferRepository;
    private final WalletService walletService;

    private final TransferMapper transferMapper;

    @Override
    public Transfer createTransfer(Long senderId, Long recipientId, Long amount) {
        Wallet senderWallet = walletService.findByIdEntity(senderId);
        Wallet recipientWallet = walletService.findByIdEntity(recipientId);
        Transfer transfer = transferMapper.mapToTransfer(amount, senderWallet, recipientWallet, null);

        validateCreatedTransfer(senderWallet, recipientWallet, transfer);
        transferFromSenderToRecipient(senderWallet, recipientWallet, transfer);

        return transferRepository.save(transfer);
    }

    @Override
    public TransferByEmailDTO createTransferToUserByEmail(Long id, @Valid CreateTransferByEmailDTO transferPayload) {
        Wallet senderWallet = walletService.findByIdEntity(id);
        Wallet recipientWallet = walletService.findByUserEmail(transferPayload.recipientEmail());
        Transfer transfer = transferMapper.mapToTransfer(transferPayload.amount(), senderWallet, recipientWallet, null);

        validateCreatedTransfer(senderWallet, recipientWallet, transfer);
        transferFromSenderToRecipient(senderWallet, recipientWallet, transfer);

        return transferMapper.mapToTransferByEmail(transferRepository.save(transfer));
    }

    @Override
    public TransferByPhoneNumberDTO createTransferToUserByPhoneNumber(Long id, @Valid CreateTransferByPhoneNumberDTO transferPayload) {
        Wallet senderWallet = walletService.findByIdEntity(id);
        Wallet recipientWallet = walletService.findByUserPhoneNumber(transferPayload.recipientPhoneNumber());
        Transfer transfer = transferMapper.mapToTransfer(transferPayload.amount(), senderWallet, recipientWallet, null);

        validateCreatedTransfer(senderWallet, recipientWallet, transfer);
        transferFromSenderToRecipient(senderWallet, recipientWallet, transfer);

        return transferMapper.mapToTransferByPhoneNumber(transferRepository.save(transfer));
    }

    @Override
    public TransferDataDTO findTransferById(Long id) {
        return transferMapper.mapToTransferData(
                transferRepository.findById(id).orElseThrow(() -> new ServiceException("Перевод с id - %d не найден.".formatted(id)))
        );
    }

    @Override
    public List<TransferDataDTO> findTransfersByDirectionTypeAndStatus(Long userId, TransferDirectionType type, TransferStatus status) {
        List<TransferDataDTO> list = transferRepository.findAllBySenderWalletIdOrRecipientWalletId(userId, userId)
                .stream()
                .filter(transfer -> Objects.isNull(type) ||
                        (type.equals(TransferDirectionType.INCOMING) ?
                                transfer.getRecipientWallet().getId().equals(userId) :
                                transfer.getSenderWallet().getId().equals(userId)))
                .filter(transfer -> Objects.isNull(status) || transfer.getStatus().equals(status))
                .map(transferMapper::mapToTransferData)
                .toList();
        return list;
    }

    private void validateCreatedTransfer(Wallet senderWallet, Wallet recipientWallet, Transfer transfer) {
        if (senderWallet.equals(recipientWallet))
            throw new ServiceException("Попытка совершения перевода самому себе.");
        if (senderWallet.getBalance() < transfer.getAmount()) {
            transfer.setStatus(TransferStatus.FAILED);
            transferRepository.save(transfer);
            throw new ServiceException("Денег на счету пользователя - %d недостаточно для совершения перевода.".formatted(senderWallet.getId()));
        }
    }

    @Transactional
    private void transferFromSenderToRecipient(Wallet senderWallet, Wallet recipientWallet, Transfer transfer) {
        senderWallet.setBalance(senderWallet.getBalance() - transfer.getAmount());
        recipientWallet.setBalance(recipientWallet.getBalance() + transfer.getAmount());
        transfer.setStatus(TransferStatus.SUCCESSFUL);
    }
}
