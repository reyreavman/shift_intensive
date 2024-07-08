package ru.cft.template.core.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class NotEnoughMoneyException extends RuntimeException {
    private final Long userId;
    private final Long balance;
    private final Long transferAmount;
}
