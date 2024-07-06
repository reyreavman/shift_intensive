package ru.cft.template.api.dto;

public record WalletDTO(
        Long id,
        Long userId,
        Long balance
) {
}
