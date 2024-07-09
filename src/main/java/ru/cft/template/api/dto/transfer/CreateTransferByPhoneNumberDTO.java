package ru.cft.template.api.dto.transfer;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record CreateTransferByPhoneNumberDTO(
        @NotNull(message = "{wallet.transfers.create.errors.recipient_phoneNumber_is_null}")
        @Pattern(regexp = "^7\\d{10}$", message = "{wallet.transfers.create.errors.recipient_phoneNumber_condition_is_invalid}")
        String recipientPhoneNumber,

        @NotNull(message = "{wallet.transfers.create.errors.amount_is_null}")
        @Pattern(regexp = "^\\d+$", message = "{wallet.transfers.create.errors.amount_is_not_integer}")
        Long amount
) {
}
