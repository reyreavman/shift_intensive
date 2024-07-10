package ru.cft.template.core.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.cft.template.api.dto.transfer.CreateTransferByEmailDTO;
import ru.cft.template.api.dto.transfer.CreateTransferByPhoneNumberDTO;
import ru.cft.template.api.dto.transfer.TransferByEmailDTO;
import ru.cft.template.api.dto.transfer.TransferByPhoneNumberDTO;
import ru.cft.template.core.entity.User;
import ru.cft.template.core.entity.Wallet;
import ru.cft.template.core.entity.transfer.Transfer;
import ru.cft.template.core.entity.transfer.TransferStatus;
import ru.cft.template.core.exception.service.ServiceException;
import ru.cft.template.core.mapper.TransferMapper;
import ru.cft.template.core.repository.TransferRepository;
import ru.cft.template.core.service.transfer.DefaultTransferService;
import ru.cft.template.core.service.wallet.WalletService;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
public class DefaultTransferServiceTest {
    @Mock
    TransferRepository transferRepository;
    @Mock
    WalletService walletService;
    @Mock
    TransferMapper transferMapper;

    @InjectMocks
    DefaultTransferService service;

    Wallet preparedSenderWallet = new Wallet(1L, User.builder()
            .id(1L)
            .firstName("Радмир")
            .lastName("Рустамович")
            .phoneNumber("Хурум")
            .email("rKhurum@example.com")
            .birthdate(LocalDate.of(2000, 1, 1))
            .passwordHash("Example1!")
            .build(), 1000L);
    Wallet preparedRecipientWallet = new Wallet(1L, User.builder()
            .id(2L)
            .firstName("Тест")
            .lastName("Тестович")
            .phoneNumber("70000000000")
            .email("test@example.com")
            .birthdate(LocalDate.of(1970, 1, 1))
            .passwordHash("Example1!")
            .build(), 1000L);
    Transfer preparedTransfer = Transfer.builder()
            .id(null)
            .senderWallet(preparedSenderWallet)
            .recipientWallet(preparedRecipientWallet)
            .amount(150L)
            .status(null)
            .build();

    @Test
    void createTransfer_ReturnsSavedTransfer() {
        long senderId = 1;
        long recipientId = 2;
        long amount = 150;
        Transfer expectedTransfer = Transfer.builder()
                .id(10L)
                .senderWallet(preparedSenderWallet)
                .recipientWallet(preparedRecipientWallet)
                .amount(150L)
                .status(TransferStatus.SUCCESSFUL)
                .build();

        doReturn(preparedSenderWallet)
                .when(walletService).findByIdEntity(senderId);
        doReturn(preparedRecipientWallet)
                .when(walletService).findByIdEntity(recipientId);
        doReturn(preparedTransfer)
                .when(transferMapper).mapToTransfer(amount, preparedSenderWallet, preparedRecipientWallet, null);
        doReturn(expectedTransfer)
                .when(transferRepository).save(preparedTransfer);

        Transfer actualTransfer = service.createTransfer(senderId, recipientId, amount);

        assertEquals(expectedTransfer, actualTransfer);
    }

    @Test
    void createTransfer_ButSenderDoesNotHaveEnoughMoney_SaveTransferWithFailedStatusAndThrowsServiceException() {
        long senderId = 1;
        long recipientId = 2;
        long amount = 1500;
        Transfer expectedTransfer = Transfer.builder()
                .id(10L)
                .senderWallet(preparedSenderWallet)
                .recipientWallet(preparedRecipientWallet)
                .amount(1500L)
                .status(TransferStatus.FAILED)
                .build();

        doReturn(preparedSenderWallet)
                .when(walletService).findByIdEntity(senderId);
        doReturn(preparedRecipientWallet)
                .when(walletService).findByIdEntity(recipientId);
        doReturn(expectedTransfer)
                .when(transferMapper).mapToTransfer(amount, preparedSenderWallet, preparedRecipientWallet, null);

        assertThrows(ServiceException.class, () -> service.createTransfer(senderId, recipientId, amount));
        verify(transferRepository).save(expectedTransfer);
        verifyNoMoreInteractions(transferRepository);
    }

