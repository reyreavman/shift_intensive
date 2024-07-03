package ru.cft.template.core.service;

import ru.cft.template.api.dto.UserDTO;

import java.time.LocalDate;

public interface UserService {
    UserDTO createUser(String firstName, String middleName, String lastName, String phoneNumber, String email, LocalDate birthdate, String password);

    UserDTO patchUser(String firstName, String middleName, String lastName, LocalDate birthdate);

    Iterable<UserDTO> findAllUsers();

    UserDTO findUserById(int id);

    UserDTO findUserByEmail(String email);

    UserDTO findUserByPhoneNumber(String phoneNumber);
}
