package ru.cft.template.core.service.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ru.cft.template.api.dto.UserDTO;
import ru.cft.template.api.payload.NewUserPayload;
import ru.cft.template.api.payload.PatchUserPayload;
import ru.cft.template.core.entity.User;
import ru.cft.template.core.exception.EntityNotFoundException;
import ru.cft.template.core.repository.UserRepository;
import ru.cft.template.core.utils.PasswordUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Validated
@Service
@RequiredArgsConstructor
public class DefaultUserService implements UserService {
    private final UserRepository userRepository;

    private final ConversionService conversionService;
    private final PasswordUtil passwordUtil;

    @Override
    @Transactional
    public UserDTO createUser(@Valid NewUserPayload userPayload) {
        User user = this.conversionService.convert(userPayload, User.class);
        user.setPasswordHash(this.passwordUtil.encode(user.getPasswordHash()));
        return this.conversionService.convert(this.userRepository.save(user), UserDTO.class);
    }

    @Override
    @Transactional
    public UserDTO patchUser(Long id, @Valid PatchUserPayload userPayload) {
        User user = this.userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(User.class.getSimpleName(), Map.of("id", String.valueOf(id))));
        if (Objects.nonNull(userPayload.firstName())) user.setFirstName(userPayload.firstName());
        if (Objects.nonNull(userPayload.middleName())) user.setMiddleName(userPayload.middleName());
        if (Objects.nonNull(userPayload.lastName())) user.setLastName(userPayload.lastName());
        if (Objects.nonNull(userPayload.birthdate())) user.setBirthdate(LocalDate.parse(userPayload.birthdate(), DateTimeFormatter.ISO_DATE));
        user.setLastUpdateDateTime(LocalDateTime.now());
        return this.conversionService.convert(user, UserDTO.class);
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
