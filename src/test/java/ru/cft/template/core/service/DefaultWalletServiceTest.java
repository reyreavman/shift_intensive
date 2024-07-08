package ru.cft.template.core.service;

import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.ConversionService;
import ru.cft.template.api.dto.wallet.WalletDTO;
import ru.cft.template.api.dto.wallet.WalletHesoyamDTO;
import ru.cft.template.core.entity.User;
import ru.cft.template.core.entity.Wallet;
import ru.cft.template.core.exception.EntityNotFoundException;
import ru.cft.template.core.repository.UserRepository;
import ru.cft.template.core.repository.WalletRepository;
import ru.cft.template.core.service.wallet.DefaultWalletService;
import ru.cft.template.core.utils.HesoyamRouletteGeneratorUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    UserRepository userRepository;
    @Mock
    ConversionService conversionService;
    @Mock
    HesoyamRouletteGeneratorUtil hesoyamRouletteGeneratorUtil;

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
            .registrationDateTime(LocalDateTime.now())
            .lastUpdateDateTime(null)
            .build();
    Wallet preparedWallet = new Wallet(1L, preparedUser, 100L);
    WalletDTO preparedWalletDTO = new WalletDTO(preparedWallet.getId(), preparedWallet.getUser().getId(), preparedWallet.getBalance());
    WalletHesoyamDTO preparedWinnerWalletHesoyamDTO = new WalletHesoyamDTO(1L, "win", 10L, 110L);
    WalletHesoyamDTO preparedLoserWalletHesoyamDTO = new WalletHesoyamDTO(1L, "lose", 10L, 110L);

//    @Before
//    public void prepareMocks() {
//        MockitoAnnotations.openMocks(this);
//    }

    @Test
    public void findWalletByUserId_WithFirstCallToGetWalletButNoWalletAndUserFound_ThrowsEntityNotFoundException() {
        Long userId = 1L;
        doReturn(Optional.empty())
                .when(this.walletRepository).findById(userId);
        doReturn(Optional.empty())
                .when(this.userRepository).findById(userId);

        assertThrows(EntityNotFoundException.class, () -> this.service.findByUserId(userId));
        verify(this.walletRepository).findById(userId);
        verifyNoMoreInteractions(this.walletRepository);
        verify(this.userRepository).findById(userId);
        verifyNoMoreInteractions(this.userRepository);
        verifyNoInteractions(this.conversionService);
    }

    @Test
    public void findWalletByUserId_WithFirstCallToGetWalletButNoWalletFound_CreatesWalletAndReturnsWalletDTO() {
        Long userId = 1L;
        Wallet walletToSave = new Wallet(null, preparedUser, 100L);
        doReturn(Optional.empty())
                .when(this.walletRepository).findById(userId);
        doReturn(Optional.of(this.preparedUser))
                .when(this.userRepository).findById(userId);
        doReturn(this.preparedWallet)
                .when(this.walletRepository).save(walletToSave);
        doReturn(this.preparedWalletDTO)
                .when(this.conversionService).convert(this.preparedWallet, WalletDTO.class);

        WalletDTO actualWalletDTO = this.service.findByUserId(userId);

        assertEquals(preparedWalletDTO, actualWalletDTO);
        verify(this.userRepository).findById(userId);
        verifyNoMoreInteractions(this.userRepository);
        verify(this.walletRepository).findById(userId);
        verify(this.walletRepository).save(walletToSave);
        verifyNoMoreInteractions(this.walletRepository);
        verify(this.conversionService).convert(preparedWallet, WalletDTO.class);
        verifyNoMoreInteractions(this.conversionService);
    }

    @Test
    public void findWalletByUserId_ReturnsWalletDTO() {
        Long userId = 1L;
        doReturn(Optional.of(this.preparedWallet))
                .when(this.walletRepository).findById(userId);
        doReturn(this.preparedWalletDTO)
                .when(this.conversionService).convert(this.preparedWallet, WalletDTO.class);

        WalletDTO actualWalletDTO = this.service.findByUserId(userId);

        assertEquals(preparedWalletDTO, actualWalletDTO);
        verify(this.walletRepository).findById(userId);
        verifyNoMoreInteractions(this.userRepository);
        verify(this.conversionService).convert(preparedWallet, WalletDTO.class);
        verifyNoMoreInteractions(this.conversionService);
    }

    @Test
    public void hesoyam_WithFirstCallToGetWallet_ThrowsEntityNotFoundException() {
        Long userId = 1L;
        doReturn(Optional.empty())
                .when(this.walletRepository).findById(userId);

        assertThrows(EntityNotFoundException.class,() -> this.service.hesoyam(userId));
        verify(this.walletRepository).findById(userId);
        verifyNoInteractions(this.hesoyamRouletteGeneratorUtil);
        verifyNoInteractions(this.conversionService);
    }

    @Test
    public void hesoyam_Winner_ReturnsWalletHesoyamDTO() {
        Long userId = 1L;
        doReturn(Optional.of(this.preparedWallet))
                .when(this.walletRepository).findById(userId);
        doReturn(true)
                .when(this.hesoyamRouletteGeneratorUtil).call();
        doReturn(this.preparedWinnerWalletHesoyamDTO)
                .when(this.conversionService).convert(this.preparedWallet, WalletHesoyamDTO.class);

        WalletHesoyamDTO actualDTO = this.service.hesoyam(1L);

        assertEquals(this.preparedWinnerWalletHesoyamDTO, actualDTO);
        verify(this.walletRepository).findById(userId);
        verifyNoMoreInteractions(this.walletRepository);
        verify(this.hesoyamRouletteGeneratorUtil).call();
        verifyNoMoreInteractions(this.hesoyamRouletteGeneratorUtil);
        verify(this.conversionService).convert(preparedWallet, WalletHesoyamDTO.class);
        verifyNoMoreInteractions(this.conversionService);
    }

    @Test
    public void hesoyam_Loser_ReturnsWalletHesoyamDTO() {
        Long userId = 1L;
        doReturn(Optional.of(this.preparedWallet))
                .when(this.walletRepository).findById(userId);
        doReturn(true)
                .when(this.hesoyamRouletteGeneratorUtil).call();
        doReturn(this.preparedLoserWalletHesoyamDTO)
                .when(this.conversionService).convert(this.preparedWallet, WalletHesoyamDTO.class);

        WalletHesoyamDTO actualDTO = this.service.hesoyam(1L);

        assertEquals(this.preparedLoserWalletHesoyamDTO, actualDTO);
        verify(this.walletRepository).findById(userId);
        verifyNoMoreInteractions(this.walletRepository);
        verify(this.hesoyamRouletteGeneratorUtil).call();
        verifyNoMoreInteractions(this.hesoyamRouletteGeneratorUtil);
        verify(this.conversionService).convert(preparedWallet, WalletHesoyamDTO.class);
        verifyNoMoreInteractions(this.conversionService);
    }
}
