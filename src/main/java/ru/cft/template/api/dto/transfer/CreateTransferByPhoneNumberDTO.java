package ru.cft.template.api.dto.transfer;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public record CreateTransferByPhoneNumberDTO(
        @NotNull(message = "{wallet.transfers.create.errors.recipient_phoneNumber_is_null}")
        @Pattern(regexp = "^7\\d{10}$", message = "{wallet.transfers.create.errors.recipient_phoneNumber_condition_is_invalid}")
        String recipientPhoneNumber,

        @NotNull(message = "{wallet.transfers.create.errors.amount_is_null}")
        @Positive(message = "{wallet.transfers.create.errors.amount_is_negative}")
        Long amount
) {
}
