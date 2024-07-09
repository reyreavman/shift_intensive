package ru.cft.template.api.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Builder
public record CreateUserDTO(
        @NotNull(message = "{wallet.users.create.errors.first_name_is_null}")
        @Pattern(regexp = "^[А-ЯЁ][а-яё]{1,50}$", message = "{wallet.users.create.errors.name_condition_is_invalid}")
        String firstName,

        @Pattern(regexp = "^[А-ЯЁ][а-яё]{1,50}$", message = "{wallet.users.create.errors.name_condition_is_invalid}")
        String middleName,

        @NotNull(message = "{wallet.users.create.errors.last_name_is_null}")
        @Pattern(regexp = "^[А-ЯЁ][а-яё]{1,50}$", message = "{wallet.users.create.errors.name_condition_is_invalid}")
        String lastName,

        @NotNull(message = "{wallet.users.create.errors.phoneNumber_is_null}")
        @Pattern(regexp = "^7\\d{10}$", message = "{wallet.users.create.errors.phoneNumber_condition_is_invalid}")
        String phoneNumber,

        @NotNull(message = "{wallet.users.create.errors.email_is_null}")
        @Email(message = "{wallet.users.create.errors.email_condition_is_invalid}")
        String email,

        @NotNull(message = "{wallet.users.create.errors.birthdate_is_null}")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate birthdate,

        @NotNull(message = "{wallet.users.create.errors.password_is_null}")
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!?])[a-zA-Z0-9!?]{8,64}$", message = "{wallet.users.create.errors.password_condition_is_invalid}")
        String password
) {
}
