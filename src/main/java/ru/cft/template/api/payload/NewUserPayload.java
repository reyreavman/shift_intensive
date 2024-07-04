package ru.cft.template.api.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Builder
public record NewUserPayload(
        @NotNull(message = "{wallet.users.create.errors.first_name_is_null}")
        @Size(min = 1, max = 50, message = "{wallet.users.create.errors.first_name_size_is_invalid}")
        @Pattern(regexp = "^[À-ß¨][à-ÿ¸]{1,50}$", message = "{wallet.users.create.errors.name_condition_is_invalid}")
        String firstName,

        @Size(min = 1, max = 50, message = "{wallet.users.create.errors.middle_name_size_is_invalid}")
        @Pattern(regexp = "^[À-ß¨][à-ÿ¸]{1,50}$", message = "{wallet.users.create.errors.name_condition_is_invalid}")
        String middleName,

        @NotNull(message = "{wallet.users.create.errors.last_name_is_null}")
        @Size(min = 1, max = 50, message = "{wallet.users.create.errors.last_name_size_is_invalid}")
        @Pattern(regexp = "^[À-ß¨][à-ÿ¸]{1,50}$", message = "{wallet.users.create.errors.name_condition_is_invalid}")
        String lastName,

        @NotNull
        @Size(min = 11, max = 11, message = "{wallet.users.create.errors.phoneNumber_size_is_invalid}")
        @Pattern(regexp = "^7\\d{10}$", message = "{wallet.users.create.errors.phoneNumber_condition_is_invalid}")
        String phoneNumber,

        @NotNull
        @Email(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "{wallet.users.create.errors.email_condition_is_invalid}")
        String email,

        @NotNull
        @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}$", message = "{wallet.users.create.errors.birthdate_condition_is_invalid}")
        String birthdate,

        @NotNull
        @Size(min = 8, max = 64, message = "{wallet.users.create.errors.password_size_is_invalid}")
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!?])[a-zA-Z0-9!?]{8,64}$", message = "{wallet.users.create.errors.password_condition_is_invalid}")
        String password
) {
}
