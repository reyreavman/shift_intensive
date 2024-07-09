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
import ru.cft.template.core.repository.UserRepository;
import ru.cft.template.core.repository.WalletRepository;

import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class DefaultTransferService implements TransferService {
    private final TransferRepository transferRepository;
    private final WalletRepository walletRepository;
    private final UserRepository userRepository;

    private final TransferMapper transferMapper;

    @Override
    @Transactional
    public TransferByEmailDTO createTransferToUserByEmail(Long senderId, CreateTransferByEmailDTO transferPayload) {
        Wallet senderWallet = walletRepository.findById(senderId).orElseThrow();
        Wallet recipientWallet = walletRepository.findById(userRepository.findByEmail(transferPayload.recipientEmail()).orElseThrow().getId()).get();
        Transfer transfer = transferMapper.mapToTransfer(transferPayload.amount(), senderWallet, recipientWallet, null);
        if (senderWallet.equals(recipientWallet))
            throw new ServiceException("Попытка совершения перевода самому себе.");
        if (senderWallet.getBalance() < transferPayload.amount()) {
            transfer.setStatus(TransferStatus.FAILED);
            transferRepository.save(transfer);
            throw new ServiceException("Денег на счету пользователя - %d недостаточно для совершения перевода.".formatted(senderId));
        }
        senderWallet.setBalance(senderWallet.getBalance() - transferPayload.amount());
        recipientWallet.setBalance(recipientWallet.getBalance() + transferPayload.amount());
        transfer.setStatus(TransferStatus.FAILED);
        return transferMapper.mapToTransferByEmail(transferRepository.save(transfer));
    }

    @Override
    @Transactional
    public TransferByPhoneNumberDTO createTransferToUserByPhoneNumber(Long senderId, CreateTransferByPhoneNumberDTO transferPayload) {
        Wallet senderWallet = walletRepository.findById(senderId).orElseThrow();
        Wallet recipientWallet = walletRepository.findById(userRepository.findByPhoneNumber(transferPayload.recipientPhoneNumber()).orElseThrow().getId()).get();
        Transfer transfer = transferMapper.mapToTransfer(transferPayload.amount(), senderWallet, recipientWallet, null);
        if (senderWallet.equals(recipientWallet))
            throw new ServiceException("Попытка совершения перевода самому себе.");
        if (senderWallet.getBalance() < transferPayload.amount()) {
            transfer.setStatus(TransferStatus.FAILED);
            transferRepository.save(transfer);
            throw new ServiceException("Денег на счету пользователя - %d недостаточно для совершения перевода.".formatted(senderId));
        }
        senderWallet.setBalance(senderWallet.getBalance() - transferPayload.amount());
        recipientWallet.setBalance(recipientWallet.getBalance() + transferPayload.amount());
        transfer.setStatus(TransferStatus.FAILED);
        return transferMapper.mapToTransferByPhoneNumber(transferRepository.save(transfer));
    }

    @Override
    public List<TransferDataDTO> findAllTransfersByUserId(Long userId) {
        Stream<Transfer> allUserTransfers = Stream.concat(
                transferRepository.findAllBySenderWalletId(userId).stream(),
                transferRepository.findAllByRecipientWalletId(userId).stream()
        );
        return allUserTransfers.map(transferMapper::mapToTransferData).toList();
    }

    @Override
    public TransferDataDTO findTransferById(Long id) {
        return transferMapper.mapToTransferData(transferRepository.findById(id).orElseThrow());
    }

    @Override
    public List<TransferDataDTO> findTransfersByStatus(Long userId, TransferStatus status) {
        return transferRepository.findAllByStatus(status).stream()
                .map(transferMapper::mapToTransferData)
                .toList();
    }

    @Override
    public List<TransferDataDTO> findTransfersByDirectionType(Long userId, TransferDirectionType type) {
        if (type.equals(TransferDirectionType.INCOMING)) {
            return transferRepository.findAllBySenderWalletId(userId).stream()
                    .map(transferMapper::mapToTransferData)
                    .toList();
        }
        return transferRepository.findAllByRecipientWalletId(userId).stream()
                .map(transferMapper::mapToTransferData)
                .toList();
    }

    @Override
    public List<TransferDataDTO> findTransfersByDirectionTypeAndStatus(Long userId, TransferDirectionType type, TransferStatus status) {
        if (type.equals(TransferDirectionType.INCOMING))
            return transferRepository.findAllByRecipientWalletId(userId).stream()
                    .filter(transfer -> transfer.getStatus().equals(status))
                    .map(transferMapper::mapToTransferData)
                    .toList();
        return this.transferRepository.findAllBySenderWalletId(userId).stream()
                .filter(transfer -> transfer.getStatus().equals(status))
                .map(transferMapper::mapToTransferData)
                .toList();

    }
}
