package ru.cft.template.api.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record SessionByEmailPayload(
        @NotNull(message = "{wallet.sessions.create.errors.email_is_null}")
        @Email(message = "{wallet.sessions.create.errors.email_condition_is_invalid}")
        String email,

        @NotNull(message = "{wallet.sessions.create.errors.password_is_null}")
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!?])[a-zA-Z0-9!?]{8,64}$", message = "{wallet.sessions.create.errors.password_condition_is_invalid}")
        String password
) {
}
