package ru.cft.template.core.service.transfer;

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
    @Transactional
    public TransferByEmailDTO createTransferToUserByEmail(Long id, CreateTransferByEmailDTO transferPayload) {
        Wallet senderWallet = walletService.findByIdEntity(id);
        Wallet recipientWallet = walletService.findByUserEmail(transferPayload.recipientEmail());
        Transfer transfer = transferMapper.mapToTransfer(transferPayload.amount(), senderWallet, recipientWallet, null);

        validateCreatedTransfer(senderWallet, recipientWallet, transfer);
        transferFromSenderToRecipient(senderWallet, recipientWallet, transfer);

        return transferMapper.mapToTransferByEmail(transferRepository.save(transfer));
    }

    @Override
    @Transactional
    public TransferByPhoneNumberDTO createTransferToUserByPhoneNumber(Long id, CreateTransferByPhoneNumberDTO transferPayload) {
        Wallet senderWallet = walletService.findByIdEntity(id);
        Wallet recipientWallet = walletService.findByUserPhoneNumber(transferPayload.recipientPhoneNumber());
        Transfer transfer = transferMapper.mapToTransfer(transferPayload.amount(), senderWallet, recipientWallet, null);

        validateCreatedTransfer(senderWallet, recipientWallet, transfer);
        transferFromSenderToRecipient(senderWallet, recipientWallet, transfer);

        return transferMapper.mapToTransferByPhoneNumber(transferRepository.save(transfer));
    }

    @Override
    public List<TransferDataDTO> findAllTransfersByUserId(Long userId) {
        return transferRepository.findAllBySenderWalletIdAndRecipientWalletId(userId, userId)
                .stream()
                .map(transferMapper::mapToTransferData)
                .toList();
    }

    @Override
    public TransferDataDTO findTransferById(Long id) {
        return transferMapper.mapToTransferData(
                transferRepository.findById(id).orElseThrow(() -> new ServiceException("Перевод с id - %d не найден.".formatted(id)))
        );
    }

    @Override
    public List<TransferDataDTO> findTransfersByStatus(Long userId, TransferStatus status) {
        return transferRepository.findAllBySenderWalletIdAndRecipientWalletIdAndStatus(userId, userId, status)
                .stream()
                .map(transferMapper::mapToTransferData)
                .toList();
    }

    @Override
    public List<TransferDataDTO> findTransfersByDirectionType(Long userId, TransferDirectionType type) {
        if (type.equals(TransferDirectionType.INCOMING)) {
            return transferRepository.findAllByRecipientWalletId(userId).stream()
                    .map(transferMapper::mapToTransferData)
                    .toList();
        }
        return transferRepository.findAllBySenderWalletId(userId).stream()
                .map(transferMapper::mapToTransferData)
                .toList();
    }

    @Override
    public List<TransferDataDTO> findTransfersByDirectionTypeAndStatus(Long userId, TransferDirectionType type, TransferStatus status) {
        if (type.equals(TransferDirectionType.INCOMING))
            return transferRepository.findAllByRecipientWalletIdAndStatus(userId, status).stream()
                    .map(transferMapper::mapToTransferData)
                    .toList();
        return this.transferRepository.findAllBySenderWalletIdAndStatus(userId, status).stream()
                .map(transferMapper::mapToTransferData)
                .toList();
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

    private void transferFromSenderToRecipient(Wallet senderWallet, Wallet recipientWallet, Transfer transfer) {
        senderWallet.setBalance(senderWallet.getBalance() - transfer.getAmount());
        recipientWallet.setBalance(recipientWallet.getBalance() + transfer.getAmount());
        transfer.setStatus(TransferStatus.SUCCESSFUL);
    }
}
