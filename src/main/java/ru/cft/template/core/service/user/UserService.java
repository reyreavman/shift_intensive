package ru.cft.template.core.service.user;

import jakarta.validation.Valid;
import ru.cft.template.api.dto.UserDTO;
import ru.cft.template.api.payload.NewUserPayload;
import ru.cft.template.api.payload.PatchUserPayload;

import java.util.List;

public interface UserService {
    UserDTO createUser(@Valid NewUserPayload userPayload);

    UserDTO patchUser(@Valid PatchUserPayload userPayload);

    List<UserDTO> findAllUsers();

    UserDTO findUserById(Long id);

    UserDTO findUserByEmail(String email);

    UserDTO findUserByPhoneNumber(String phoneNumber);
}
