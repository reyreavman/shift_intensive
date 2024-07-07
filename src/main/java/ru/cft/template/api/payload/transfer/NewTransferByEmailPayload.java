package ru.cft.template.api.payload.transfer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public record NewTransferByEmailPayload(
        @NotNull(message = "{wallet.transfers.create.errors.recipient_email_is_null}")
        @Email(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "{wallet.transfers.create.errors.recipient_email_condition_is_invalid}")
        String recipientEmail,

        @NotNull(message = "{wallet.transfers.create.errors.amount_is_null}")
        @Positive(message = "{wallet.transfers.create.errors.amount_condition_is_invalid}")
        @Pattern(regexp = "^\\d+$", message = "{wallet.transfers.create.errors.amount_is_not_integer}")
        Integer amount
) {
}
