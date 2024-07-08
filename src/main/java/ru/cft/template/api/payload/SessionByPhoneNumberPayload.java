package ru.cft.template.api.payload;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record SessionByPhoneNumberPayload(
        @NotNull(message = "{wallet.sessions.create.errors.phone_number_is_null}")
        @Pattern(regexp = "^7\\d{10}$", message = "{wallet.sessions.create.errors.phoneNumber_condition_is_invalid}")
        String phoneNumber,

        @NotNull(message = "{wallet.sessions.create.errors.password_is_null}")
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!?])[a-zA-Z0-9!?]{8,64}$", message = "{wallet.sessions.create.errors.password_condition_is_invalid}")
        String password) {
}
