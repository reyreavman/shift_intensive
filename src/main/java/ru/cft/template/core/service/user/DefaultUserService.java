package ru.cft.template.core.service.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.cft.template.api.dto.UserDTO;
import ru.cft.template.api.payload.NewUserPayload;
import ru.cft.template.api.payload.PatchUserPayload;
import ru.cft.template.core.entity.User;
import ru.cft.template.core.exception.EntityNotFoundException;
import ru.cft.template.core.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Validated
@Service
@RequiredArgsConstructor
public class DefaultUserService implements UserService {
    private final UserRepository userRepository;
    private final DefaultConversionService conversionService;

    @Override
    public UserDTO createUser(@Valid NewUserPayload userPayload) {
        User savedUser = this.userRepository.save(Objects.requireNonNull(this.conversionService.convert(userPayload, User.class)));
        return this.conversionService.convert(savedUser, UserDTO.class);
    }

    @Override
    public UserDTO patchUser(@Valid PatchUserPayload userPayload) {
        //TODO: patchUser
        return null;
    }

    @Override
    public List<UserDTO> findAllUsers() {
        List<UserDTO> users = new ArrayList<>();
        this.userRepository.findAll().forEach(user -> users.add(this.conversionService.convert(user, UserDTO.class)));
        return users;
    }

    @Override
    public UserDTO findUserById(Long id) {
        User user = this.userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(User.class.getSimpleName(), Map.of("id", String.valueOf(id))));
        return this.conversionService.convert(user, UserDTO.class);
    }

    @Override
    public UserDTO findUserByEmail(String email) {
        User user = this.userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException(User.class.getSimpleName(), Map.of("email", email)));
        return this.conversionService.convert(user, UserDTO.class);
    }

    @Override
    public UserDTO findUserByPhoneNumber(String phoneNumber) {
        User user = this.userRepository.findByPhoneNumber(phoneNumber).orElseThrow(() -> new EntityNotFoundException(User.class.getSimpleName(), Map.of("phoneNumber", phoneNumber)));
        return this.conversionService.convert(user, UserDTO.class);
    }
}
