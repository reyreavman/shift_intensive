package ru.cft.template.api.payload;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record NewSessionByPhoneNumberPayload(
        @NotNull
        @Size(min = 11, max = 11, message = "{wallet.sessions.create.errors.phoneNumber_size_is_invalid}")
        @Pattern(regexp = "^7\\d{10}$", message = "{wallet.sessions.create.errors.phoneNumber_condition_is_invalid}")
        String phoneNumber,

        @NotNull
        @Size(min = 8, max = 64, message = "{wallet.sessions.create.errors.password_size_is_invalid}")
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!?])[a-zA-Z0-9!?]{8,64}$", message = "{wallet.sessions.create.errors.password_condition_is_invalid}")
        String password) {
}
