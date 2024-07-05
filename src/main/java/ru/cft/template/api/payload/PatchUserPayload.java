package ru.cft.template.api.payload;


import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record PatchUserPayload(
        @Size(min = 1, max = 50, message = "{wallet.users.patch.errors.first_name_size_is_invalid}")
        @Pattern(regexp = "^[А-ЯЁ][а-яё]{1,50}$", message = "{wallet.users.patch.errors.name_condition_is_invalid}")
        String firstName,

        @Size(min = 1, max = 50, message = "{wallet.users.patch.errors.middle_name_size_is_invalid}")
        @Pattern(regexp = "^[А-ЯЁ][а-яё]{1,50}$", message = "{wallet.users.patch.errors.name_condition_is_invalid}")
        String middleName,

        @Size(min = 1, max = 50, message = "{wallet.users.patch.errors.last_name_size_is_invalid}")
        @Pattern(regexp = "^[А-ЯЁ][а-яё]{1,50}$", message = "{wallet.users.patch.errors.name_condition_is_invalid}")
        String lastName,

        @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}$", message = "{wallet.users.patch.errors.birthdate_condition_is_invalid}")
        LocalDate birthdate
) {
}
