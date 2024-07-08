package ru.cft.template.api.dto.wallet;

public record WalletHesoyamDTO(
        Long userId,
        HesoyamResult results,
        Long amount,
        Long balance
) {
}
