package ru.cft.template.core.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.cft.template.api.dto.user.CreateUserDTO;
import ru.cft.template.api.dto.user.UserDTO;
import ru.cft.template.core.entity.User;
import ru.cft.template.core.entity.Wallet;
import ru.cft.template.core.exception.service.ServiceException;
import ru.cft.template.core.mapper.UserMapper;
import ru.cft.template.core.repository.UserRepository;
import ru.cft.template.core.repository.WalletRepository;
import ru.cft.template.core.service.user.DefaultUserService;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
public class DefaultUserServiceTest {
    @Mock
    WalletRepository walletRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    UserMapper userMapper;
    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    DefaultUserService service;

    User savedUser = User.builder()
            .id(1L)
            .firstName("Радмир")
            .middleName("Рустамович")
            .lastName("Хурум")
            .phoneNumber("79999999999")
            .email("rkhurum@example.com")
            .birthdate(LocalDate.parse("2004-01-01"))
            .passwordHash("hashedPassword")
            .build();
    UserDTO expectedUserDTO = UserDTO.builder()
            .id(1L)
            .firstName("Радмир")
            .middleName("Рустамович")
            .lastName("Хурум")
            .phoneNumber("79999999999")
            .email("rkhurum@example.com")
            .birthdate(LocalDate.parse("2004-01-01"))
            .build();

    @Before
    public void prepareMocks() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void createUser_WithFullyGivenInfo_ReturnsCreatedUserDTO() {
        CreateUserDTO userToCreatePayload = CreateUserDTO.builder()
                .firstName("Радмир")
                .middleName("Рустамович")
                .lastName("Хурум")
                .phoneNumber("79999999999")
                .email("rkhurum@example.com")
                .birthdate(LocalDate.of(2000, 6, 1))
                .password("Example1!")
                .build();
        User userPayloadConvertedToEntity = User.builder()
                .id(null)
                .firstName("Радмир")
                .middleName("Рустамович")
                .lastName("Хурум")
                .phoneNumber("79999999999")
                .email("rkhurum@example.com")
                .birthdate(LocalDate.of(2000, 6, 1))
                .passwordHash("Example1!")
                .build();

        doReturn(userPayloadConvertedToEntity)
                .when(this.userMapper).mapToUser(userToCreatePayload, savedUser.getPasswordHash());
        doReturn(savedUser.getPasswordHash())
                .when(this.passwordEncoder).encode(userToCreatePayload.password());
        doReturn(savedUser)
                .when(this.userRepository).save(userPayloadConvertedToEntity);
        doReturn(expectedUserDTO)
                .when(this.userMapper).mapToUserDTO(savedUser);
        doReturn(null)
                .when(this.walletRepository).save(new Wallet(null, savedUser, 100L));

        UserDTO actualUser = this.service.createUser(userToCreatePayload);

        assertEquals(expectedUserDTO, actualUser);
        verify(this.userRepository).save(userPayloadConvertedToEntity);
        verifyNoMoreInteractions(this.userRepository);
    }


    @Test
    public void findUser_ByNullableId_ThrowsEntityNotFoundException() {
        Long id = null;

        assertThrows(ServiceException.class, () -> this.service.findUserById(id));
        verify(this.userRepository).findById(id);
        verifyNoMoreInteractions(this.userRepository);
    }

    @Test
    public void findUser_ByNullableEmail_ThrowsEntityNotFoundException() {
        String email = null;

        assertThrows(ServiceException.class, () -> this.service.findUserByEmail(email));
        verify(this.userRepository).findByEmail(email);
        verifyNoMoreInteractions(this.userRepository);
    }

    @Test
    public void findUser_ByNullablePhoneNumber_ThrowsEntityNotFoundException() {
        String phoneNumber = null;

        assertThrows(ServiceException.class, () -> this.service.findUserByPhoneNumber(phoneNumber));
        verify(this.userRepository).findByPhoneNumber(phoneNumber);
        verifyNoMoreInteractions(this.userRepository);
    }

    @Test
    public void findUser_ById_ReturnsUserFoundById() {
        Long id = 1L;
        doReturn(Optional.of(savedUser))
                .when(this.userRepository).findById(id);
        doReturn(expectedUserDTO)
                .when(this.userMapper).mapToUserDTO(savedUser);

        UserDTO actualUserDTO = this.service.findUserById(id);

        assertEquals(expectedUserDTO, actualUserDTO);
        verify(this.userRepository).findById(id);
        verifyNoMoreInteractions(this.userRepository);
    }

    @Test
    public void findUser_ByEmail_returnsUserFoundByEmail() {
        String email = "rkhurum@example.com";
        doReturn(Optional.of(savedUser))
                .when(this.userRepository).findByEmail(email);
        doReturn(expectedUserDTO)
                .when(this.userMapper).mapToUserDTO(savedUser);

        UserDTO actualUserDTO = this.service.findUserByEmail(email);

        assertEquals(expectedUserDTO, actualUserDTO);
        verify(this.userRepository).findByEmail(email);
        verifyNoMoreInteractions(this.userRepository);
    }

    @Test
    public void findUser_ByPhoneNumber_returnsUserFoundByPhoneNumber() {
        String phoneNumber = "79999999999";
        doReturn(Optional.of(savedUser))
                .when(this.userRepository).findByPhoneNumber(phoneNumber);
        doReturn(expectedUserDTO)
                .when(this.userMapper).mapToUserDTO(savedUser);

        UserDTO actualUserDTO = this.service.findUserByPhoneNumber(phoneNumber);

        assertEquals(expectedUserDTO, actualUserDTO);
        verify(this.userRepository).findByPhoneNumber(phoneNumber);
        verifyNoMoreInteractions(this.userRepository);
    }
}
