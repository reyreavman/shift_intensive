package ru.cft.template.core.service.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.cft.template.api.dto.UserDTO;
import ru.cft.template.api.payload.NewUserPayload;
import ru.cft.template.api.payload.PatchUserPayload;
import ru.cft.template.core.entity.User;
import ru.cft.template.core.repository.UserRepository;

import java.util.Objects;

@Validated
@Service
@RequiredArgsConstructor
public class DefaultUserService implements UserService {
    private final UserRepository userRepository;
    private final ConversionService conversionService;

    @Override
    public UserDTO createUser(@Valid NewUserPayload userPayload) {
        User savedUser = this.userRepository.save(Objects.requireNonNull(this.conversionService.convert(userPayload, User.class)));
        return this.conversionService.convert(savedUser, UserDTO.class);
    }

    @Override
    public UserDTO patchUser(PatchUserPayload userPayload) {
        return null;
    }

    @Override
    public Iterable<UserDTO> findAllUsers() {
    }

    @Override
    public UserDTO findUserById(Long id) {
    }

    @Override
    public UserDTO findUserByEmail(String email) {
    }

    @Override
    public UserDTO findUserByPhoneNumber(String phoneNumber) {
    }
}
