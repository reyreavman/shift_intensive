package ru.cft.template.api.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record NewSessionByEmailPayload(
        @NotNull
        @Email(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "{wallet.sessions.create.errors.email_condition_is_invalid}")
        String email,

        @NotNull
        @Size(min = 8, max = 64, message = "{wallet.sessions.create.errors.password_size_is_invalid}")
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!?])[a-zA-Z0-9!?]{8,64}$", message = "{wallet.sessions.create.errors.password_condition_is_invalid}")
        String password
) {
}
