package ru.cft.template.api.dto.wallet;

public record WalletHesoyamDTO(
        Long userId,
        String outcome,
        Long amount,
        Long balance
) {
}
