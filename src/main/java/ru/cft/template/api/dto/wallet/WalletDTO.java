package ru.cft.template.api.dto.wallet;

public record WalletDTO(
        Long id,
        Long userId,
        Long balance
) {
}