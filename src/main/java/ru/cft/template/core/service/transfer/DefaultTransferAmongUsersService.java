package ru.cft.template.core.service.transfer;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.cft.template.api.dto.transfer.TransferAmongUsersDataDTO;
import ru.cft.template.api.dto.transfer.TransferByEmailDTO;
import ru.cft.template.api.dto.transfer.TransferByPhoneNumberDTO;
import ru.cft.template.api.payload.transfer.NewTransferByEmailPayload;
import ru.cft.template.api.payload.transfer.NewTransferByPhoneNumberPayload;
import ru.cft.template.core.entity.User;
import ru.cft.template.core.entity.Wallet;
import ru.cft.template.core.entity.transfer.TransferAmongUsers;
import ru.cft.template.core.entity.transfer.TransferDirectionType;
import ru.cft.template.core.entity.transfer.TransferStatus;
import ru.cft.template.core.exception.EntityNotFoundException;
import ru.cft.template.core.exception.NotEnoughMoneyException;
import ru.cft.template.core.exception.SelfTransferException;
import ru.cft.template.core.repository.TransferAmongUsersRepository;
import ru.cft.template.core.repository.UserRepository;
import ru.cft.template.core.repository.WalletRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class DefaultTransferAmongUsersService implements TransferAmongUsersService {
    private final TransferAmongUsersRepository transferRepository;
    private final WalletRepository walletRepository;
    private final UserRepository userRepository;

    private final ConversionService conversionService;

    @Override
    @Transactional
    public TransferByEmailDTO createTransferToUserByEmail(Long senderId, @Valid NewTransferByEmailPayload transferPayload) {
        Wallet senderWallet = this.walletRepository.findById(senderId)
                .orElseThrow(() -> new EntityNotFoundException(Wallet.class.getSimpleName(), Map.of("senderWalletId", String.valueOf(senderId))));
        Long recipientId = this.userRepository.findByEmail(transferPayload.recipientEmail())
                .orElseThrow(() -> new EntityNotFoundException(User.class.getSimpleName(), Map.of("recipientEmail", String.valueOf(transferPayload.recipientEmail())))).getId();
        Wallet recipientWallet = this.walletRepository.findById(recipientId)
                .orElseThrow(() -> new EntityNotFoundException(Wallet.class.getSimpleName(), Map.of("recipientWalletId", String.valueOf(recipientId))));
        if (senderWallet.equals(recipientWallet))
            throw new SelfTransferException(senderWallet.getId(), recipientWallet.getId());
        TransferAmongUsers transfer = TransferAmongUsers.builder()
                .id(null)
                .senderWallet(senderWallet)
                .recipientWallet(recipientWallet)
                .amount(transferPayload.amount())
                .status(senderWallet.getBalance() >= transferPayload.amount() ? TransferStatus.SUCCESSFUL : TransferStatus.FAILED)
                .dateTime(LocalDateTime.now())
                .recipientEmail(transferPayload.recipientEmail())
                .build();
        this.transferRepository.save(transfer);
        if (senderWallet.getBalance() < transferPayload.amount())
            throw new NotEnoughMoneyException(senderId, senderWallet.getBalance(), transferPayload.amount());
        else {
            senderWallet.setBalance(senderWallet.getBalance() - transferPayload.amount());
            recipientWallet.setBalance(recipientWallet.getBalance() + transferPayload.amount());
            return this.conversionService.convert(transfer, TransferByEmailDTO.class);
        }
    }

    @Override
    @Transactional
    public TransferByPhoneNumberDTO createTransferToUserByPhoneNumber(Long senderId, @Valid NewTransferByPhoneNumberPayload transferPayload) {
        Wallet senderWallet = this.walletRepository.findById(senderId)
                .orElseThrow(() -> new EntityNotFoundException(Wallet.class.getSimpleName(), Map.of("senderWalletId", String.valueOf(senderId))));
        Long recipientId = this.userRepository.findByPhoneNumber(transferPayload.phoneNumber())
                .orElseThrow(() -> new EntityNotFoundException(User.class.getSimpleName(), Map.of("recipientPhoneNumber", String.valueOf(transferPayload.phoneNumber())))).getId();
        Wallet recipientWallet = this.walletRepository.findById(recipientId)
                .orElseThrow(() -> new EntityNotFoundException(Wallet.class.getSimpleName(), Map.of("recipientWalletId", String.valueOf(recipientId))));
        if (senderWallet.equals(recipientWallet))
            throw new SelfTransferException(senderWallet.getId(), recipientWallet.getId());
        TransferAmongUsers transfer = TransferAmongUsers.builder()
                .id(null)
                .senderWallet(senderWallet)
                .recipientWallet(recipientWallet)
                .amount(transferPayload.amount())
                .status(senderWallet.getBalance() >= transferPayload.amount() ? TransferStatus.SUCCESSFUL : TransferStatus.FAILED)
                .dateTime(LocalDateTime.now())
                .recipientPhoneNumber(transferPayload.phoneNumber())
                .build();
        this.transferRepository.save(transfer);
        if (senderWallet.getBalance() < transferPayload.amount())
            throw new NotEnoughMoneyException(senderId, senderWallet.getBalance(), transferPayload.amount());
        else {
            senderWallet.setBalance(senderWallet.getBalance() - transferPayload.amount());
            recipientWallet.setBalance(recipientWallet.getBalance() + transferPayload.amount());
            return this.conversionService.convert(transfer, TransferByPhoneNumberDTO.class);
        }
    }

    @Override
    public List<TransferAmongUsersDataDTO> findAllTransfersByUserId(Long userId) {
        Stream<TransferAmongUsers> allUserTransfers = Stream.concat(
                this.transferRepository.findAllBySenderWalletId(userId).stream(),
                this.transferRepository.findAllByRecipientWalletId(userId).stream()
        );
        return allUserTransfers.map(transfer -> this.conversionService.convert(transfer, TransferAmongUsersDataDTO.class)).toList();
    }

    @Override
    public TransferAmongUsersDataDTO findTransferById(Long id) {
        return this.conversionService.convert(this.transferRepository.findById(id), TransferAmongUsersDataDTO.class);
    }

    @Override
    public List<TransferAmongUsersDataDTO> findTransfersByStatus(Long userId, TransferStatus status) {
        return this.transferRepository.findAllByStatus(status)
                .stream()
                .filter(transfer -> transfer.getId().equals(userId))
                .map(transfer -> this.conversionService.convert(transfer, TransferAmongUsersDataDTO.class))
                .toList();
    }

    @Override
    public List<TransferAmongUsersDataDTO> findTransfersByDirectionType(Long userId, TransferDirectionType type) {
        if (type.equals(TransferDirectionType.INCOMING))
            return this.transferRepository.findAllByRecipientWalletId(userId)
                    .stream()
                    .map(transfer -> this.conversionService.convert(transfer, TransferAmongUsersDataDTO.class))
                    .toList();
        return this.transferRepository.findAllBySenderWalletId(userId)
                .stream()
                .map(transfer -> this.conversionService.convert(transfer, TransferAmongUsersDataDTO.class))
                .toList();
    }

    @Override
    public List<TransferAmongUsersDataDTO> findTransfersByDirectionTypeAndStatus(Long userId, TransferDirectionType type, TransferStatus status) {
        if (type.equals(TransferDirectionType.INCOMING))
            return this.transferRepository.findAllByRecipientWalletId(userId)
                    .stream()
                    .filter(transfer -> transfer.getStatus().equals(status))
                    .map(transfer -> this.conversionService.convert(transfer, TransferAmongUsersDataDTO.class))
                    .toList();
        return this.transferRepository.findAllBySenderWalletId(userId)
                .stream()
                .filter(transfer -> transfer.getStatus().equals(status))
                .map(transfer -> this.conversionService.convert(transfer, TransferAmongUsersDataDTO.class))
                .toList();

    }
}
