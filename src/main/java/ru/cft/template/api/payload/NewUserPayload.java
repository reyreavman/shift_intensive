package ru.cft.template.api.payload;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record NewUserPayload(
        @NotNull
        String firstName,
        String middleName,
        @NotNull
        String lastName,
        @NotNull
        String phoneNumber,
        @NotNull
        String email,
        @NotNull
        LocalDate birthdate,
        @NotNull
        String password
) {
}
