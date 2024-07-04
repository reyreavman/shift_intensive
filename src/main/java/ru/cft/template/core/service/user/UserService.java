package ru.cft.template.core.service.user;

import jakarta.validation.Valid;
import ru.cft.template.api.dto.UserDTO;
import ru.cft.template.api.payload.NewUserPayload;
import ru.cft.template.api.payload.PatchUserPayload;

public interface UserService {
    UserDTO createUser(@Valid NewUserPayload userPayload);

    UserDTO patchUser(PatchUserPayload userPayload);

    Iterable<UserDTO> findAllUsers();

    UserDTO findUserById(Long id);

    UserDTO findUserByEmail(String email);

    UserDTO findUserByPhoneNumber(String phoneNumber);
}
