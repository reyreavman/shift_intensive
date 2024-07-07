package ru.cft.template.api.payload.transfer;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record NewTransferByPhoneNumberPayload(
        @NotNull(message = "{wallet.transfers.create.errors.recipient_phoneNumber_is_null}")
        @Size(min = 11, max = 11, message = "{wallet.transfers.create.errors.recipient_phoneNumber_size_is_invalid}")
        @Pattern(regexp = "^7\\d{10}$", message = "{wallet.transfers.create.errors.recipient_phoneNumber_condition_is_invalid}")
        String phoneNumber,

        @NotNull(message = "{wallet.transfers.create.errors.amount_is_null}")
        @Positive(message = "{wallet.transfers.create.errors.amount_condition_is_invalid}")
        @Pattern(regexp = "^\\d+$", message = "{wallet.transfers.create.errors.amount_is_not_integer}")
        Integer amount
) {
}
