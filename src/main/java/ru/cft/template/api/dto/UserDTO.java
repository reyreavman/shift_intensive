package ru.cft.template.api.dto;

import java.time.LocalDate;

public record UserDTO(
        Long id,
        String firstName,
        String middleName,
        String lastName,
        String phoneNumber,
        String email,
        LocalDate birthdate
) {
}
