package ru.cft.template.core.service.user;

import ru.cft.template.api.dto.user.CreateUserDTO;
import ru.cft.template.api.dto.user.PatchUserDTO;
import ru.cft.template.api.dto.user.UserDTO;
import ru.cft.template.core.entity.User;

import java.util.List;

public interface UserService {
    UserDTO createUser(CreateUserDTO createUserDTO);

    UserDTO patchUser(Long id, PatchUserDTO userPayload);

    List<UserDTO> findAllUsers();

    UserDTO findUserById(Long id);

    User findUserByIdEntity(Long id);

    UserDTO findUserByEmail(String email);

    UserDTO findUserByPhoneNumber(String phoneNumber);
}
