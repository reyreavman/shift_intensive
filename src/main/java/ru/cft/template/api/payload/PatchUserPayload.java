package ru.cft.template.api.payload;


import java.time.LocalDate;

public record PatchUserPayload(
        String firstName,
        String middleName,
        String lastName,
        LocalDate birthdate
) {
}
