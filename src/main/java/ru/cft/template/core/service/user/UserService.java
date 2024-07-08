package ru.cft.template.core.service.user;

import ru.cft.template.api.dto.UserDTO;
import ru.cft.template.api.payload.user.PatchUserPayload;
import ru.cft.template.api.payload.user.UserPayload;

import java.util.List;

public interface UserService {
    UserDTO createUser(UserPayload userPayload);

    UserDTO patchUser(Long id, PatchUserPayload userPayload);

    List<UserDTO> findAllUsers();

    UserDTO findUserById(Long id);

    UserDTO findUserByEmail(String email);

    UserDTO findUserByPhoneNumber(String phoneNumber);
}
