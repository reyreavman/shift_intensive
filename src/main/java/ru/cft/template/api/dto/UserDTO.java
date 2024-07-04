package ru.cft.template.api.dto;

import lombok.Builder;

import java.time.LocalDate;

@Builder
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
