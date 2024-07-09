package ru.cft.template.api.dto.transfer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record CreateTransferByEmailDTO(
        @NotNull(message = "{wallet.transfers.create.errors.recipient_email_is_null}")
        @Email(message = "{wallet.transfers.create.errors.recipient_email_condition_is_invalid}")
        String recipientEmail,

        @NotNull(message = "{wallet.transfers.create.errors.amount_is_null}")
        @Pattern(regexp = "^\\d+$", message = "{wallet.transfers.create.errors.amount_is_not_integer}")
        Long amount
) {
}
