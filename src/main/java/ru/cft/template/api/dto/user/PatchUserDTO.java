package ru.cft.template.api.dto.user;


import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Builder
public record PatchUserDTO(
        @Pattern(regexp = "^[А-ЯЁ][а-яё]{1,50}$", message = "{wallet.users.patch.errors.name_condition_is_invalid}")
        String firstName,

        @Pattern(regexp = "^[А-ЯЁ][а-яё]{1,50}$", message = "{wallet.users.patch.errors.name_condition_is_invalid}")
        String middleName,

        @Pattern(regexp = "^[А-ЯЁ][а-яё]{1,50}$", message = "{wallet.users.patch.errors.name_condition_is_invalid}")
        String lastName,

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate birthdate
) {
}
