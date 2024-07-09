package ru.cft.template.core.mapper;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.cft.template.api.dto.user.CreateUserDTO;
import ru.cft.template.api.dto.user.PatchUserDTO;
import ru.cft.template.api.dto.user.UserDTO;
import ru.cft.template.core.entity.User;

import java.util.Objects;

@Component
public class UserMapper {
    public UserDTO mapToUserDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .middleName(user.getMiddleName())
                .lastName(user.getLastName())
                .phoneNumber(user.getPhoneNumber())
                .email(user.getEmail())
                .birthdate(user.getBirthdate())
                .build();
    }

    public User mapToUserPatch(User userToPatch, PatchUserDTO userPayload) {
        userToPatch.setFirstName(Objects.nonNull(userPayload.firstName()) ? userPayload.firstName() : userToPatch.getFirstName());
        userToPatch.setMiddleName(Objects.nonNull(userPayload.middleName()) ? userPayload.middleName() : userToPatch.getMiddleName());
        userToPatch.setLastName(Objects.nonNull(userPayload.lastName()) ? userPayload.lastName() : userToPatch.getLastName());
        userToPatch.setBirthdate(Objects.nonNull(userPayload.birthdate()) ? userPayload.birthdate() : userToPatch.getBirthdate());
        return userToPatch;
    }

    public User mapToUser(CreateUserDTO payload, PasswordEncoder encoder) {
        return User.builder()
                .id(null)
                .firstName(payload.firstName())
                .middleName(payload.middleName())
                .lastName(payload.lastName())
                .phoneNumber(payload.phoneNumber())
                .email(payload.email())
                .birthdate(payload.birthdate())
                .passwordHash(encoder.encode(payload.password()))
                .build();
    }
}
