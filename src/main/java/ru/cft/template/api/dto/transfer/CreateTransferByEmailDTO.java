package ru.cft.template.api.dto.transfer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateTransferByEmailDTO(
        @NotNull(message = "{wallet.transfers.create.errors.recipient_email_is_null}")
        @Email(message = "{wallet.transfers.create.errors.recipient_email_condition_is_invalid}")
        String recipientEmail,

        @NotNull(message = "{wallet.transfers.create.errors.amount_is_null}")
        @Positive(message = "{wallet.transfers.create.errors.amount_is_negative}")
        Long amount
) {
}
