package ru.cft.template.api.dto;

public record WalletHesoyamDTO(
        Long userId,
        String outcome,
        Long amount,
        Long balance
) {
}