    @Test
    void createTransfer_ButSenderTryingToTransferMoneyToHimself_ThrowsServiceException() {
        long senderId = 1;
        long recipientId = 1;
        long amount = 150;
        Transfer expectedTransfer = Transfer.builder()
                .id(10L)
                .senderWallet(preparedSenderWallet)
                .recipientWallet(preparedRecipientWallet)
                .amount(150L)
                .status(null)
                .build();

        assertThrows(ServiceException.class, () -> service.createTransfer(senderId, recipientId, amount));
        verifyNoInteractions(transferRepository);
        verifyNoInteractions(transferMapper);
        verifyNoInteractions(walletService);
    }

    @Test
    void createTransferToUserByEmail_ReturnsTransferByEmailDTO() {
        long id = 1;
        CreateTransferByEmailDTO emailDTO = new CreateTransferByEmailDTO("test@example.com", 150L);
        Transfer transfer = Transfer.builder()
                .id(10L)
                .senderWallet(preparedSenderWallet)
                .recipientWallet(preparedRecipientWallet)
                .amount(150L)
                .status(TransferStatus.SUCCESSFUL)
                .build();
        TransferByEmailDTO expectedDTO = TransferByEmailDTO.builder()
                .id(10L)
                .senderId(id)
                .recipientEmail(emailDTO.recipientEmail())
                .amount(emailDTO.amount())
                .dateTime(LocalDateTime.now())
                .status(TransferStatus.SUCCESSFUL)
                .build();

        doReturn(preparedSenderWallet)
                .when(walletService).findByIdEntity(id);
        doReturn(preparedRecipientWallet)
                .when(walletService).findByUserEmail(emailDTO.recipientEmail());
        doReturn(preparedTransfer)
                .when(transferMapper).mapToTransfer(emailDTO.amount(), preparedSenderWallet, preparedRecipientWallet, null);
        doReturn(transfer)
                .when(transferRepository).save(preparedTransfer);
        doReturn(expectedDTO)
                .when(transferMapper).mapToTransferByEmail(transfer);

        TransferByEmailDTO actualDTO = service.createTransferToUserByEmail(id, emailDTO);

        assertEquals(expectedDTO, actualDTO);
    }

    @Test
    void createTransferToUserByPhoneNumbere_ReturnsTransferByEmailDTO() {
        long id = 1;
        CreateTransferByPhoneNumberDTO phoneNumberDTO = new CreateTransferByPhoneNumberDTO("70000000000", 150L);
        Transfer transfer = Transfer.builder()
                .id(10L)
                .senderWallet(preparedSenderWallet)
                .recipientWallet(preparedRecipientWallet)
                .amount(150L)
                .status(TransferStatus.SUCCESSFUL)
                .build();
        TransferByPhoneNumberDTO expectedDTO = TransferByPhoneNumberDTO.builder()
                .id(10L)
                .senderId(id)
                .recipientPhoneNumber(phoneNumberDTO.recipientPhoneNumber())
                .amount(phoneNumberDTO.amount())
                .dateTime(LocalDateTime.now())
                .status(TransferStatus.SUCCESSFUL)
                .build();

        doReturn(preparedSenderWallet)
                .when(walletService).findByIdEntity(id);
        doReturn(preparedRecipientWallet)
                .when(walletService).findByUserPhoneNumber(phoneNumberDTO.recipientPhoneNumber());
        doReturn(preparedTransfer)
                .when(transferMapper).mapToTransfer(phoneNumberDTO.amount(), preparedSenderWallet, preparedRecipientWallet, null);
        doReturn(transfer)
                .when(transferRepository).save(preparedTransfer);
        doReturn(expectedDTO)
                .when(transferMapper).mapToTransferByPhoneNumber(transfer);

        TransferByPhoneNumberDTO actualDTO = service.createTransferToUserByPhoneNumber(id, phoneNumberDTO);

        assertEquals(expectedDTO, actualDTO);
    }
}
