package ru.cft.template.core.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.cft.template.api.dto.wallet.HesoyamResult;
import ru.cft.template.api.dto.wallet.WalletDTO;
import ru.cft.template.api.dto.wallet.WalletHesoyamDTO;
import ru.cft.template.core.entity.User;
import ru.cft.template.core.entity.Wallet;
import ru.cft.template.core.exception.service.ServiceException;
import ru.cft.template.core.mapper.WalletMapper;
import ru.cft.template.core.repository.WalletRepository;
import ru.cft.template.core.service.wallet.DefaultWalletService;
import ru.cft.template.core.utils.HesoyamRouletteGeneratorUtil;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
public class DefaultWalletServiceTest {
    @Mock
    WalletRepository walletRepository;
    @Mock
    WalletMapper walletMapper;
    @Mock
    HesoyamRouletteGeneratorUtil hesoyamRouletteGenerator;

    @InjectMocks
    DefaultWalletService service;

    User preparedUser = User.builder()
            .id(1L)
            .firstName("Радмир")
            .middleName("Рустамович")
            .lastName("Хурум")
            .phoneNumber("79999999999")
            .email("rExample@example.com")
            .birthdate(LocalDate.parse("2004-06-01", DateTimeFormatter.ISO_DATE))
            .passwordHash("hashed_password")
            .build();
    Wallet preparedWallet = new Wallet(1L, preparedUser, 100L);
    WalletDTO preparedWalletDTO = new WalletDTO(preparedWallet.getId(), preparedWallet.getBalance());
    WalletHesoyamDTO preparedWinnerWalletHesoyamDTO = new WalletHesoyamDTO(1L, HesoyamResult.WINNER, 10L, 110L);
    WalletHesoyamDTO preparedLoserWalletHesoyamDTO = new WalletHesoyamDTO(1L, HesoyamResult.LOSER, 10L, 110L);

    @Before
    public void prepareMocks() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void findById_ButNoWalletInDB_ThrowsServiceException() {
        Long userId = 1L;
        doReturn(Optional.empty())
                .when(walletRepository).findById(userId);

        assertThrows(ServiceException.class, () -> service.findById(userId));
        verify(walletRepository).findById(userId);
        verifyNoMoreInteractions(walletRepository);
        verifyNoInteractions(walletMapper);
    }

    @Test
    public void findWalletByUserId_ReturnsWalletDTO() {
        Long userId = 1L;
        doReturn(Optional.of(preparedWallet))
                .when(walletRepository).findById(userId);
        doReturn(preparedWalletDTO)
                .when(walletMapper).mapToWalletDTO(preparedWallet);

        WalletDTO actualWalletDTO = service.findById(userId);

        assertEquals(preparedWalletDTO, actualWalletDTO);
        verify(walletRepository).findById(userId);
        verifyNoMoreInteractions(walletRepository);
        verify(walletMapper).mapToWalletDTO(preparedWallet);
        verifyNoMoreInteractions(walletMapper);
    }

    @Test
    public void findById_ReturnsWalletEntity() {
        Long userId = 1L;
        doReturn(Optional.of(preparedWallet))
                .when(walletRepository).findById(userId);

        Wallet actualWallet = service.findByIdEntity(userId);

        assertEquals(preparedWallet, actualWallet);
        verify(walletRepository).findById(userId);
        verifyNoMoreInteractions(walletRepository);
        verifyNoInteractions(walletMapper);
    }

    @Test
    public void findByUserPhoneNumber_ButNoWalletsWithGivenPhoneNumber_ThrowsServiceException() {
        String phoneNumber = "79999999999";
        doReturn(Optional.empty())
                .when(walletRepository).findByUserPhoneNumber(phoneNumber);


        assertThrows(ServiceException.class, () -> service.findByUserPhoneNumber(phoneNumber));
        verify(walletRepository).findByUserPhoneNumber(phoneNumber);
        verifyNoMoreInteractions(walletRepository);
    }

    @Test
    public void findByUserPhoneNumber_ReturnsWalletEntity() {
        String phoneNumber = "79999999999";
        doReturn(Optional.of(preparedWallet))
                .when(walletRepository).findByUserPhoneNumber(phoneNumber);

        Wallet actualWallet = service.findByUserPhoneNumber(phoneNumber);

        assertEquals(preparedWallet, actualWallet);
        verify(walletRepository).findByUserPhoneNumber(phoneNumber);
        verifyNoMoreInteractions(walletRepository);
        verifyNoInteractions(walletMapper);
    }

    @Test
    public void findByUserEmail_ButNoWalletsWithGivenEmail_ThrowsServiceException() {
        String email = "rExample@example.com";
        doReturn(Optional.empty())
                .when(walletRepository).findByUserEmail(email);


        assertThrows(ServiceException.class, () -> service.findByUserEmail(email));
        verify(walletRepository).findByUserEmail(email);
        verifyNoMoreInteractions(walletRepository);
    }

    @Test
    public void findByUserEmail_ReturnsWalletEntity() {
        String email = "rExample@example.com";
        doReturn(Optional.of(preparedWallet))
                .when(walletRepository).findByUserEmail(email);

        Wallet actualWallet = service.findByUserEmail(email);

        assertEquals(preparedWallet, actualWallet);
        verify(walletRepository).findByUserEmail(email);
        verifyNoMoreInteractions(walletRepository);
        verifyNoInteractions(walletMapper);
    }

    @Test
    public void hesoyam_ButNoWalletsWithGivenId_ThrowsServiceException() {
        Long id = 1L;
        doReturn(Optional.empty())
                .when(walletRepository).findById(id);


        assertThrows(ServiceException.class, () -> service.findById(id));
        verify(walletRepository).findById(id);
        verifyNoMoreInteractions(walletRepository);
    }

    @Test
    public void hesoyam_ReturnsHesoyamWinner() {
        Long id = 1L;
        HesoyamResult result = HesoyamResult.WINNER;
        doReturn(result)
                .when(hesoyamRouletteGenerator).call();
        doReturn(Optional.of(preparedWallet))
                .when(walletRepository).findById(id);
        doReturn(preparedWinnerWalletHesoyamDTO)
                .when(walletMapper).mapToWalletHesoyam(preparedWallet, result, 0L);

        WalletHesoyamDTO hesoyamDTO = service.hesoyam(id);

        assertEquals(preparedWinnerWalletHesoyamDTO, hesoyamDTO);
        verify(walletRepository).findById(id);
        verifyNoMoreInteractions(walletRepository);
        verify(hesoyamRouletteGenerator).call();
        verifyNoMoreInteractions(hesoyamRouletteGenerator);
        verify(walletMapper).mapToWalletHesoyam(preparedWallet, result, 0L);
        verifyNoMoreInteractions(walletMapper);

    }
}